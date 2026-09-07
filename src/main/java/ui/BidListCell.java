package ui;

import dto.mapper.PlaceBidResponse;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Custom cell để hiển thị mỗi lượt bid trong ListView.
 * Dòng đầu tiên (giá cao nhất) được đánh dấu bằng 👑 và viền vàng.
 */
public class BidListCell extends ListCell<PlaceBidResponse> {

  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
  private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Override
  protected void updateItem(PlaceBidResponse bid, boolean empty) {
    super.updateItem(bid, empty);

    if (empty || bid == null) {
      setText(null);
      setGraphic(null);
      setStyle("");
      return;
    }

    String formattedPrice = CURRENCY_FORMAT.format(bid.currentPrice()) + " ₫";
    String bidderInfo = "👤 Người dùng #" + bid.highestBidderId() + "  →  " + formattedPrice;

    Label bidderLabel = new Label(bidderInfo);
    bidderLabel.setFont(Font.font("System", FontWeight.BOLD, 13));

    String timeStr = bid.bidTime() != null ? "⏱ " + bid.bidTime().format(TIME_FORMAT) : "";
    Label timeLabel = new Label(timeStr);
    timeLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

    VBox content = new VBox(2, bidderLabel, timeLabel);
    content.setStyle("-fx-padding: 4 6 4 6;");

    if (getIndex() == 0) {
      bidderLabel.setText("👑 " + bidderInfo);
      setStyle("-fx-border-color: #f1c40f; -fx-border-width: 0 0 0 4; -fx-background-color: #fffbea;");
    } else {
      setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
    }

    setGraphic(content);
    setText(null);
  }
}
