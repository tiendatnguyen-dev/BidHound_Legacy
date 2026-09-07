package network;

import dto.mapper.PlaceBidRequest;
import dto.mapper.PlaceBidResponse;
import dto.util.JsonConverter;

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

      PlaceBidRequest request = new PlaceBidRequest(101L, 5001L, new BigDecimal("250000"));
      String jsonRequest = JsonConverter.toJson(request);
      out.println(jsonRequest);

      String jsonResponse = serverIn.readLine();
      System.out.println("[Client] Response from server: " + jsonResponse);
      PlaceBidResponse response = JsonConverter.fromJson(jsonResponse, PlaceBidResponse.class);
      System.out.println(response.statusMessage());
      System.out.println(response.currentPrice());
      System.out.println(response.bidTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
