package dto.util;

import javafx.scene.control.Alert;

public class AlertBox {
  public static void createAlert(String type, String title, String header, String context) {
    switch (type) {
      case "WARNING": {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
        break;
      }
      case "INFORMATION": {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
        break;
      }
      case "ERROR": {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.show();
        break;
      }
    }
  }
}
