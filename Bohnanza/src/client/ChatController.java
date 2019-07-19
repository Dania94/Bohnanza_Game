package client;

import java.io.IOException;
import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import server.IGameRoom;

/**
 * Das ist die Klasse Controller-Klasse des Fensters Chat.fxml
 *
 */
public class ChatController extends GridPane implements SetterInterface {

	@FXML
	private GridPane chat;

	@FXML
	private TextArea messages;

	@FXML
	private TextField inputField;

	private Client client;
	private IGameRoom gameroom;

	/**
	 * Das ist der Konstruktor der Klasse ChatCOntroller
	 */
	public ChatController() {
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
	 * Wird aufgerufen sobald Enter gedrückt wurde während der Cursor sich innerhalb
	 * des Textelds befindet
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
	 * Diese Methode sendet Nachrichten an den Server
	 *
	 * @throws RemoteException
	 */
	private void sendMessageToServer() throws RemoteException {
		String text = inputField.getText();
		inputField.setText(null);
		if (text == null) {
			return;
		}
		gameroom.sendMessage(text, client.getUsername(), "roomChat");
	}

	@Override
	public void setClient(Client client) {
		this.client = client;
		try {
			this.gameroom = client.getLobby().getGameRoom(client.getGameRoom());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Diese Methode hängt eine Nachricht unten im Chatfenster an.
	 *
	 * @param str
	 *            Nachricht bzw. Text als String
	 */
	public void setMessages(String str) {
		messages.setText(str);
	}
}
