package client;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * Die Klasse Updater ist für die Aktualisierung der Benutzeroberfläche
 * verantwortlich.
 * 
 *
 */
public class Updater {

	// Hauptfenster
	public static final String CREATE_ROOM = "CreateRoom.fxml";
	public static final String LOBBY = "Lobby.fxml";
	public static final String PLAY_AREA = "PlayArea.fxml";
	
	// Kleinere Fenster
	public static final String ACCOUNT_SETTINGS = "AccountSettings.fxml";
	public static final String CHANGE_PASSWORD = "ChangePassword.fxml";
	public static final String DELETE_ACCOUNT = "DeleteAccount.fxml";
	public static final String LOGOUT = "Logout.fxml";
	public static final String MAIN_MENU = "MainMenu.fxml";	
	public static final String REGISTER_MENU = "RegisterMenu.fxml";
	public static final String SHOW_RANKING = "ShowRanking.fxml";
	
	// Spezielle Messageboxes
	public static final String WRONGPASSWORD_CHANGE_PASSWORD = "messageboxes/WrongPasswordChangePassword.fxml";
	public static final String WRONGPASSWORD_DELETE_ACCOUNT = "messageboxes/WrongPasswordDeleteAccount.fxml";
	
	
	private ArrayList<String> fullscreen = new ArrayList<String>();
	
	private Client client;
	private Stage stage;

	public Updater(Client client) {
		this.client = client;
		
		fullscreen.add(LOBBY);
		fullscreen.add(CREATE_ROOM);
		fullscreen.add(PLAY_AREA);
	}

	/**
	 * Das  übergebene Fenster wird angezeigt und das alte geschlossen.
	 *
	 * @param button
	 *            der Button, der im alten Fenster geklickt wurde.
	 * @param fxmlName
	 *            das neue Fenster, das als nächstes gefnet werden soll.
	 *
	 */

	public void guiChange(Button button, String fxmlName) {
		guiChange(fxmlName);
	}

	/**
	 * Das �bergebene Fenster wird geöffnet und das bisherige geschlossen.
	 * 
	 * @param fxmlname
	 *            das ü�bergebene Fenster, das als nächstes geöffnet werden soll.
	 * 
	 */
	public void guiChange(String fxmlname) {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlname));
		try {
			Parent root = (Parent) fxmlLoader.load();
			if (fxmlLoader.getController() instanceof SetterInterface) {
				SetterInterface controller = (SetterInterface) fxmlLoader.getController();
				controller.setClient(client);
			}
			Scene scene = new Scene(root);
			stage.setTitle("Bohnanza");
			stage.setScene(scene);
			stage.show();
			if(fullscreen.contains(fxmlname)) {
				stage.setMaximized(false);
				stage.setMaximized(true);
			} else {
				stage.setMaximized(false);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStage(Stage primaryStage) {
		this.stage = primaryStage;
	}
}
