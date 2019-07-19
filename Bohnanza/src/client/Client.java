package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.ILobby;

public class Client {
	private ILobby lobby;
	private Updater updater;
	private String username;
	private String gameRoom;

	/**
	 * Der Konstruktor der Klasse Client.
	 */
	public Client() {
		updater = new Updater(this);
	}

	/**
	 * In dieser Methode erfolgt die Verbindung zum Server
	 *
	 * @param serverIP
	 *            die IP-Adresse des Spielservers, als String übergeben
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public void connectToServer(String serverIP) throws RemoteException {
		if (isConnected()) {
			return;
		}
		try {
			Registry registry = LocateRegistry.getRegistry(serverIP);
			lobby = (ILobby) registry.lookup("gameServer");
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected() {
		return lobby != null;
	}

	public Updater getUpdater() {
		return updater;
	}

	public ILobby getLobby() {
		return lobby;
	}

	/**
	 * Diese Methode gibt den Benutzernamen zurück
	 *
	 * @return Benutzername als String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Diese Methode gibt den Spielraum zurück
	 *
	 * @return Spielraum als String
	 */
	public String getGameRoom() {
		return gameRoom;
	}

	/**
	 * Diese Methode setzt den Benutzernamen
	 *
	 * @param username
	 *            der als String übergebene Benutzername
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Diese Methode setzt den Namen des Spielraums
	 *
	 * @param gameRoom
	 *            Name des Spielraums wird als String übergeben
	 */
	public void setGameRoom(String gameRoom) {
		this.gameRoom = gameRoom;
	}
}
