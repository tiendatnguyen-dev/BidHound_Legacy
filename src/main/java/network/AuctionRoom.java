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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionRoom {
  private final Long itemId;
  private BigDecimal currentPrice = BigDecimal.ZERO;
  private Long currentWinnerId = null;
  private int remainingSeconds = 0;
  private boolean isClosed = false;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
      if (isClosed) {
        MessageEnvelop messageEnvelop = new MessageEnvelop(MessageType.ERROR, "The auction is closed!");
        sender.sendMessage(messageEnvelop);
      } else {
        Bid bid = BidMapper.toEntity(placeBidRequest);
        if (bid.getAmount().compareTo(currentPrice) > 0) {
          currentPrice = bid.getAmount();
          currentWinnerId = bid.getBidderId();
          PlaceBidResponse response = BidMapper.toDTO(bid, "Success");
          MessageEnvelop messageEnvelop = new MessageEnvelop(MessageType.BID_BROADCAST, JsonConverter.toJson(response));
          broadcast(messageEnvelop);
          if (remainingSeconds <= 10) {
            remainingSeconds += 15;
            MessageEnvelop message = new MessageEnvelop(MessageType.TIMER_EXTEND, "Hệ thống tự động gia hạn thêm" +
                    " 15 giây do có người đặt giá ở giây cuối!");
            broadcast(message);
          }
        } else {
          MessageEnvelop messageEnvelop = new MessageEnvelop(MessageType.ERROR, "Bid phải lớn hơn giá đặt hiện tại.!");
          sender.sendMessage(messageEnvelop);
        }
      }
    } finally {
      lock.unlock();
    }
  }

  public void startAuction(int initialSeconds) {
    this.remainingSeconds = initialSeconds;
    Runnable task = () -> {
      lock.lock();
      try {
        if (remainingSeconds > 0) {
          remainingSeconds--;
          MessageEnvelop tickMsg = new MessageEnvelop(MessageType.TIMER_TICK, String.valueOf(remainingSeconds));
          broadcast(tickMsg);
        } else {
          isClosed = true;
          String payload = "Phòng đấu giá cho sản phẩm " + itemId + " đã kết thúc! Người thắng: " +
                  "User " + currentWinnerId + " với giá: " + currentPrice;
          MessageEnvelop endMsg = new MessageEnvelop(MessageType.AUCTION_ENDED, payload);
          broadcast(endMsg);
          scheduler.shutdown();
        }
      } finally {
        lock.unlock();
      }
    };
    scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
    };

  public int getRemainingSeconds() {
    return remainingSeconds;
  }
  public boolean isClosed() {
    return isClosed;
  }
}
