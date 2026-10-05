package ui;

import com.entities.User;
import com.util.AlertBox;
import com.util.HttpUtils;
import com.util.JsonConverter;
import com.util.SceneManager;
import com.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class LoginController {
    @FXML private TextField userIdField;
    @FXML private TextField usernameField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        User userDto = new User();
        String userId = userIdField.getText();
        String username = usernameField.getText();
        if (userId.trim().isEmpty() || username.trim().isEmpty()) {
            AlertBox.createAlert("WARNING","Thông báo","Vui lòng không bỏ trống các ô!","");
            return;
        }

        Long userIdDto;
        try {
            userIdDto = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            AlertBox.createAlert("WARNING","Thông báo","Vui lòng nhập số!","");
            return;
        }
        userDto.setId(userIdDto);
        userDto.setUsername(username);
        String jsonBody = JsonConverter.toJson(userDto);

        try {
            HttpResponse<String> response = HttpUtils.sendPost("http://localhost:8080/api/auth/login",jsonBody);

            if (response.statusCode() == 200) {
                String userJson = new String(response.body().getBytes(), StandardCharsets.UTF_8);
                User currentUser = JsonConverter.fromJson(userJson,User.class);
                UserSession.getInstance().setCurrentUser(currentUser);
                SceneManager.switchScene("ItemListing.fxml","Danh sách đấu giá");
            } else {
                AlertBox.createAlert("ERROR","Lỗi","Id hoặc tên đăng nhập sai!","");
            }
        } catch (Exception e) {
            AlertBox.createAlert("ERROR", "Lỗi", "Không thể kết nối đến máy chủ!", "");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        SceneManager.switchScene("Register.fxml","Đăng ký");
    }
}
