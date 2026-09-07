package service;

import dao.ItemDao;
import dao.ItemDaoImpl;
import dto.entities.Item;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import network.AuctionRoom;
import network.ClientHandler;
import network.ClientManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RoomService {
  private final ItemDao itemDao = new ItemDaoImpl();

  public AuctionRoom handleJoinRoom(ClientHandler client, MessageEnvelop message) {
    Long itemId = Long.parseLong(message.payload());
    AuctionRoom currentRoom = ClientManager.getInstance().getOrCreateRoom(itemId);
    currentRoom.addClient(client);
    Item item = itemDao.findById(itemId);

    if (currentRoom.getRemainingSeconds() == 0 && !currentRoom.isClosed()) {
      if (item.getEndTime() != null) {
        int remaining = (int) LocalDateTime.now().until(item.getEndTime(), ChronoUnit.SECONDS);
        currentRoom.startAuction(Math.max(remaining, 0));
      }
    }

    String payload = JsonConverter.toJson(item);
    client.sendMessage(new MessageEnvelop(MessageType.JOIN_SUCCESS, "Tham gia phòng " + itemId + " thành công!"));
    client.sendMessage(new MessageEnvelop(MessageType.ROOM_INIT, payload));

    System.out.println("Client đã tham gia phòng: " + itemId);
    return currentRoom;
  }
}
