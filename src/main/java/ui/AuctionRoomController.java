package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AuctionRoomController {

  @FXML private Label userBalanceLabel;
  @FXML private Label userNameLabel;
  @FXML private Label itemTitleLabel;
  @FXML private Label itemDetailLabel;
  @FXML private Label timerLabel;
  @FXML private VBox timerCardBox;
  @FXML private Label currentPriceLabel;
  @FXML private Label currentWinnerLabel;
  @FXML private TextField bidInputField;
  @FXML private Button placeBidButton;
  @FXML private ListView<String> bidHistoryListView;

  @FXML
  public void initialize() {
    // TODO: Tự tay khởi tạo dữ liệu, kết nối socket và listener tại đây
  }

  @FXML
  private void handlePlaceBid(ActionEvent event) {
    // TODO: Tự tay xử lý gửi request đặt giá qua socket
  }

  @FXML
  private void handleQuickBid50k(ActionEvent event) {
    // TODO: Tự tay xử lý nút tăng giá nhanh +50k
  }

  @FXML
  private void handleQuickBid100k(ActionEvent event) {
    // TODO: Tự tay xử lý nút tăng giá nhanh +100k
  }

  @FXML
  private void handleQuickBid500k(ActionEvent event) {
    // TODO: Tự tay xử lý nút tăng giá nhanh +500k
  }
}
