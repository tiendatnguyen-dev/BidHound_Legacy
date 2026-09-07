import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import dto.mapper.PlaceBidRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;

/**
 * Integration Test - Bước 7 của Plan.
 *
 * Yêu cầu trước khi chạy:
 *   1. MySQL đang chạy, đã có dữ liệu mẫu (users + items theo schema).
 *   2. ServerSide đang chạy trên port 8000.
 *   3. Item 101 đang ở trạng thái ACTIVE.
 *   4. User 1001 (Alice) balance >= 1_200_000, User 2002 (Bob) balance >= 1_500_000.
 *
 * Kịch bản test:
 *   - Alice và Bob cùng tham gia phòng item 101.
 *   - Alice đặt giá 1_200_000 (hợp lệ, lớn hơn giá khởi điểm 1_000_000).
 *   - Bob đặt giá 900_000 (KHÔNG hợp lệ, nhỏ hơn giá Alice vừa đặt -> phải nhận ERROR).
 *   - Bob đặt giá 1_500_000 (hợp lệ, Bob đang thắng).
 *   - Alice đặt giá 1_500_000 (KHÔNG hợp lệ, bằng giá hiện tại -> phải nhận ERROR).
 *   - Chờ auction kết thúc tự nhiên (server dùng initialSeconds=15 khi test).
 *
 * Sau khi test, kiểm tra trên MySQL Workbench:
 *   - Bảng bids: có 2 bản ghi mới (1_200_000 của Alice, 1_500_000 của Bob).
 *   - Bảng items: item 101 status = 'ENDED', winner_id = 2002, current_price = 1_500_000.
 *   - Bảng users: balance của Bob (2002) đã bị trừ 1_500_000.
 */
public class IntegrationTest {

  private static final String HOST    = "localhost";
  private static final int    PORT    = 8000;
  private static final Long   ITEM_ID = 101L;

  // ID người dùng khớp với dữ liệu mẫu trong DB
  private static final Long ALICE_ID = 1001L;
  private static final Long BOB_ID   = 2002L;

  public static void main(String[] args) throws InterruptedException {
    System.out.println("=== BẮT ĐẦU INTEGRATION TEST ===\n");

    // Khởi động Alice và Bob trên 2 thread riêng biệt
    Thread aliceThread = new Thread(() -> runClient("Alice", ALICE_ID), "Thread-Alice");
    Thread bobThread   = new Thread(() -> runClient("Bob",   BOB_ID),   "Thread-Bob");

    aliceThread.start();
    Thread.sleep(200); // Bob vào sau Alice 200ms
    bobThread.start();

    aliceThread.join();
    bobThread.join();

    System.out.println("\n=== KẾT THÚC INTEGRATION TEST ===");
    System.out.println("Hãy kiểm tra MySQL Workbench để xác nhận:");
    System.out.println("  SELECT * FROM bids    WHERE item_id = " + ITEM_ID + ";");
    System.out.println("  SELECT id, status, winner_id, current_price FROM items WHERE id = " + ITEM_ID + ";");
    System.out.println("  SELECT id, username, balance FROM users WHERE id IN (" + ALICE_ID + ", " + BOB_ID + ");");
  }

  /**
   * Mỗi client: kết nối -> JOIN_ROOM -> đặt các bid theo kịch bản -> lắng nghe cho đến khi auction kết thúc.
   */
  private static void runClient(String name, Long userId) {
    try (Socket socket = new Socket(HOST, PORT);
         BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         PrintWriter    out = new PrintWriter(socket.getOutputStream(), true)) {

      System.out.printf("[%s] Đã kết nối tới server.%n", name);

      // --- Thread lắng nghe phản hồi từ server ---
      Thread[] receiverHolder = new Thread[1];
      boolean[] auctionEnded = {false};

      Thread receiver = new Thread(() -> {
        try {
          String line;
          while ((line = in.readLine()) != null) {
            MessageEnvelop env = JsonConverter.fromJson(line, MessageEnvelop.class);
            printReceived(name, env);
            if (env.type() == MessageType.AUCTION_ENDED) {
              auctionEnded[0] = true;
              break;
            }
          }
        } catch (Exception e) {
          // Server đóng kết nối sau khi auction ended
          if (!auctionEnded[0]) {
            System.out.printf("[%s] Kết nối đóng sớm: %s%n", name, e.getMessage());
          }
        }
      });
      receiver.setDaemon(true);
      receiver.start();
      receiverHolder[0] = receiver;

      // --- Gửi JOIN_ROOM ---
      sendMessage(out, new MessageEnvelop(MessageType.JOIN_ROOM, String.valueOf(ITEM_ID)));
      System.out.printf("[%s] -> JOIN_ROOM (item: %d)%n", name, ITEM_ID);
      Thread.sleep(300);

      // --- Kịch bản đặt giá ---
      if (name.equals("Alice")) {
        // Hợp lệ: 1_200_000 > giá khởi điểm 1_000_000
        placeBid(out, name, ITEM_ID, userId, new BigDecimal("1200000"));
        Thread.sleep(500);

        // Không hợp lệ: 1_500_000 == giá Bob sẽ đặt (test sau) -> Alice thử lại bằng giá Bob
        // Đây là test case: bằng giá hiện tại phải bị reject
        Thread.sleep(1500); // Chờ Bob đặt 1_500_000 trước
        placeBid(out, name, ITEM_ID, userId, new BigDecimal("1500000"));

      } else { // Bob
        Thread.sleep(700); // Chờ Alice đặt giá xong
        // Không hợp lệ: 900_000 < 1_200_000 (giá Alice vừa đặt)
        placeBid(out, name, ITEM_ID, userId, new BigDecimal("900000"));
        Thread.sleep(300);

        // Hợp lệ: 1_500_000 > 1_200_000
        placeBid(out, name, ITEM_ID, userId, new BigDecimal("1500000"));
      }

      // --- Lắng nghe cho đến khi auction kết thúc ---
      receiver.join(60_000); // Timeout 60 giây
      System.out.printf("[%s] Đã xong.%n", name);

    } catch (Exception e) {
      System.err.printf("[%s] Lỗi: %s%n", name, e.getMessage());
    }
  }

  private static void placeBid(PrintWriter out, String name, Long itemId, Long bidderId, BigDecimal amount) {
    PlaceBidRequest request = new PlaceBidRequest(itemId, bidderId, amount);
    String payload = JsonConverter.toJson(request);
    sendMessage(out, new MessageEnvelop(MessageType.PLACE_BID, payload));
    System.out.printf("[%s] -> PLACE_BID amount=%.0f%n", name, amount);
  }

  private static void sendMessage(PrintWriter out, MessageEnvelop messageEnvelop) {
    out.println(JsonConverter.toJson(messageEnvelop));
  }

  private static void printReceived(String name, MessageEnvelop env) {
    String label = switch (env.type()) {
      case TIMER_TICK    -> "[TICK]";
      case BID_BROADCAST -> "[BID_BROADCAST]";
      case TIMER_EXTEND  -> "[TIMER_EXTEND]";
      case AUCTION_ENDED -> "[AUCTION_ENDED]";
      case ERROR         -> "[ERROR]";
      case JOIN_SUCCESS  -> "[JOIN_SUCCESS]";
      default            -> "[" + env.type() + "]";
    };
    // Bỏ qua TIMER_TICK để tránh spam log
    if (env.type() != MessageType.TIMER_TICK) {
      System.out.printf("[%s] <- %s %s%n", name, label, env.payload());
    }
  }
}
