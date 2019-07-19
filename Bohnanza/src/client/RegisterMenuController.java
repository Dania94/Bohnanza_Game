package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import client.messageboxes.MessageBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.ILobby;

/**
 *
 * Die Controller-Klasse des Fensters "Register Menu".
 *
 */
public class RegisterMenuController implements SetterInterface {

	@FXML
	private Button backButton;

	@FXML
	private Button accountButton;

	@FXML
	private TextField username;

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField serverIP;

	private Updater updater;
	private Client client;

	/**
	 * Der Konstruktor der Klasse RegisterMenuController
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public RegisterMenuController() throws RemoteException {

	}

	/**
	 * Wenn der Nutzer den Button 2Registrieren" klickt, werden seine eingegebenen
	 * Daten in der Datenbank gespeichert und der Nutzer in die Lobby bewegt.
	 *
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void registerButtonClicked() throws RemoteException {
		String name = username.getText();
		String password = passwordField.getText();
		String serverAddress = serverIP.getText();

		if (name.length() > 15 || password.length() > 15) {
			MessageBox.showMessageBox("Benutzername und Passwort duerfen maximal 14 Zeichen enthalten!");
			return;
		}

		if (name.length() <= 2 || password.length() <= 6) {
			MessageBox.showMessageBox("Benutzername muss mind. 3 Zeichen, Passwort mind. 7 Zeichen enthalten!");
			return;
		}

		if (!client.isConnected()) {
			client.connectToServer(serverAddress);
		}

		ILobby lobby = client.getLobby();

		if (lobby.isUserInDatabase(name)) {
			MessageBox.showMessageBox("Benutzername existiert bereits!");
			return;
		}

		if (!lobby.createAccount(name, password)) {
			MessageBox.showMessageBox("Interner Fehler beim Zugriff auf die Datenbank.");
			return;
		}

		client.setUsername(name);
		if (checkUser()) {
			lobby.login(client.getUsername());
			updater.guiChange(accountButton, Updater.LOBBY);
		} else {
			MessageBox.showMessageBox("Der angegebene Benutzer ist bereits eingeloggt!");
		}
	}

	/**
	 * Die Methode ‹berpr‹ft, ob der angegebene Nutzer bereits in der Datenbank
	 * vorhanden ist.
	 *
	 * @return
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private boolean checkUser() throws RemoteException {
		ArrayList<String> userNames = client.getLobby().getAllUserNames();
		for (String name : userNames) {
			System.out.println(name);
			System.out.println(client.getUsername());
			if (name.equals(client.getUsername())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Wenn der Nutzer den Button "Zur‹ck" klickt, wird er zur‹ck zum Hauptmenu
	 * bewegt und der Registrierungsvorgang abgebrochen.
	 */
	@FXML
	public void backButtonClicked() {
		updater.guiChange(accountButton, Updater.MAIN_MENU);
	}

	@Override
	public void setClient(Client client) {
		this.client = client;
		this.updater = client.getUpdater();
	}

}
