package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

	public static void ShowAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Ȯ�� �ʿ�");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static void ShowAlert(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Ȯ�� �ʿ�");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
