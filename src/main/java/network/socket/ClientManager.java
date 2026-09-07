package network.socket;

import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
  private static volatile ClientManager instance;

  private ClientManager() {}
  public static ClientManager getInstance() {
    ClientManager clientManager = instance;
    if (clientManager == null) {
      synchronized (ClientManager.class) {
        clientManager = instance;
        if (clientManager == null) {
          clientManager = instance = new ClientManager();
        }
      }
    }
    return clientManager;
  }

  ConcurrentHashMap<Long, AuctionRoom> rooms = new ConcurrentHashMap<>();

  public AuctionRoom getOrCreateRoom(Long itemId) {
    return rooms.computeIfAbsent(itemId, id -> new AuctionRoom(itemId));
  }

  public AuctionRoom getRoom(Long itemId) {
    return rooms.get(itemId);
  }
}
