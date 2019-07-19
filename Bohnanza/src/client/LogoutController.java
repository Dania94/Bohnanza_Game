package client;

import java.rmi.RemoteException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import server.ILobby;

/**
 *
 * Die Controller-Klasse des Fensters "Logout".
 *
 */
public class LogoutController implements SetterInterface {

	@FXML
	private Button yesButton;

	@FXML
	private Button noButton;

	private ILobby lobby;
	private Updater updater;
	private Client client;

	/**
	 * Wenn der Spieler den Button "Ja" klickt, wird er aus dem Spiel ausgeloggt und
	 * das AnmeldeFenster wird angezeigt.
	 *
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	@FXML
	public void yesButtonClicked() throws RemoteException {
		lobby.logout(client.getUsername());
		updater.guiChange(noButton, Updater.MAIN_MENU);
	}

	/**
	 * Wenn der Spieler den Button "Nein" klickt, wird err zurueck in die Lobby
	 * bewegt.
	 *
	 */
	@FXML
	public void noButtonClicked() {
		updater.guiChange(noButton, Updater.LOGOUT);
	}

	/**
	 * Konstruktor der Klasse LobbyController.
	 *
	 */
	public LogoutController() {
	}

	@Override
	public void setClient(Client client) {
		this.lobby = client.getLobby();
		this.updater = client.getUpdater();
		this.client = client;
	}
}
