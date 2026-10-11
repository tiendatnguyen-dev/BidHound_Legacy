package ui;

import com.entities.User;
import com.util.AlertBox;
import com.util.HttpUtils;
import com.util.JsonConverter;
import com.util.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.http.HttpResponse;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label statusLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        if (username.trim().isEmpty() || email.trim().isEmpty()) {
            AlertBox.createAlert("WARNING","Thông báo","Vui lòng không bỏ trống các ô!","");
            return;
        }

        User userDto = new User();
        userDto.setUsername(username);
        userDto.setEmail(email);
        try {
            String userJson = JsonConverter.toJson(userDto);
            HttpResponse<String> httpResponse = HttpUtils.sendPost("http://localhost:8080/api/auth/register", userJson);
            if (httpResponse.statusCode() == 201) {
                AlertBox.createAlert("INFORMATION", "Thành công", "Đăng ký thành công!", "");
                SceneManager.switchScene("Login.fxml", "Đăng nhập");
            } else {
                AlertBox.createAlert("ERROR", "Lỗi", "Tài khoản đã tồn tại!", "");
            }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        SceneManager.switchScene("Login.fxml","Đăng nhập");
    }
}
