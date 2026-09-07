package ui;

import dto.util.MessageEnvelop;
import dto.util.MessageType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.ClientSide;
import network.NetworkMessageListener;

public class AuctionRoomController implements NetworkMessageListener {

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
  private Long itemId;
  private Long userId;


  @FXML
  public void initialize() {
    ClientSide clientSide = ClientSide.getInstance();
    clientSide.addListener(this);
    }

  @FXML
  private void handlePlaceBid(ActionEvent event) {
    // TODO: xử lý gửi request đặt giá qua socket
  }

  @FXML
  private void handleQuickBid50k(ActionEvent event) {
    // TODO: xử lý nút tăng giá nhanh +50k
  }

  @FXML
  private void handleQuickBid100k(ActionEvent event) {
    // TODO: xử lý nút tăng giá nhanh +100k
  }

  @FXML
  private void handleQuickBid500k(ActionEvent event) {
    // TODO: xử lý nút tăng giá nhanh +500k
  }

  @Override
  public void onMessageReceived(MessageEnvelop envelope) {

  }

  public void setRoomData(Long itemId, Long userId) {
    this.itemId = itemId;
    this.userId = userId;
    MessageEnvelop joinMsg = new MessageEnvelop(MessageType.JOIN_ROOM, String.valueOf(itemId));
    ClientSide.getInstance().send(joinMsg);
  }
}
