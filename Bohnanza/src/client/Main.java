package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 *
 * Die Mainklasse des Spiels.
 *
 */
public class Main extends Application {

	private Client client = new Client();

	public static void main(String[] args) {
		Platform.setImplicitExit(true);
		launch(args);
	}

	/**
	 * Öffnet das Mainmenu, wenn das Spiel startet.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		client.getUpdater().setStage(primaryStage);

		client.getUpdater().guiChange(Updater.MAIN_MENU);
	}

	/**
	 * Beendet den Client, wenn das Spiel beendet wird.
	 */
	@Override
	public void stop() {
		System.out.println("Stage is closing");
		System.exit(0);
	}
}
