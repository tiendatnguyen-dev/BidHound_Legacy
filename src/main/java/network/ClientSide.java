package network;

import dto.mapper.PlaceBidRequest;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;

public class ClientSide {
  private static final int PORT = 8000;

  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", PORT);
         BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      new Thread(() -> {
        try {
          String jsonResponse;
          while ((jsonResponse = serverIn.readLine()) != null) {
            MessageEnvelop responseEnv = JsonConverter.fromJson(jsonResponse, MessageEnvelop.class);
            System.out.println("[CLIENT NHẬN TIN] Type: " + responseEnv.type() + " | Payload: " + responseEnv.payload());
          }
        } catch (Exception e) {
          System.out.println("Kết nối tới Server bị đóng.");
        }
      }).start();

      MessageEnvelop joinMsg = new MessageEnvelop(MessageType.JOIN_ROOM, "101");
      out.println(JsonConverter.toJson(joinMsg));

      Thread.sleep(500);

      PlaceBidRequest request = new PlaceBidRequest(101L, 5001L, new BigDecimal("300000"));
      MessageEnvelop bidMsg = new MessageEnvelop(MessageType.PLACE_BID, JsonConverter.toJson(request));
      out.println(JsonConverter.toJson(bidMsg));

      Thread.sleep(10000);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
