package network.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.entities.User;
import dto.util.HttpUtils;
import dto.util.JsonConverter;
import service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LoginHandler implements HttpHandler {
  UserService userService = new UserService();
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      exchange.getResponseHeaders().set("Content-Type", "application/json");

      InputStream is = exchange.getRequestBody();
      String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      User user = JsonConverter.fromJson(json, User.class);

      if (userService.authenticateUser(user.getId(), user.getUsername())) {
        String responseText = "{\"status\":\"SUCCESS\", \"message\":\"Login successfully!\"}";
        HttpUtils.sendResponse(exchange,200, responseText);
      } else {
        String errorJson = "{\"status\":\"ERROR\", \"message\":\"Sai tên đăng nhập hoặc mật khẩu!\"}";
        HttpUtils.sendResponse(exchange, 401, errorJson);
      }
    } else {
      exchange.sendResponseHeaders(405,-1);
    }
  }
}
