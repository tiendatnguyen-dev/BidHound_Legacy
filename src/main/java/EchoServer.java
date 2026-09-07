import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
  private static final int FIXED_THREAD_SIZE = 50;
  private static final int PORT = 8000;

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT);
         ExecutorService executorService = Executors.newFixedThreadPool(FIXED_THREAD_SIZE)) {
      while (true) {
        Socket socket = serverSocket.accept();
        System.out.println("A new client connected.");

        executorService.execute(new ClientHandler(socket));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
