package network;

import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSide {
  private static final int PORT = 8000;

  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", PORT);
         BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

      Thread receiverThread = new Thread(() -> {
        try {
          String jsonResponse;
          while ((jsonResponse = serverIn.readLine()) != null) {
            MessageEnvelop env = JsonConverter.fromJson(jsonResponse, MessageEnvelop.class);
            MessageType messageType = env.type();
            switch (messageType) {
                case TIMER_TICK ->
                        System.out.println("[COUNTER] Time remaining: " + env.payload() + " seconds");

                case BID_BROADCAST ->
                        System.out.println("[NEW_PRICE] " + env.payload());

                case TIMER_EXTEND ->
                        System.out.println("[TIMER_EXTEND] " + env.payload());

                case AUCTION_ENDED ->
                        System.out.println("[AUCTION_ENDED] Auction ended: " + env.payload());

                case ERROR ->
                        System.err.println("[ERROR] " + env.payload());

                default ->
                        System.out.println("[MESSAGE] " + env.type() + ": " + env.payload());
              }
            }
        } catch (Exception e) {
          System.out.println("Connection shut down.");
        }
      });

      receiverThread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
