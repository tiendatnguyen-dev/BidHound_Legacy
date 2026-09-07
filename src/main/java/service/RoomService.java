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

public class RoomService {
  private final ItemDao itemDao = new ItemDaoImpl();

  public void handleJoinRoom(ClientHandler client, MessageEnvelop message, AuctionRoom currentRoom) {
    Long itemId = Long.parseLong(message.payload());
    currentRoom = ClientManager.getInstance().getOrCreateRoom(itemId);
    currentRoom.addClient(client);

    if (currentRoom.getRemainingSeconds() == 0 && !currentRoom.isClosed()) {
      currentRoom.startAuction(15);
    }

    Item item = itemDao.findById(itemId);
    String payload = JsonConverter.toJson(item);

    client.sendMessage(new MessageEnvelop(MessageType.JOIN_SUCCESS, "Tham gia phòng " + itemId + " thành công!"));
    client.sendMessage(new MessageEnvelop(MessageType.ROOM_INIT, payload));

    System.out.println("Client đã tham gia phòng: " + itemId);
  }
}
