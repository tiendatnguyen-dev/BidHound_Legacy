package network;

import dto.mapper.PlaceBidRequest;
import dto.mapper.PlaceBidResponse;
import dto.entities.Bid;
import dto.mapper.BidMapper;
import dto.util.JsonConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
  private final Socket socket;

  public ClientHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      String jsonLine;
      while ((jsonLine = in.readLine()) != null) {
        PlaceBidRequest placeBidRequest = JsonConverter.fromJson(jsonLine, PlaceBidRequest.class);

        Bid bid = BidMapper.toEntity(placeBidRequest);

        PlaceBidResponse placeBidResponse = BidMapper.toDTO(bid, "Success");

        String jsonResponse = JsonConverter.toJson(placeBidResponse);
        out.println(jsonResponse);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
