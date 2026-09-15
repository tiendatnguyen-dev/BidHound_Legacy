package com.service;

import com.dto.entities.Item;
import com.dto.util.JsonConverter;
import com.dto.util.MessageEnvelop;
import com.dto.util.MessageType;
import com.network.socket.AuctionRoom;
import com.network.socket.ClientHandler;
import com.network.socket.ClientManager;
import com.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RoomService {
  private final ItemRepository itemDao;

  public RoomService(ItemRepository itemDao) {
    this.itemDao = itemDao;
  }

  public AuctionRoom handleJoinRoom(ClientHandler client, MessageEnvelop message) {
    Long itemId = Long.parseLong(message.payload());
    AuctionRoom currentRoom = ClientManager.getInstance().getOrCreateRoom(itemId);
    currentRoom.addClient(client);
    Item item = itemDao.findById(itemId).orElse(null);

    if (currentRoom.getRemainingSeconds() == 0 && !currentRoom.isClosed()) {
      if (item.getEndTime() != null) {
        currentRoom.setCurrentPrice(item.getCurrentPrice());
        currentRoom.setCurrentWinnerId(item.getWinnerId());
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
