
package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 *
 * Diese Klasse ist die Controller-Klasse des Fensters "Account Settings".
 *
 *
 */
public class AccountSettingsController implements SetterInterface {

	@FXML
	private Button changePasswordButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button accountCancelButton;

	private Updater updater;

	/**
	 * Diese Methode wird aufgerufen, wenn der Button "Passwort aendern" geklickt
	 * wurde. Der Dialog "Passwort aendern" wird angezeigt.
	 *
	 * @param event
	 *
	 */
	@FXML
	void changePasswordButtonClicked(ActionEvent event) {
		updater.guiChange(changePasswordButton, Updater.CHANGE_PASSWORD);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Button "Account loeschen" geklickt
	 * wurde. Der Dialog "Account loeschen" wird angezeigt.
	 *
	 * @param event
	 *
	 */
	@FXML
	void deleteButtonClicked(ActionEvent event) {
		updater.guiChange(deleteButton, Updater.DELETE_ACCOUNT);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Button "Abbrechen" geklickt wurde.
	 * Der Vorgang wird abgebrochen und der Nutzer zurueck in die Lobby bewegt.
	 *
	 * @param event
	 *
	 */
	@FXML
	void cancelButtonClicked(ActionEvent event) {
		updater.guiChange(accountCancelButton, Updater.LOBBY);
	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
	}
}