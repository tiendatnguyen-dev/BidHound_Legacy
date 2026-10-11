package ui;

import com.entities.Item;
import com.entities.User;
import com.util.AlertBox;
import com.util.SceneManager;
import com.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.service.ItemService;
import com.service.UserService;

import java.math.BigDecimal;
import java.util.List;

public class ProfileController {
    @FXML private Label userNameLabel;
    @FXML private Label userIdLabel;
    @FXML private Label balanceLabel;

    @FXML private TextField topUpAmountField;

    @FXML private TableView<Item> historyTableView;
    @FXML private TableColumn<Item, Long>   itemIdCol;
    @FXML private TableColumn<Item, String> titleCol;
    @FXML private TableColumn<Item, BigDecimal> priceCol;
    @FXML private TableColumn<Item, String> statusCol;
    @FXML private TableColumn<Item, Long>   resultCol;

    ItemService itemService;
    UserService userService;
    User user = UserSession.getInstance().getCurrentUser();

    @FXML
    public void initialize() {
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("winnerId"));

        resultCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Long winnerId, boolean empty) {
                super.updateItem(winnerId, empty);
                if (empty || winnerId == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                boolean isWinner = user != null && winnerId.equals(user.getId());
                setText(isWinner ? "Thắng" : "Thua");
                setStyle(isWinner
                        ? "-fx-text-fill: #2ecc71; -fx-font-weight: bold;"
                        : "-fx-text-fill: #e74c3c;");
            }
        });

        if (user != null) {
            userNameLabel.setText(user.getUsername());
            userIdLabel.setText("#" + user.getId());
            balanceLabel.setText(formatBalance(user.getBalance()));

            List<Item> bidHistory = itemService.getUserAuctionHistory(user.getId());
            if (bidHistory != null) {
                historyTableView.getItems().setAll(bidHistory);
            }
        }
    }

    @FXML
    private void handleTopUp() {
        String amountText = topUpAmountField.getText();
        if (amountText == null || amountText.trim().isEmpty()) {
            AlertBox.createAlert("WARNING", "Thông báo", "Vui lòng nhập số tiền muốn nạp!", "");
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountText.trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                AlertBox.createAlert("WARNING", "Thông báo", "Số tiền nạp phải lớn hơn 0!", "");
                return;
            }
            userService.topUpBalance(user, amount);

            user.setBalance(user.getBalance().add(amount));
            UserSession.getInstance().setCurrentUser(user);
            balanceLabel.setText(formatBalance(user.getBalance()));
            topUpAmountField.clear();
            AlertBox.createAlert("INFORMATION", "Thành công", "Nạp tiền thành công!", "");
        } catch (NumberFormatException e) {
            AlertBox.createAlert("ERROR", "Lỗi", "Vui lòng chỉ nhập số hợp lệ!", "");
        }
    }

    @FXML
    private void handleBackToLobby(ActionEvent event) {
        SceneManager.switchScene("ItemListing.fxml", "Trang chủ");
    }

    private String formatBalance(BigDecimal balance) {
        if (balance == null) return "0 ₫";
        return String.format("%,.0f ₫", balance);
    }
}
