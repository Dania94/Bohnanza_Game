package client;

import client.messageboxes.MessageBox;
import datenbank.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

/**
 *
 * Diese Klasse ist die Controller-Klasse des Fensters "Passwort aendern".
 *
 */
public class ChangePasswordController implements SetterInterface {

	@FXML
	private Button changePasswordButton;

	@FXML
	private Button cancelButton;

	@FXML
	private PasswordField confirmPassword;

	@FXML
	private PasswordField currentPassword;

	@FXML
	private PasswordField newPassword;

	private Account account = new Account();
	private Updater updater;
	private Client client;

	/**
	 * 
	 * Diese Methode wird aufgerufen, wenn der Button "Passwort aendern" geklickt
	 * wurde. Der Spieler erhaelt die Moeglichkeit, ein neues Passwort einzugeben
	 * und durch sein altes Passwort zu bestaetigen.
	 * 
	 */
	@FXML
	public void changePasswordButtonClicked() {
		String newPswd = newPassword.getText();
		String currentPswd = currentPassword.getText();
		String confirmPswd = confirmPassword.getText();
		if (!account.getUserPswd(client.getUsername()).equals(currentPswd)) {
			System.out.println("Wrong old password!");
			updater.guiChange(changePasswordButton, Updater.WRONGPASSWORD_CHANGE_PASSWORD);
		} else if (newPswd.length() < 7) {
			MessageBox.showMessageBox("Passwort muss mind. 7 Zeichen lang sein.");
		} else if (!newPswd.equals(confirmPswd)) {
			System.out.println("New Passwort and confirmed Passwort different!");
			updater.guiChange(changePasswordButton, Updater.WRONGPASSWORD_CHANGE_PASSWORD);
		} else {
			account.updateUserPswd(client.getUsername(), newPswd);
			updater.guiChange(changePasswordButton, Updater.LOBBY);
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Button "Abbrechen" geklickt wurde.
	 * Der Vorgang "Passwort aendern" wird abgebrochen und der Nutzer in die Lobby
	 * zurueck bewegt.
	 */
	@FXML
	public void cancelButtonClicked() {
		updater.guiChange(cancelButton, Updater.LOBBY);
	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
		this.client = client;
	}
}
