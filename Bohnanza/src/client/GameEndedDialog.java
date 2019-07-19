package client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class GameEndedDialog extends Dialog<ButtonType> {

	public GameEndedDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameEnded.fxml"));
		try {
			fxmlLoader.setController(this);
			Node node = (Node) fxmlLoader.load();

			getDialogPane().setContent(node);

		} catch (IOException e) {
			e.printStackTrace();
		}
		setResizable(true);
	}

	@FXML
	private Label reasonLabel;

	@FXML
	private TextArea punkteArea;

	@FXML
	void initialize() {
		getDialogPane().getButtonTypes().add(new ButtonType("Okay", ButtonData.APPLY));
	}

	public static boolean isSuccess(Optional<ButtonType> optional) {
		if (!optional.isPresent()) {
			return false;
		}

		if (optional.get().getButtonData().equals(ButtonData.APPLY)) {
			return true;
		}

		return false;
	}

	public void setPunkte(HashMap<String, Integer> punkte) {
		int maxNameLength = findMaxNameLength(punkte.keySet());

		int minScoreDistance = 2 + maxNameLength;

		StringBuilder sb = new StringBuilder();

		for (String name : punkte.keySet()) {
			String punkteStr = String.valueOf(punkte.get(name));
			addLine(sb, name, punkteStr, minScoreDistance);
		}

		punkteArea.setText(sb.toString());
	}

	/**
	 * Methode formatiert Ausgabe der besten Spieler
	 *
	 * @param sb
	 *            StringBuilder
	 * @param user
	 *            der Spielername als String
	 * @param score
	 *            erreichte Punktezahl eines Spielers als String
	 * @param maxNameLength
	 *            Spielername mit größer Anzahl Zeichen als int
	 */
	private void addLine(StringBuilder sb, String user, String score, int maxNameLength) {
		sb.append(user);
		int numSpaces = maxNameLength - user.length();
		for (int i = 0; i < numSpaces; i++) {
			sb.append(' ');
		}
		sb.append(score);
		sb.append('\n');
	}

	/**
	 * Die Methode sucht im Set der Namen den Benutzernamen mit grÃ¶ÃŸer LÃ¤nge
	 *
	 * @param highScoresList
	 * @return Länge des Benutzernamens als Integer
	 */
	private int findMaxNameLength(Set<String> names) {
		int maxLength = 0;
		for (String name : names) {
			int length = name.length();
			if (length > maxLength) {
				maxLength = length;
			}
		}
		return maxLength;
	}

	public void setGameEndedReason(String reason) {
		reasonLabel.setText(reason);
	}
}
