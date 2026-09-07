package network;

import dto.entities.Bid;
import dto.mapper.BidMapper;
import dto.mapper.PlaceBidRequest;
import dto.mapper.PlaceBidResponse;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionRoom {
  private final Long itemId;
  private BigDecimal currentPrice = BigDecimal.ZERO;
  private Long currentWinnerId = null;
  private final Set<ClientHandler> clients = new CopyOnWriteArraySet<>();
  private final ReentrantLock lock = new ReentrantLock();

  public AuctionRoom(Long itemId) {
    this.itemId = itemId;
  }

  public void addClient(ClientHandler client) {
    clients.add(client);
  }

  public void removeClient(ClientHandler client) {
    clients.remove(client);
  }

  public void broadcast(MessageEnvelop message) {
    for (ClientHandler client : clients) {
      client.sendMessage(message);
    }
  }

  public void placeBid(ClientHandler sender, PlaceBidRequest placeBidRequest) {
    lock.lock();
    try {
      Bid bid = BidMapper.toEntity(placeBidRequest);
      if (bid.getAmount().compareTo(currentPrice) > 0) {
        currentPrice = bid.getAmount();
        currentWinnerId = bid.getBidderId();
        PlaceBidResponse response = BidMapper.toDTO(bid, "Success");
        MessageEnvelop messageEnvelop = new MessageEnvelop(MessageType.BID_BROADCAST, JsonConverter.toJson(response));
        broadcast(messageEnvelop);
      } else {
        MessageEnvelop messageEnvelop = new MessageEnvelop(MessageType.ERROR, "Bid phải lớn hơn giá đặt hiện tại.!");
        sender.sendMessage(messageEnvelop);
      }
    } finally {
      lock.unlock();
    }
  }
}
