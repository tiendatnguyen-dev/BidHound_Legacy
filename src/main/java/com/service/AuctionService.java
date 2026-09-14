package com.service;

import com.dao.DatabaseConnection;
import com.dto.entities.User;
import com.dao.BidDao;
import com.dao.BidDaoImpl;
import com.dao.ItemDao;
import com.dao.ItemDaoImpl;
import com.dao.TransactionDao;
import com.dao.TransactionDaoImpl;
import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dto.entities.Bid;
import com.dto.entities.Item;
import com.dto.mapper.BidMapper;
import com.dto.mapper.PlaceBidRequest;
import com.dto.mapper.PlaceBidResponse;
import com.dto.util.JsonConverter;
import com.dto.util.MessageEnvelop;
import com.dto.util.MessageType;
import com.network.socket.AuctionRoom;
import com.network.socket.ClientHandler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AuctionService {
  private final ItemDao itemDao;
  private final BidDao bidDao;
  private final UserDao userDao;
  private final TransactionDao transactionDao;

  public AuctionService() {
    DatabaseConnection db = new DatabaseConnection();
    this.itemDao = new ItemDaoImpl(new BidDaoImpl(db), db);
    this.bidDao = new BidDaoImpl(db);
    this.userDao = new UserDaoImpl(db);
    this.transactionDao = new TransactionDaoImpl(db);
  }

  public AuctionService(ItemDao itemDao, BidDao bidDao,
                        UserDao userDao, TransactionDao transactionDao) {
    this.itemDao = itemDao;
    this.bidDao = bidDao;
    this.userDao = userDao;
    this.transactionDao = transactionDao;
  }

  public void finalizeAuction(Long itemId, Long winnerId, BigDecimal finalPrice) {
    transactionDao.finalizeAuction(itemId, winnerId, finalPrice);
  }

  public void processBid(AuctionRoom auctionRoom, ClientHandler sender, PlaceBidRequest placeBidRequest) {
    if (auctionRoom.isClosed()) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "The auction is closed!"));
      return;
    }
    Bid bid = BidMapper.toEntity(placeBidRequest);
    if (bid.getAmount().compareTo(auctionRoom.getCurrentPrice()) <= 0) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "Bid phải lớn hơn giá đặt hiện tại!"));
      return;
    }

    User bidder = userDao.findUserById(placeBidRequest.bidderId());
    if (bidder == null || bidder.getBalance() == null || bidder.getBalance().compareTo(bid.getAmount()) < 0) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "Số dư không đủ để đặt giá này!"));
      return;
    }

    auctionRoom.setCurrentPrice(bid.getAmount());
    auctionRoom.setCurrentWinnerId(bid.getBidderId());

    bidDao.save(bid);

    PlaceBidResponse response = BidMapper.toDTO(bid, "Success");
    MessageEnvelop broadcastMsg = new MessageEnvelop(MessageType.BID_BROADCAST, JsonConverter.toJson(response));
    auctionRoom.broadcast(broadcastMsg);

    itemDao.updateCurrentPriceAndWinner(bid.getItemId(),bid.getAmount(),bid.getBidderId());

    Item currentItem = itemDao.findById(bid.getItemId());
    currentItem.addBid(bid);

    if (auctionRoom.getRemainingSeconds() <= 10) {
      auctionRoom.extendTime(15);
      MessageEnvelop extendMsg = new MessageEnvelop(
              MessageType.TIMER_EXTEND,
              "Hệ thống tự động gia hạn thêm 15 giây do có người đặt giá ở giây cuối!"
      );
      auctionRoom.broadcast(extendMsg);
      itemDao.updateEndTime(currentItem.getId(), LocalDateTime.now().plusSeconds(15));
    }
  }
}

