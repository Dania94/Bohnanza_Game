package client.messageboxes;

import client.Client;
import client.SetterInterface;
import client.Updater;
/**
 * the controller class of the fxml Wrongpassworddeleteaccount
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WrongPasswordDeleteAccountController implements SetterInterface {

	@FXML
	private Button okButton;

	private Updater updater;

	/**
	 * Wenn der OK-Button gedrückt wird gelangt man zum Main Menu
	 */
	@FXML
	public void okButtonClicked() {
		updater.guiChange(okButton, Updater.DELETE_ACCOUNT);
	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
	}

}
