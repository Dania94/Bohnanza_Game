package client.messageboxes;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class MessageBox {
	public static void showMessageBox(String text) {
		Platform.runLater(new Runnable() {
			public void run() {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Bohnanza");
				alert.setHeaderText(text);
				// alert.setContentText("Additional information");
				alert.showAndWait();
			}
		});
	}
}
