package network.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.entities.Item;
import dto.util.HttpUtils;
import dto.util.JsonConverter;
import service.ItemService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ItemHandler implements HttpHandler {
    ItemService itemService = new ItemService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String path = exchange.getRequestURI().getPath();
            String[] parts = path.split("/");
            if (parts.length == 3) {
                handleGetList(exchange);
            }
            else if (parts.length == 4) {
                String itemId = parts[3];
                Long convertedId = Long.parseLong(itemId);
                handleGetDetail(exchange, convertedId);

            }
        } else if ("POST".equals(exchange.getRequestMethod())) {
            String path = exchange.getRequestURI().getPath();
            InputStream is = exchange.getRequestBody();
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Item item = JsonConverter.fromJson(json, Item.class);
            String[] parts = path.split("/");
            if (parts.length == 3) {
                itemService.createItem(item);
                String response = JsonConverter.toJson(item);
                HttpUtils.sendResponse(exchange,200, response);
            }
        } else {
            HttpUtils.sendResponse(exchange, 404, "{\"status\":\"ERROR\", \"message\":\"Not Found\"}");

        }
    }

    private void handleGetList(HttpExchange exchange) {
        List<Item> items = itemService.getList();
        String json = JsonConverter.toJson(items);
        HttpUtils.sendResponse(exchange,200,json);
    }

    private void handleGetDetail(HttpExchange exchange, Long itemId) {
        Item item = itemService.getDetail(itemId);
        if (item != null) {
            String json = JsonConverter.toJson(item);
            HttpUtils.sendResponse(exchange, 200, json);
        }
        else {
            HttpUtils.sendResponse(exchange,404,"/Error/");
        }
    }
}
