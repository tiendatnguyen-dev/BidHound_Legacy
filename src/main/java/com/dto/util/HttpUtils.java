package com.dto.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

  public static HttpResponse<String> sendPost(String url, String jsonBody) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
