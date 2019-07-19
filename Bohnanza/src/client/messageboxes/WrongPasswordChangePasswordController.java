package client.messageboxes;

import client.Client;
import client.SetterInterface;
import client.Updater;
/**
 * the controller class of the fxml Wrongpasswordchangepassword
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WrongPasswordChangePasswordController implements SetterInterface {

	@FXML
	private Button okButton;

	private Updater updater;

	/**
	 * Wenn der OK-Button gedrückt wird gelangt man zum Main Menu
	 */
	@FXML
	public void okButtonClicked() {
		updater.guiChange(okButton, Updater.CHANGE_PASSWORD);
	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
	}

}
