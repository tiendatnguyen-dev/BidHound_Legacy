package ui;

import dto.entities.Bid;
import dto.entities.Item;
import dto.mapper.PlaceBidRequest;
import dto.mapper.PlaceBidResponse;
import dto.util.AlertBox;
import dto.util.JsonConverter;
import dto.util.MessageEnvelop;
import dto.util.MessageType;
import dto.util.TimeCounter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import network.socket.ClientSide;
import network.socket.NetworkMessageListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class AuctionRoomController implements NetworkMessageListener {

  @FXML
  private Label userBalanceLabel;
  @FXML
  private Label userNameLabel;
  @FXML
  private Label itemTitleLabel;
  @FXML
  private Label itemDetailLabel;
  @FXML
  private Label timerLabel;
  @FXML
  private VBox timerCardBox;
  @FXML
  private Label currentPriceLabel;
  @FXML
  private Label currentWinnerLabel;
  @FXML
  private TextField bidInputField;
  @FXML
  private Button placeBidButton;
  @FXML
  private ListView<PlaceBidResponse> bidHistoryListView;
  private Long itemId;
  private Long userId;


  @FXML
  public void initialize() {
    ClientSide.getInstance().addListener(this);
    bidHistoryListView.setCellFactory(listView -> new BidListCell());
  }

  @FXML
  private void handlePlaceBid(ActionEvent event) {
    String text = bidInputField.getText();
    if (text == null || text.trim().isEmpty()) {
      AlertBox.createAlert("ERROR", "Không hợp lệ!", "Vui lòng nhập số tiền muốn đặt giá!", "");
      return;
    }

    try {
      BigDecimal amount = new BigDecimal(text.trim());
      BigDecimal currentPrice = new BigDecimal(currentPriceLabel.getText().trim());

      if (amount.compareTo(currentPrice) > 0) {
        PlaceBidRequest request = new PlaceBidRequest(itemId, userId, amount);
        MessageEnvelop envelop = new MessageEnvelop(MessageType.PLACE_BID, JsonConverter.toJson(request));
        ClientSide.getInstance().send(envelop);
        bidInputField.clear();
      } else {
        AlertBox.createAlert("ERROR", "Không hợp lệ!", "Giá đặt phải lớn hơn giá hiện tại (" + currentPrice + ")!", "");
      }
    } catch (NumberFormatException e) {
      AlertBox.createAlert("ERROR", "Không hợp lệ!", "Vui lòng chỉ nhập số hợp lệ!", "");
    }
  }

  @FXML
  private void handleQuickBid50k(ActionEvent event) {
    quickBidAdd(new BigDecimal("50000"));
  }

  @FXML
  private void handleQuickBid100k(ActionEvent event) {
    quickBidAdd(new BigDecimal("100000"));
  }

  @FXML
  private void handleQuickBid500k(ActionEvent event) {
    quickBidAdd(new BigDecimal("500000"));
  }

  private void quickBidAdd(BigDecimal increment) {
    try {
      BigDecimal currentPrice = new BigDecimal(currentPriceLabel.getText().trim());
      BigDecimal newPrice = currentPrice.add(increment);
      bidInputField.setText(newPrice.toPlainString());
    } catch (Exception e) {
      AlertBox.createAlert("ERROR", "Lỗi", "Không thể tính toán giá nhanh!", "");
    }
  }

  @Override
  public void onMessageReceived(MessageEnvelop envelope) {
    MessageType type = envelope.type();
    switch (type) {
      case ROOM_INIT: {
        Platform.runLater(() -> {
          Item item = JsonConverter.fromJson(envelope.payload(), Item.class);

          itemTitleLabel.setText(item.getTitle());
          itemDetailLabel.setText("Mã sản phẩm: " + item.getId() + " | " + "Người bán: " + item.getSellerId());
          currentPriceLabel.setText(item.getCurrentPrice() != null ? item.getCurrentPrice().toString() : "0");
          currentWinnerLabel.setText(item.getWinnerId() != null ? item.getWinnerId().toString() : "Chưa có");

          int remainingSeconds = 0;
          if (item.getEndTime() != null) {
            remainingSeconds = (int) LocalDateTime.now().until(item.getEndTime(), ChronoUnit.SECONDS);
            remainingSeconds = Math.max(0, remainingSeconds);
          }
          timerLabel.setText(TimeCounter.timeFormatter(remainingSeconds));

          for (Bid bid : item.getBidHistory()) {
            PlaceBidResponse bidResponse = new PlaceBidResponse(
                    bid.getId(), bid.getItemId(), bid.getAmount(),
                    bid.getBidderId(), bid.getCreatedAt(), "History"
            );
            bidHistoryListView.getItems().add(bidResponse);
          }
        });
        break;
      }
      case LEAVE_ROOM: {
        ClientSide.getInstance().removeListener(this);
        break;
      }

      case TIMER_TICK: {
        int seconds = Integer.parseInt(envelope.payload());
        String formattedTime = TimeCounter.timeFormatter(seconds);
        Platform.runLater(() -> {
          timerLabel.setText(formattedTime);
          if (seconds <= 10) {
            timerLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
          } else {
            timerLabel.setStyle("-fx-text-fill: #2c3e50;");
          }
        });
        break;
      }

      case BID_BROADCAST: {
        PlaceBidResponse bid = JsonConverter.fromJson(envelope.payload(), PlaceBidResponse.class);
        Platform.runLater(() -> {
          currentWinnerLabel.setText(bid.highestBidderId().toString());
          currentPriceLabel.setText(bid.currentPrice().toString());
          bidHistoryListView.getItems().add(0, bid);
        });
        break;
      }
      case TIMER_EXTEND: {
        Platform.runLater(() -> {
          AlertBox.createAlert("INFORMATION", "Gia hạn", null, envelope.payload());

        });
        break;
      }
      case AUCTION_ENDED: {
        Platform.runLater(() -> {
          timerLabel.setText("ĐÃ KẾT THÚC");
          timerLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-weight: bold;");
          bidInputField.setDisable(true);
          placeBidButton.setDisable(true);

          AlertBox.createAlert("INFORMATION", "Kết Thúc Phiên Đấu Giá",
                  "Phiên đấu giá đã khép lại!", envelope.payload());
        });
        break;
      }
      case ERROR: {
        Platform.runLater(() -> {
          AlertBox.createAlert("ERROR", "Thông báo", "Không thể thực hiện!", envelope.payload());
        });
        break;
      }
    }
  }

  public void setRoomData(Long itemId, Long userId) {
    this.itemId = itemId;
    this.userId = userId;
    MessageEnvelop joinMsg = new MessageEnvelop(MessageType.JOIN_ROOM, String.valueOf(itemId));
    ClientSide.getInstance().send(joinMsg);
  }

  /**
   * Dọn dẹp trước khi đóng cửa sổ: gửi LEAVE_ROOM và hủy listener để tránh memory leak.
   */
  public void cleanup() {
    if (itemId != null) {
      ClientSide.getInstance().send(new MessageEnvelop(MessageType.LEAVE_ROOM, String.valueOf(itemId)));
    }
    ClientSide.getInstance().removeListener(this);
  }
}
