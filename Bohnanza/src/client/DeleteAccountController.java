package client;

import datenbank.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

/**
 *
 * Diese Klasse ist die Controller-Klasse des Fensters "Account loeschen".
 *
 */
public class DeleteAccountController implements SetterInterface {

	@FXML
	private Button deleteButton;

	@FXML
	private Button cancelButton;

	@FXML
	private PasswordField password;

	private Account account = new Account();
	private Updater updater;
	private Client client;

	/**
	 * Diese Methode wird aufgerufen, wenn der Button "Abbrechen" geklickt wurde.
	 * Der Vorgang "Passwort aendern" wird abgebrochen und der Nutzer in die Lobby
	 * zurueck bewegt
	 */
	@FXML
	public void cancelButtonClicked() {
		updater.guiChange(cancelButton, Updater.LOBBY);
	}

	/**
	 * Wenn der Button "loeschen" geklickt wird, ueberprueft das Spiel das Passwort
	 * des Nutzers und loescht den Account.
	 * 
	 */
	@FXML
	public void deleteButtonClicked() {
		String pass = password.getText();
		if (account.getUserPswd(client.getUsername()).equals(pass)) {
			account.deleteUser(client.getUsername());
			updater.guiChange(deleteButton, Updater.MAIN_MENU);
		} else {
			System.out.println("Wrong password");
			updater.guiChange(deleteButton, Updater.WRONGPASSWORD_DELETE_ACCOUNT);
		}
	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
		this.client = client;
	}

}
