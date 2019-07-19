package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import client.messageboxes.MessageBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import server.ILobby;

/**
 *
 * Die Controller-Klasse des Fensters "Main Menu".
 *
 */

public class MainMenuController extends UnicastRemoteObject implements SetterInterface {

	private static final long serialVersionUID = 1L;

	@FXML
	private Button loginButton;

	@FXML
	private Button registerButton;

	@FXML
	private PasswordField password;

	@FXML
	private TextField username;

	@FXML
	private TextField ip;

	private Updater updater;
	private Client client;

	/**
	 * Konstruktor der Klasse MainMenuController.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public MainMenuController() throws RemoteException {

	}

	/**
	 * Die Methode ueberprueft, ob der Nutzer bereits angemeldet ist.
	 *
	 * @return true falls der Nutzer noch nicht angemeldet ist.
	 * @throws RemoteException
	 */

	private boolean isUserAlreadyLoggedIn() throws RemoteException {
		ArrayList<String> userNames = client.getLobby().getAllUserNames();
		System.out.println("Looking for user: " + client.getUsername());
		for (String name : userNames) {
			System.out.println(name);
			if (name.equals(client.getUsername())) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * Wenn der Button "Login" geklickt wird, wird der Nutzer in seinen Account
	 * eingeloggt und in die Lobby bewegt.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void loginButtonClicked(ActionEvent event) throws RemoteException {
		String user_name = username.getText();
		String pass = password.getText();
		String serverIP = ip.getText();

		// set for testing. Remove later.
		// user_name = "testuser";
		// pass = "testing";
		// serverIP = "localhost";

		if (user_name.length() == 0 || pass.length() == 0) {
			MessageBox.showMessageBox("Kein Benutzername/Passwort eingegeben!");
			return;
		}

		if (!client.isConnected()) {
			client.connectToServer(serverIP);
		}

		ILobby lobby = client.getLobby();

		if (!lobby.isUserInDatabase(user_name)) {
			MessageBox.showMessageBox("Benutzername nicht in der Datenbank gefunden!");
			return;
		}

		if (!lobby.checkPasswordForUserInDatabase(user_name, pass)) {
			MessageBox.showMessageBox("Falsches Passwort angegeben!");
			return;
		}

		client.setUsername(user_name);

		if (!isUserAlreadyLoggedIn()) {
			MessageBox.showMessageBox("Der angegebene Benutzer ist bereits eingeloggt!");
			return;
		}

		client.getLobby().login(client.getUsername());
		updater.guiChange(loginButton, Updater.LOBBY);
	}

	/**
	 * Wenn der Nutzer den Button "Registrieren" klickt, wird das Fenster
	 * "RegisterMenu" geoeffnet.
	 * 
	 * @param event
	 *
	 */
	@FXML
	void registerButtonClicked(ActionEvent event) {
		updater.guiChange(registerButton, Updater.REGISTER_MENU);
	}

	@Override
	public void setClient(Client client) {
		this.client = client;
		this.updater = client.getUpdater();
	}

}
