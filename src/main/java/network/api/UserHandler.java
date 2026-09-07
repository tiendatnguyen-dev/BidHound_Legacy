package network.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.entities.Item;
import dto.util.HttpUtils;
import dto.util.JsonConverter;
import service.ItemService;

import java.io.IOException;
import java.util.List;

public class UserHandler implements HttpHandler {
  ItemService itemService = new ItemService();

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("GET".equals(exchange.getRequestMethod())) {
      String path = exchange.getRequestURI().getPath();
      String[] parts = path.split("/");

      if (parts.length == 5 && "history".equals(parts[4])) {
        Long userId = Long.parseLong(parts[3]);
        List<Item> history = itemService.getUserAuctionHistory(userId);
        String json = JsonConverter.toJson(history);
        HttpUtils.sendResponse(exchange, 200, json);
      } else {
        HttpUtils.sendResponse(exchange, 404, "Not Found");
      }
    }
  }
}

