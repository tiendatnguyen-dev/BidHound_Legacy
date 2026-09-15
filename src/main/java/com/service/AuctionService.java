package com.service;

import com.dto.entities.User;
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
import com.repository.BidRepository;
import com.repository.ItemRepository;
import com.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuctionService {
  private final ItemRepository itemDao;
  private final BidRepository bidDao;
  private final UserRepository userDao;

  public AuctionService(ItemRepository itemDao, BidRepository bidDao,
                        UserRepository userDao) {
    this.itemDao = itemDao;
    this.bidDao = bidDao;
    this.userDao = userDao;
  }

  @Transactional
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

    User bidder = userDao.findById(placeBidRequest.bidderId()).orElse(null);
    if (bidder == null || bidder.getBalance() == null || bidder.getBalance().compareTo(bid.getAmount()) < 0) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "Số dư không đủ để đặt giá này!"));
      return;
    }

    auctionRoom.setCurrentPrice(bid.getAmount());
    auctionRoom.setCurrentWinnerId(bid.getBidderId());

    bidDao.save(bid);
    userDao.deductBalance(bidder.getId(), bid.getAmount());

    PlaceBidResponse response = BidMapper.toDTO(bid, "Success");
    MessageEnvelop broadcastMsg = new MessageEnvelop(MessageType.BID_BROADCAST, JsonConverter.toJson(response));
    auctionRoom.broadcast(broadcastMsg);

    Item currentItem = itemDao.findById(bid.getItemId()).orElse(null);
    if (currentItem != null) {
      currentItem.setCurrentPrice(bid.getAmount());
      currentItem.setWinnerId(bid.getBidderId());
      currentItem.addBid(bid);
      if (auctionRoom.getRemainingSeconds() <= 10) {
        auctionRoom.extendTime(15);
        MessageEnvelop extendMsg = new MessageEnvelop(
                MessageType.TIMER_EXTEND,
                "Hệ thống tự động gia hạn thêm 15 giây do có người đặt giá ở giây cuối!"
        );
        auctionRoom.broadcast(extendMsg);
      }
    }
  }
}

