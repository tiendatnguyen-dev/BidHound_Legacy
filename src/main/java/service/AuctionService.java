package service;

import dao.BidDao;
import dao.BidDaoImpl;
import dao.ItemDao;
import dao.ItemDaoImpl;
import dao.TransactionDao;
import dao.TransactionDaoImpl;
import dao.UserDao;
import dao.UserDaoImpl;
import dto.entities.Bid;
import dto.entities.User;
import dto.mapper.BidMapper;
import dto.mapper.PlaceBidRequest;
import dto.mapper.PlaceBidResponse;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import network.AuctionRoom;
import network.ClientHandler;

import java.math.BigDecimal;

public class AuctionService {
  private final ItemDao itemDao = new ItemDaoImpl();
  private final BidDao bidDao  = new BidDaoImpl();
  private final UserDao userDao = new UserDaoImpl();
  private final TransactionDao transactionDao = new TransactionDaoImpl();

  public void finalizeAuction(Long itemId, Long winnerId, BigDecimal finalPrice) {
    transactionDao.finalizeAuction(itemId, winnerId, finalPrice);
  }

  public boolean processBid(AuctionRoom auctionRoom, ClientHandler sender, PlaceBidRequest placeBidRequest) {
    if (auctionRoom.isClosed()) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "The auction is closed!"));
      return false;
    }
    Bid bid = BidMapper.toEntity(placeBidRequest);
    if (bid.getAmount().compareTo(auctionRoom.getCurrentPrice()) <= 0) {
      sender.sendMessage(new MessageEnvelop(MessageType.ERROR, "Bid phải lớn hơn giá đặt hiện tại!"));
      return false;
    }

    auctionRoom.setCurrentPrice(bid.getAmount());
    auctionRoom.setCurrentWinnerId(bid.getBidderId());

    bidDao.save(bid);

    PlaceBidResponse response = BidMapper.toDTO(bid, "Success");
    MessageEnvelop broadcastMsg = new MessageEnvelop(MessageType.BID_BROADCAST, JsonConverter.toJson(response));
    auctionRoom.broadcast(broadcastMsg);

    if (auctionRoom.getRemainingSeconds() <= 10) {
      auctionRoom.extendTime(15);
      MessageEnvelop extendMsg = new MessageEnvelop(
              MessageType.TIMER_EXTEND,
              "Hệ thống tự động gia hạn thêm 15 giây do có người đặt giá ở giây cuối!"
      );
      auctionRoom.broadcast(extendMsg);
    }
    return true;
  }
}

