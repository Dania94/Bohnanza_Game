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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import server.ILobby;
import server.ILobbyListener;

/**
 *
 * Die Controller-Klasse des Fensters "Lobby".
 *
 */
public class LobbyController extends UnicastRemoteObject implements SetterInterface, ILobbyListener {

	private static final long serialVersionUID = 1L;

	@FXML
	private Button createRoomButton;

	@FXML
	private Button instructionsButton;

	@FXML
	private Button accountButton;

	@FXML
	private Button rankingButton;

	@FXML
	private Button logoutButton;

	@FXML
	private Button sendMessageButton;

	@FXML
	private TextField message = new TextField();

	@FXML
	private TextArea userList = new TextArea();

	@FXML
	private TextArea chatWindow;

	@FXML
	private VBox chatPlaceholder;

	@FXML
	private ListView<String> roomsView;

	private ILobby lobby;
	private Updater updater;
	private Client client;

	private LobbyChatController chat;

	/**
	 * initialisieren die FXML-Datei Lobby.fxml
	 * 
	 * @throws RemoteException
	 */
	@FXML
	void initialize() {
		chat = new LobbyChatController();
		chatPlaceholder.getChildren().add(chat);
	}

	/**
	 * Der Konstruktor der Klasse LobbyController.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 * 
	 * @throws NotBoundException
	 *             RMI Exception
	 */
	public LobbyController() throws RemoteException, NotBoundException {
	}

	/**
	 * Zeigt alle anwesenden Spieler an.
	 *
	 * @throws RemoteException
	 */
	private void displayUserList() throws RemoteException {
		userList.setText("");
		ArrayList<String> userNames = lobby.getAllUserNames();
		if (userNames.size() > 0) {
			for (String name : userNames) {
				userList.appendText(" " + name + "\n");
			}
		}
	}

	/**
	 * Zeigt alle geöffneten Spielraeume an.
	 *
	 * @throws RemoteException
	 */
	private void displayRoomsList() throws RemoteException {
		roomsView.getItems().clear();
		ArrayList<String> rooms = lobby.getAllRoomNames();
		for (String room : rooms) {
			roomsView.getItems().add(room);
		}
	}

	/**
	 * Zeigt fortlaufend alle im Chat gesendeten Nachrichten.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void displayChatList() throws RemoteException {
		String text = lobby.getAllMessages(client.getUsername());
		chat.setMessages(text);
		// chatWindow.setText(text);
	}

	/**
	 * Wenn der Button "Spielraum erstellen" geklickt wird, oeffnet sich die Staging
	 * Area und der Spieler wird dorthin bewegt.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void createRoomButtonClicked(ActionEvent event) throws RemoteException {
		String roomName = lobby.createRoom(client.getUsername());
		if (roomName != null) {
			client.setGameRoom(roomName);
			updater.guiChange(createRoomButton, Updater.CREATE_ROOM);
		} else {
			MessageBox.showMessageBox("Du hast bereits einen Raum erstellt.");
		}
	}

	/**
	 * Erkennt, welcher Raum in der Liste doppelt geklickt wurde und bewegt den
	 * Spieler in den ausgewaehlten Raum.
	 *
	 * @param click
	 *            ein erkannter Mausklick
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	@FXML
	public void handle(MouseEvent click) throws RemoteException {
		if (click.getClickCount() == 2) {
			// Use ListView's getSelected Item
			String currentItemSelected = roomsView.getSelectionModel().getSelectedItem();
			System.out.println("selected room" + currentItemSelected);
			client.setGameRoom(currentItemSelected);
			boolean joined = lobby.joinRoom(client.getUsername(), currentItemSelected);
			if (joined) {
				updater.guiChange(Updater.CREATE_ROOM);
			} else {
				MessageBox.showMessageBox("Der Spielraum ist voll.");
			}
		}
	}

	/**
	 *
	 * Wenn der Button "Anleitung" geklickt wird, oeffnet sich die Spielanleitung.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void instructionsButtonClicked(ActionEvent event) throws IOException {
		// text file, should be opening in default text editor
		File file = new File("instruction_1.png");
		File file1 = new File("instruction_2.png");
		// first check if Desktop is supported by Platform or not
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
	 * Wenn der Button "Bestenliste" geklickt wird, zeigt das Spiel die aktuelle
	 * Bestenliste an.
	 * 
	 * @param event
	 *
	 */
	@FXML
	void rankingButtonClicked(ActionEvent event) {
		System.out.println("Ranking");
		updater.guiChange(accountButton, Updater.SHOW_RANKING);
	}

	/**
	 * Wenn der Spieler den Button "Account" klickt, oefnnet sich das Fenster
	 * "Account Settings".
	 *
	 */
	@FXML
	void accountButtonClicked(ActionEvent event) {
		updater.guiChange(accountButton, Updater.ACCOUNT_SETTINGS);
	}

	/***
	 *
	 * Wenn der Spieler den Button "Logout" klickt, wird er vom Spiel abgemeldet und
	 * verlaesst die Lobby. Der Anmelde-Dialog wird angezeigt.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void logoutButtonClicked(ActionEvent event) throws RemoteException {
		System.out.println("Logout");
		updater.guiChange(logoutButton, Updater.LOGOUT);
	}

	/**
	 * Wenn der Spieler den Button "Senden" klickt, wird seine eingegebene
	 * Chat-Nachricht an den Server geschickt und dann im Chat angezeigt.
	 * 
	 * @param event
	 *
	 */
	@FXML
	void sendMessageButtonClicked(ActionEvent event) {
		String inputMessage = message.getText();
		message.setText("");
		String messageType = "client";
		try {
			lobby.sendMessage(inputMessage, client.getUsername(), messageType);
		} catch (RemoteException e) {
			Logger.getLogger(CreateRoomController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Die Methode teilt allen wartenden Listenern mit, dass es eine Aenderung in
	 * der Lobby gab.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	@Override
	public void notifyLobbyChange() throws RemoteException {
		try {
			displayUserList();
			displayRoomsList();
			displayChatList();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setClient(Client client) {
		this.lobby = client.getLobby();
		this.client = client;
		chat.setClient(client);
		try {
			lobby.setLobbyListener(this);
			notifyLobbyChange();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.updater = client.getUpdater();
	}

}
