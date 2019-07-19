package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Das LobbyInterface. Implementiert Methoden zur entfernten Kommunikation mit
 * Clients.
 *
 */
public interface ILobby extends Remote {
	// deklariere Methoden zum entfernten Aufruf.

	/**
	 * Der Spieler wird der Liste der Spieler hinzugefuegt. Eine Nachricht wird
	 * angezeigt, dass der Spieler die Lobby betreten hat.
	 *
	 * @param username
	 *            der Spieler, der die Lobby betritt.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void login(String username) throws RemoteException;

	/**
	 * Der Spieler verlaesst das Spiel und wird abgemeldet.
	 *
	 * @param unsername
	 *            der Spieler, der sich abmelden moechte.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void logout(String unsername) throws RemoteException;

	/**
	 * Ein Spieler sendet eine Nachricht im Chat.
	 *
	 * @param inputMessage
	 *            die Nachricht, die der Spieler geschickt hat.
	 * @param username
	 *            der Name des Spielers.
	 * @param type
	 *            der Nachricht.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void sendMessage(String inputMessage, String username, String type) throws RemoteException;

	/**
	 * Falls der Spieler, der versucht, den Raum zu loeschen der Ersteller ist, wird
	 * der Raum geloescht. Falls nicht, erhaelt der Spieler eine Fehlermeldung-
	 *
	 * @param owner
	 *            der Spieler, der den Raum loeschen moechte.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void deleteRoom(String owner) throws RemoteException;

	/**
	 * Gibt alle Chat-Nachrichten zurueck.
	 *
	 * @param username
	 *            der Spielername
	 * @return alle Chat-Nachrichten.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public String getAllMessages(String username) throws RemoteException;

	/**
	 * Fuegt einen Listener zur Liste der LobbyListener hinzu.
	 *
	 * @param lobbyListener
	 *            Ein Listener zur Liste der LobbyListener
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void setLobbyListener(ILobbyListener lobbyListener) throws RemoteException;

	/**
	 * Setzt den Spieler auf spielbereit.
	 *
	 * @param username
	 *            der Spielername.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void setPlayerReady(String username) throws RemoteException;

	/**
	 * Falls der Spieler, der versucht, einen Raum zu erstellen, noch keinen
	 * Spielraum erstellt hat, wird ein neuer Spielraum erstellt. Falls der Spieler
	 * bereits einen Spielraum erstellt hat, erhaelt er eine Fehlermeldung.
	 *
	 * @param username
	 *            der Spieler, der einen Raum erstellen moeschte.
	 * @return den Namen des Raums.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public String createRoom(String username) throws RemoteException;

	/**
	 * Gibt die Liste aller Raumnamen zurueck.
	 *
	 * @return die Liste aller Raumnamen
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public ArrayList<String> getAllRoomNames() throws RemoteException;

	/**
	 * Gibt die Liste aller Spielernamen zurueck.
	 *
	 * @return alle Spielernamen
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public ArrayList<String> getAllUserNames() throws RemoteException;

	/**
	 * Falls der gewaehlte Raum weniger als 5 Spieler enthaelt, betritt der Spieler
	 * den Raum. Falls nicht, wird eine Fehlermeldung angezeigt.
	 *
	 * @param username
	 *            der Spielername.
	 * @param roomName
	 *            der Raumname, den der Spieler eintretten moeschte.
	 * @return boolean, falls der Spieler den Raum eintritt.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public boolean joinRoom(String username, String roomName) throws RemoteException;

	/**
	 * Gibt den genannten Raum zurueck, falls dieser existiert.
	 *
	 * @param roomName
	 *            der Raumname.
	 * @return IGameRoom der genannte Raum
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public IGameRoom getGameRoom(String roomName) throws RemoteException;

	/**
	 * Testet, ob ein Spieler in der Datenbank ist.
	 *
	 * @param username
	 *            der Spielername.
	 * @return boolean, true falls der Spielername schon in Database ist.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public boolean isUserInDatabase(String username) throws RemoteException;

	/**
	 * Testet, ob das gegebene Passwort korrekt ist.
	 *
	 * @param username
	 *            der Spielername.
	 * @param password
	 *            das gegebene Passwort.
	 * @return boolean true falls das gegebene Passwort richtig ist.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public boolean checkPasswordForUserInDatabase(String username, String password) throws RemoteException;

	/**
	 * Fuegt einen neuen Account in die Datenbank ein.
	 *
	 * @param name
	 *            der Spielername.
	 * @param password
	 *            das gegebene Passwort.
	 * @return boolean true falls der Spielername neu ist und der Spieler in der
	 *         Datenbank ist.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public boolean createAccount(String name, String password) throws RemoteException;

}
