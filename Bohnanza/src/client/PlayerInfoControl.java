package client;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * Das ist die Controller-KLasse zum Fenster PlayerInfo.fxml
 *
 */
public class PlayerInfoControl extends GridPane {

	@FXML
	private GridPane PlayerInfo;

	@FXML
	private TextField Beanfield1;

	@FXML
	private TextField Beanfield2;

	@FXML
	private TextField Beanfield3;

	@FXML
	private TextField SpielerName;

	@FXML
	private TextField Taler;

	/**
	 * Der Konstruktor der Klasse PlayerInfoControl ruft PlayerInfo.fxml auf Dort
	 * enthalten sind der Name des Spielers, Anzahl der Taler des Spielers, und die
	 * Erntefelder des Spielers
	 */
	public PlayerInfoControl() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlayerInfo.fxml"));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Diese Methode ändert den Spielernamen
	 *
	 * @param string
	 *            Der neue Name wird als String übergeben
	 */
	public void setPlayerName(String string) {
		SpielerName.setText(string);
	}

	/**
	 * Diese Methode ändert die Anzahl der Taler eines Spielers
	 *
	 * @param string
	 *            Die neue Anzahl der Taler wird als String übergeben
	 */
	public void setTaler(String string) {
		Taler.setText(string);
	}

	/**
	 * Diese Methode ändert die angepflanzte Bohnensorte des Erntefelds 1
	 *
	 * @param string
	 *            Die neue Bohnensorte wird als String übergeben
	 */
	public void setBeanfield1(String string) {
		Beanfield1.setText(string);
	}

	/**
	 * Diese Methode ändert die angepflanzte Bohnensorte des Erntefelds 2
	 *
	 * @param string
	 *            Die neue Bohnensorte wird als String übergeben
	 */
	public void setBeanfield2(String string) {
		Beanfield2.setText(string);
	}

	/**
	 * Diese Methode ändert die angepflanzte Bohnensorte des Erntefelds 3
	 *
	 * @param string
	 *            Die neue Bohnensorte wird als String übergeben
	 */
	public void setBeanfield3(String string) {
		Beanfield3.setText(string);
	}
}
