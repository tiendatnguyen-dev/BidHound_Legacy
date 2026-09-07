package network;

import dto.entities.Item;
import dto.mapper.PlaceBidRequest;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
  private final Socket socket;
  private PrintWriter printWriter;
  private AuctionRoom currentRoom;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      this.printWriter = out;
      String jsonLine;
      while ((jsonLine = in.readLine()) != null) {
        MessageEnvelop messageEnvelop = JsonConverter.fromJson(jsonLine, MessageEnvelop.class);

        handleMessage(messageEnvelop);
      }
    } catch (IOException e) {
      System.out.println("Client đã ngắt kết nối.");
      if (this.currentRoom != null) {
        this.currentRoom.removeClient(this);
        this.currentRoom = null;
      }
    }
  }

  public void sendMessage(MessageEnvelop messageEnvelop) {
    if (printWriter != null) {
      String toJson = JsonConverter.toJson(messageEnvelop);
      printWriter.println(toJson);
    }
  }

  public void handleMessage(MessageEnvelop message) {
    switch (message.type()) {
      case JOIN_ROOM: {
        Long itemId = Long.parseLong(message.payload());
        this.currentRoom = ClientManager.getInstance().getOrCreateRoom(itemId);
        this.currentRoom.addClient(this);
        if (this.currentRoom.getRemainingSeconds() == 0 && !this.currentRoom.isClosed()) {
          this.currentRoom.startAuction(15);
        }
        System.out.println("Client đã tham gia phòng: " + itemId);
        sendMessage(new MessageEnvelop(MessageType.JOIN_SUCCESS, "Tham gia phòng " + itemId + " thành công!"));
        break;
      }
      case LEAVE_ROOM: {
        if (this.currentRoom != null) {
          this.currentRoom.removeClient(this);
          this.currentRoom = null;
        }
        break;
      }
      case PLACE_BID:
        PlaceBidRequest bidRequest = JsonConverter.fromJson(message.payload(), PlaceBidRequest.class);

        if (this.currentRoom != null) {
          this.currentRoom.placeBid(this, bidRequest);
          System.out.println("[NEW_BID]" + bidRequest.itemId() + ": " + bidRequest.amount() + " From " + bidRequest.bidderId());
        } else {
          sendMessage(new MessageEnvelop(MessageType.ERROR, "Bạn chưa tham gia phòng đấu giá nào!"));
        }
        break;
    }
  }
}
