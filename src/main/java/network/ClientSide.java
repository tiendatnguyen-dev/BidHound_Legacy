package network;

import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientSide {
  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 8000;

  private final List<NetworkMessageListener> listeners = new CopyOnWriteArrayList<>();
  private static volatile ClientSide instance;

  private Socket socket;
  private BufferedReader serverIn;
  private PrintWriter out;
  private boolean isRunning = false;

  private ClientSide() {}

  public static ClientSide getInstance() {
    if (instance == null) {
      synchronized (ClientSide.class) {
        if (instance == null) {
          instance = new ClientSide();
        }
      }
    }
    return instance;
  }

  /**
   * Kết nối tới Server và khởi chạy luồng lắng nghe ngầm
   */
  public synchronized void connect(String host, int port) throws IOException {
    if (socket != null && !socket.isClosed()) {
      return;
    }

    this.socket = new Socket(host, port);
    this.serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
    this.isRunning = true;

    Thread receiverThread = new Thread(this::listenLoop);
    receiverThread.setDaemon(true);
    receiverThread.start();
  }

  public void connect() throws IOException {
    connect(DEFAULT_HOST, DEFAULT_PORT);
  }

  private void listenLoop() {
    try {
      String jsonResponse;
      while (isRunning && (jsonResponse = serverIn.readLine()) != null) {
        MessageEnvelop env = JsonConverter.fromJson(jsonResponse, MessageEnvelop.class);
        if (env != null) {
          notifyListeners(env);
        }
      }
    } catch (IOException e) {
      System.out.println("Mất kết nối tới Server.");
    }
  }

  /**
   * Gửi MessageEnvelop lên Server
   */
  public void send(MessageEnvelop envelope) {
    if (out != null && envelope != null) {
      String json = JsonConverter.toJson(envelope);
      out.println(json);
    }
  }

  public void addListener(NetworkMessageListener listener) {
    listeners.add(listener);
  }

  public void removeListener(NetworkMessageListener listener) {
    listeners.remove(listener);
  }

  private void notifyListeners(MessageEnvelop env) {
    for (NetworkMessageListener listener : listeners) {
      listener.onMessageReceived(env);
    }
  }

  public void disconnect() {
    this.isRunning = false;
    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
      }
    } catch (IOException ignored) {}
  }
}
