package network.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSide {
  private static final int PORT = 8000;

  public static void main(String[] args) {
    System.out.println("Server Starting");
    try (ServerSocket serverSocket = new ServerSocket(PORT);
         ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      while (true) {;
        Socket socket = serverSocket.accept();
        System.out.println("A new client connected.");

        executorService.execute(new ClientHandler(socket));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
