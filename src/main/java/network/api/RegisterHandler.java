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

public class RegisterHandler implements HttpHandler {
  UserService userService = new UserService();
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    if ("POST".equals(exchange.getRequestMethod())) {
      InputStream is = exchange.getRequestBody();
      String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      User user = JsonConverter.fromJson(json, User.class);
      if (userService.registerUser(user.getUsername())) {
        HttpUtils.sendResponse(exchange,201,
                "{\"status\":\"SUCCESS\", \"message\":\"Tạo tài khoản thành công!\"}");
      } else {
        HttpUtils.sendResponse(exchange,400,
                "{\"status\":\"ERROR\", \"message\":\"Tài khoản đã tồn tại!\"}");
      }

    } else {
      HttpUtils.sendResponse(exchange, 405, "{\"status\":\"ERROR\", \"message\":\"Method Not Allowed\"}");
    }
  }
}
