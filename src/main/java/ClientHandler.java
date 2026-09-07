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
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println("[" + Thread.currentThread().getName() + "] Nhận từ Client: " + line);
        out.println("Server Echo: " + line);

        if ("quit".equalsIgnoreCase(line.trim()) || "exit".equalsIgnoreCase(line.trim())) {
          System.out.println("Client " + socket.getRemoteSocketAddress() + " đã ngắt kết nối.");
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
