package dto.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
  public static void sendResponse(HttpExchange exchange, int statusCode, String response) {
    byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
    try {
      exchange.sendResponseHeaders(statusCode,bytes.length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(bytes);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
