package network.socket;

import dto.mapper.PlaceBidRequest;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import service.AuctionService;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Quản lí 1 AuctionRoom phía Server, giúp gửi thông báo về phía Client.
 */
public class AuctionRoom {
  private final Long itemId;
  private BigDecimal currentPrice = BigDecimal.ZERO;
  private Long currentWinnerId = null;
  private int remainingSeconds = 0;
  private boolean isClosed = false;
  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  private final Set<ClientHandler> clients = new CopyOnWriteArraySet<>();
  private final ReentrantLock lock = new ReentrantLock();
  private final AuctionService auctionService = new AuctionService();


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
      auctionService.processBid(this, sender, placeBidRequest);
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

          auctionService.finalizeAuction(this.itemId, this.currentWinnerId, this.currentPrice);

          scheduler.shutdown();
        }
      } finally {
        lock.unlock();
      }
    };
    scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
  }

  ;

  public int getRemainingSeconds() {
    return remainingSeconds;
  }

  public void setRemainingSeconds(int remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
  }

  public boolean isClosed() {
    return isClosed;
  }

  public BigDecimal getCurrentPrice() {
    return currentPrice;
  }

  public Long getItemId() {
    return itemId;
  }

  public Long getCurrentWinnerId() {
    return currentWinnerId;
  }

  public void setCurrentWinnerId(Long currentWinnerId) {
    this.currentWinnerId = currentWinnerId;
  }

  public void setCurrentPrice(BigDecimal currentPrice) {
    this.currentPrice = currentPrice;
  }

  public void extendTime(int seconds) {
    this.remainingSeconds += seconds;
  }
}


