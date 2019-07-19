
package client;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.messageboxes.MessageBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import server.GameRoomState;
import server.IGameRoom;
import server.IGameRoomListener;
import server.ILobby;

public class CreateRoomController extends UnicastRemoteObject implements SetterInterface, IGameRoomListener {

	private static final long serialVersionUID = 1L;

	@FXML
	private VBox chatPlaceholder;

	@FXML
	private Button start;

	@FXML
	private Button cancel;

	@FXML
	private Button deleteEasy;

	@FXML
	private Button deleteHard;

	@FXML
	private TextArea onlinePlayer;

	@FXML
	private Button easyBot;

	@FXML
	private Button hardBot;

	private Updater updater;
	private IGameRoom gameroom;
	private ILobby lobby;
	private Client client;

	private ChatController chat;

	@FXML
	void initialize() {
		chat = new ChatController();
		chatPlaceholder.getChildren().add(chat);
	}

	/**
	 * Der Konstruktor der Klasse CreateRoomController. Der Ersteller wird zu den
	 * Spielern hinzugefügt
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 * 
	 * @throws NotBoundException
	 *             RMI Exception
	 */
	public CreateRoomController() throws RemoteException, NotBoundException {
	}

	/**
	 * Startet das Spiel wenn die Anzahl der Spieler größer als 3 ist
	 *
	 * @throws RemoteException
	 * @throws NotBoundException
	 *
	 */
	@FXML
	void startButtonClicked(ActionEvent event) throws RemoteException, NotBoundException {		
		boolean success = gameroom.startGame(client.getUsername());
		if (!success) {
			MessageBox.showMessageBox("Du brauchst mind. 3 Spieler.");
		}
		// Wechsel zu PlayArea wird durch notifyGameRoomChange behandelt.
	}

	/**
	 * Der Cancel-Button wird gedrückt und man kommt zurück zur Lobby und der
	 * Spielraum wird gelöscht
	 *
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void cancelButtonClicked(ActionEvent event) throws RemoteException {
		gameroom.exitGame(client.getUsername());
		lobby.deleteRoom(client.getUsername());
		updater.guiChange(cancel, Updater.LOBBY);
	}

	/**
	 * Einen EasyBot zum Spielraum hinzufügen
	 *
	 * @param event
	 * @throws RemoteException
	 *
	 */

	@FXML
	void easyBotButtonClicked(ActionEvent event) throws RemoteException {
		boolean easyBotSuccess = gameroom.addEasyBot();
		if (!easyBotSuccess) {
			MessageBox.showMessageBox("Der Spielraum ist voll.");
		}
	}

	/**
	 * Einen HardBot zum Spielraum hinzufügen
	 *
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void hardBotButtonClicked(ActionEvent event) throws RemoteException {
		boolean hardBotSuccess = gameroom.addHardBot();
		if (!hardBotSuccess) {
			MessageBox.showMessageBox("Der Spielraum ist voll.");
		}
	}

	/**
	 *
	 * Ein Dr�cken des Buttons löscht einen EasyBot
	 *
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void deleteEasyButtonClicked(ActionEvent event) throws RemoteException {
		boolean easyDeleted = gameroom.deleteEasyBot();
		if (!easyDeleted) {
			MessageBox.showMessageBox("Es gibt keine einfachen Bots.");
		}
	}

	/**
	 * Ein Dr�cken des Buttons löscht einen HardBot
	 *
	 * @param event
	 * @throws RemoteException
	 *
	 */
	@FXML
	void deleteHardButtonClicked(ActionEvent event) throws RemoteException {
		boolean hardDeleted = gameroom.deleteHardBot();
		if (!hardDeleted) {
			MessageBox.showMessageBox("Es gibt keine schweren Bots.");
		}
	}

	/**
	 * Zeige die ChatListe an
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void displayChatList() throws RemoteException {
		String messageString = gameroom.getRoomMessages(client.getUsername());
		chat.setMessages(messageString);
	}

	/**
	 * Zeige alle Benutzer im Spielraum an
	 *
	 * @throws RemoteException
	 */
	private void displayPlayerList() throws RemoteException {
		onlinePlayer.setText("");
		ArrayList<String> playerNames = gameroom.getPlayerNames();
		if (playerNames.size() > 0) {
			for (String name : playerNames) {
				onlinePlayer.appendText(" " + name + "\n");
			}
		}
	}

	// /**
	// * Sends the message to the server.
	// */
	// @FXML
	// void sendMessageButtonClicked(ActionEvent event) {
	// String inputMessage = message.getText();
	// message.setText("");
	// String messageType = "client";
	// try {
	// gameroom.sendMessage(inputMessage, client.getUsername(), messageType);
	// } catch (RemoteException e) {
	// Logger.getLogger(CreateRoomController.class.getName()).log(Level.SEVERE,
	// null, e);
	// }
	// }

	@Override
	public void notifyGameRoomChange() throws RemoteException {

		if (gameroom.getGameRoomState() == GameRoomState.PLAYING) {
			CreateRoomController listener = this;

			Platform.runLater(new Runnable() {
				public void run() {
					try {
						gameroom.removeGameRoomListener(listener);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					updater.guiChange(start, Updater.PLAY_AREA);
				}
			});
		}

		try {
			displayChatList();
			displayPlayerList();
		} catch (RemoteException ex) {
			Logger.getLogger(CreateRoomController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Setzt den aktiven Client, die Lobby, den Spielraum und den Updater.
	 * 
	 * @param client
	 *            der übergebene Client.
	 */
	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
		this.lobby = client.getLobby();
		this.client = client;
		chat.setClient(client);
		try {
			this.gameroom = lobby.getGameRoom(client.getGameRoom());
			gameroom.setGameRoomListener(this);
			notifyGameRoomChange();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ruft die Spielanleitung auf
	 *
	 * @param event
	 * @throws IOException 
	 *
	 */
	@FXML
	void instructionsButtonClicked(ActionEvent event) throws IOException {
		// Datei, die im Standard-Texteditor geöffnet wird
				File file = new File("instruction_1.png");
				File file1 = new File("instruction_2.png");
				// überprüfe, ob der verwendete Desktop unterstützt wird
				if (!Desktop.isDesktopSupported()) {
					System.out.println("Desktop is not supported");
					return;
				}
				Desktop desktop = Desktop.getDesktop();
				if (file.exists())
					desktop.open(file);
				if (file1.exists())
					desktop.open(file1);
	}

	/**
	 * Ruft die Bestenliste auf
	 *
	 * @param event
	 */
	@FXML
	void rankingButtonClicked(ActionEvent event) {
		updater.guiChange(Updater.SHOW_RANKING);
	}

}
