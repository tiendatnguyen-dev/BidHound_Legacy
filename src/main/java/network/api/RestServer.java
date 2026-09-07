package network.api;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class RestServer {
  public static void main(String[] args) {
    try {
      HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080),0);
      httpServer.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
      httpServer.start();
      httpServer.createContext("/api/auth/login",new LoginHandler());
      httpServer.createContext("/api/auth/register", new RegisterHandler());
      httpServer.createContext("/api/items", new ItemHandler());

      System.out.println("REST API Server đang chạy tại port 8080...");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
