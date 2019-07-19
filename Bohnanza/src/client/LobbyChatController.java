package client;

import java.io.IOException;
import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import server.ILobby;

public class LobbyChatController extends GridPane implements SetterInterface {

	@FXML
	private GridPane chat;

	@FXML
	private TextArea messages;

	@FXML
	private TextField inputField;

	private Client client;
	private ILobby lobby;

	/**
	 * Der Konstruktor der Klasse LobbyChatController
	 * startet Chat.fxml
	 */
	public LobbyChatController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Chat.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 *
	 * Wird aufgerufen sobald Enter gedrückt wurde während the Curser sich innerhalb
	 * des Textfeldes befindet
	 *
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void sendOnEnter(ActionEvent event) throws RemoteException {
		sendMessageToServer();
	}

	/**
	 * Wird aufgerufen sobald der "Send" Button gedrückt wurde
	 *
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void sendMessageButtonClicked(ActionEvent event) throws RemoteException {
		sendMessageToServer();
	}

	/**
	 * Die Methode sendet eine Nachricht zum Server
	 *
	 * @throws RemoteException
	 */
	private void sendMessageToServer() throws RemoteException {
		String text = inputField.getText();
		inputField.setText(null);
		if (text == null) {
			return;
		}
		lobby.sendMessage(text, client.getUsername(), "client");
	}

	@Override
	public void setClient(Client client) {
		this.client = client;
		lobby = client.getLobby();
	}

	/*
	 * Die Methode schreibt einen Text in ein TextArea
	 *
	 * @param Nachricht als String
	 */
	public void setMessages(String str) {
		messages.setText(str);
	}
}
