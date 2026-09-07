import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {
  private static final int PORT = 8000;

  public static void main(String[] args) {
    try (Socket socket = new Socket("localhost", PORT);
         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
         BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      String line;
      while ((line = in.readLine()) != null) {
        out.println(line);
        String response = serverIn.readLine();
        System.out.println(response);

        if ("quit".equalsIgnoreCase(line.trim()) || "exit".equalsIgnoreCase(line.trim())) {
          break;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
