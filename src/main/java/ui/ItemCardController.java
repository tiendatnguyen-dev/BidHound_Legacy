package ui;

import com.dto.entities.Item;
import com.dto.util.AuctionStatus;
import com.dto.util.TimeCounter;
import com.dto.util.UserSession;
import com.dto.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ItemCardController {

  @FXML private Label titleLabel;
  @FXML private Label priceLabel;
  @FXML private Label durationLabel;
  @FXML private Button actionButton;

  private Item currentItem;

  /**
   * Bơm dữ liệu từ Item vào các thành phần giao diện của Card
   */
  public void setData(Item item) {
    this.currentItem = item;

    titleLabel.setText(item.getTitle());
    if (item.getCurrentPrice() != null) {
      priceLabel.setText(String.format("%,.0f ₫", item.getCurrentPrice()));
    }

    int remainingSeconds = (int) LocalDateTime.now().until(item.getEndTime(), ChronoUnit.SECONDS);
    remainingSeconds = Math.max(0, remainingSeconds);
    durationLabel.setText(TimeCounter.timeFormatter(remainingSeconds));

    // Ẩn nút nếu phiên đã kết thúc
    if (remainingSeconds <= 0 || AuctionStatus.ENDED.equals(item.getAuctionStatus())) {
      actionButton.setText("Đã kết thúc");
      actionButton.setDisable(true);
      actionButton.setStyle("-fx-background-color: #94a3b8; -fx-text-fill: white; "
          + "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 0;");
    }
  }

  @FXML
  private void handleAction(ActionEvent event) {
    Long userId = UserSession.getInstance().getCurrentUser().getId();
    AuctionRoomController controller =
        SceneManager.switchSceneWithController("/ui/AuctionRoom.fxml", "Phòng Đấu Giá: " + currentItem.getTitle());
    if (controller != null) {
      controller.setRoomData(currentItem.getId(), userId);
    }
  }
}