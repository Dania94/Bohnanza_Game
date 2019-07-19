package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import datenbank.Account;
import datenbank.Account_score;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import server.GameRoomState;
import server.IGameRoom;
import server.ILobby;

/**
 *
 * Die Controller-Klasse des Fensters "Schow Ranking".
 *
 *
 */
public class ShowRankingController implements SetterInterface {

	@FXML
	private Button backButton;
	@FXML
	private TextArea highestScores;
	@FXML
	private Button bestPlayers;

	private Updater updater;
	private Account accounts = new Account();
	private ILobby lobby;
	private IGameRoom gameroom;

	/**
	 * Ruft die Methode zum Anzeigen der Liste der besten Spieler auf
	 */
	@FXML
	private void initialize() {
		getTheBestPlayers();
	}

	/**
	 * Zeigt eine Liste der besten Spieler an.
	 */
	private void getTheBestPlayers() {
		ArrayList<Account_score> highScoresList = accounts.bestenListe();


		int maxNameLength = findMaxNameLength(highScoresList);

		String username = "Benutzername";
		if(username.length()>maxNameLength) {
			maxNameLength = username.length();
		}

		// Min. Distanz zwischen Name und Punktezahl.
		maxNameLength += 2;

		StringBuilder sb = new StringBuilder();

		addLine(sb, username, "Punktezahl", maxNameLength);

		for (Account_score a : highScoresList) {
			addLine(sb, a.getUsename(), ""+a.getScore(), maxNameLength);
		}

		highestScores.setText(sb.toString());
	}

	/**
	 * Methode formatiert Ausgabe der besten Spieler
	 *
	 * @param sb StringBuilder
	 * @param user der Spielername als String
	 * @param score erreichte Punktezahl eines Spielers als String
	 * @param maxNameLength Spielername mit größer Anzahl Zeichen als int
	 */
	private void addLine(StringBuilder sb, String user, String score, int maxNameLength) {
		sb.append(user);
		int numSpaces = maxNameLength - user.length();
		for(int i=0;i<numSpaces;i++) {
			sb.append(' ');
		}
		sb.append(score);
		sb.append('\n');
	}

	/**
	 * Die Methode sucht in der Highscoreliste den Benutzernamen mit größer Länge
	 *
	 * @param highScoresList
	 * @return Länge des Benutzernamens als Integer
	 */
	private int findMaxNameLength(ArrayList<Account_score> highScoresList) {
		int maxLength = 0;
		for (Account_score a : highScoresList) {
			int length = a.getUsename().length();
			if(length>maxLength) {
				maxLength = length;
			}
		}
		return maxLength;
	}

	/**
	 * Wenn der Nutzer den Button "Zurueck" klickt, wird das Fenster geschlossen und
	 * die das vorherige Fenster wieder angezeigt.
	 * @throws RemoteException RMI Exception
	 */
	public void backButtonClicked() throws RemoteException {
		if(gameroom==null) {
			updater.guiChange(backButton, Updater.LOBBY);
			return;
		}

		if (gameroom.getGameRoomState() == GameRoomState.PLAYING) {
			updater.guiChange(backButton, Updater.PLAY_AREA);
		} else if (gameroom.getGameRoomState() == GameRoomState.PREPARING) {
			updater.guiChange(backButton, Updater.CREATE_ROOM);
		} else {
			updater.guiChange(backButton, Updater.LOBBY);
		}

	}

	@Override
	public void setClient(Client client) {
		this.updater = client.getUpdater();
		this.lobby = client.getLobby();
		try {
			this.gameroom = lobby.getGameRoom(client.getGameRoom());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

}
