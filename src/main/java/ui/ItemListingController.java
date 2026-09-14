package ui;

import com.dto.entities.Item;
import com.dto.entities.User;
import com.dto.util.AlertBox;
import com.dto.util.SceneManager;
import com.dto.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import com.service.ItemService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ItemListingController {
    @FXML private Label userLabel;
    @FXML private Label balanceLabel;
    @FXML private FlowPane itemsFlowPane;

    @FXML private TextField newItemTitleField;
    @FXML private TextField newItemPriceField;
    @FXML private TextField newItemDurationField;

    ItemService itemService = new ItemService();

    @FXML
    public void initialize() {
        User user = UserSession.getInstance().getCurrentUser();
        if (user != null) {
            userLabel.setText(user.getUsername());
            balanceLabel.setText(user.getBalance().toString());
        }

        loadItems();
    }

    @FXML
    public void loadItems() {
        itemsFlowPane.getChildren().clear();

        itemsFlowPane.setHgap(16);
        itemsFlowPane.setVgap(16);

        List<Item> items = itemService.getList();

        if (items == null) return;

        for (Item item : items) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ItemCard.fxml"));
                Parent cardNode = loader.load();

                ItemCardController cardController = loader.getController();
                cardController.setData(item);

                itemsFlowPane.getChildren().add(cardNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCreateItem() {
        String newItemTitle = newItemTitleField.getText();
        String newItemPrice = newItemPriceField.getText();
        String newItemDuration = newItemDurationField.getText();
        if (newItemTitle.trim().isEmpty() || newItemPrice.trim().isEmpty() || newItemDuration.trim().isEmpty()) {
            AlertBox.createAlert("ERROR","Thông báo","Vui lòng không bỏ trống các ô","");
        } else {
            Item item = new Item();
            item.setTitle(newItemTitle);
            item.setCurrentPrice(BigDecimal.valueOf(Long.parseLong(newItemPrice)));
            item.setEndTime(LocalDateTime.now().plusSeconds(Integer.parseInt(newItemDuration)));
            itemService.createItem(item);
            AlertBox.createAlert("INFORMATION","Thông báo","Tạo sản phẩm mới thành công!","");
            loadItems();
        }

    }

    @FXML
    private void handleOpenProfile(ActionEvent event) {
        SceneManager.switchScene("Profile.fxml","Trang cá nhân");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().cleanUserSession();
        SceneManager.switchScene("Login.fxml","Đăng nhập");
    }
}
