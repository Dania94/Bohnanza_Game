package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.CreateRoomController;
import datenbank.Account;

/**
 *
 * Diese Klasse repräsentiert die Lobby des Spiels.
 *
 */
public class Lobby extends UnicastRemoteObject implements ILobby {

	private static final long serialVersionUID = 1L;
	private List<ILobbyListener> lobbyListeners = Collections.synchronizedList(new ArrayList<ILobbyListener>());
	private Map<String, Player> players = Collections.synchronizedMap(new HashMap<String, Player>());
	private List<GameRoom> gamerooms = Collections.synchronizedList(new ArrayList<GameRoom>());
	private List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
	private Account account = new Account();

	/**
	 * Der Lobby-Konstruktor.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public Lobby() throws RemoteException {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#sendMessage(String inputMessage, String username, String
	 * type)
	 */
	@Override
	public void sendMessage(String inputMessage, String username, String type) throws RemoteException {
		Message message = new Message(inputMessage, username, type, new Date());
		messages.add(message);
		notifyModelChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby# getAllMessages(String username)
	 */
	@Override
	public String getAllMessages(String username) throws RemoteException {
		String messageList = "";
		for (Message m : messages) {
			if (!((m.getUsername().equals(username)) && (m.getType().equals("join")))) {
				Date date = m.getDate();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String a = dateFormat.format(date);
				SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
				try {
					Date time = parseFormat.parse(a);
					messageList += printFormat.format(time) + ":" + m.getUsername() + ": " + m.getMsg() + "\n";
				} catch (ParseException ex) {
					Logger.getLogger(CreateRoomController.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return messageList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#login(String username)
	 */
	@Override
	public void login(String username) throws RemoteException {
		Player player = new Player(username);
		players.put(username, player);
		String joinMessage = " hat die Lobby betreten.";
		String type = "join";
		Message message = new Message(joinMessage, username, type, new Date());
		messages.add(message);
		notifyModelChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#logout(String username)
	 */
	@Override
	public void logout(String username) throws RemoteException {
		Player player = players.get(username);
		if (player != null) {
			players.remove(username);
		} else {
			System.out.println("Warning: Couldn´t find user to logout.");
		}
		notifyModelChange();
	}
	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#deleteRoom(String owner)
	 */

	@Override
	public void deleteRoom(String owner) throws RemoteException {
		for (int i = 0; i < gamerooms.size(); i++) {
			GameRoom room = gamerooms.get(i);
			if (room.getCreator().getPlayerName().equals(owner)) {
				gamerooms.remove(room);
			}
		}
		notifyModelChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#setLobbyListener(ILobbyListener lobbyListener)
	 */
	@Override
	public synchronized void setLobbyListener(ILobbyListener lobbyListener) {
		lobbyListeners.add(lobbyListener);
	}

	/**
	 * Teilt allen horchenden LobbyListenern mit, dass eine Aenderung stattgefunden
	 * hat.
	 */
	private synchronized void notifyModelChange() {
		for (ILobbyListener lobbyListener : lobbyListeners) {
			try {
				lobbyListener.notifyLobbyChange();
			} catch (RemoteException e) {
				// Likely rmi connection lost.
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#setPlayerReady(String username)
	 */
	@Override
	public void setPlayerReady(String username) throws RemoteException {
		Player player = players.get(username);
		player.setReadyToPlay(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#createRoom(String username)
	 */
	@Override
	public String createRoom(String username) throws RemoteException {
		if (isRoomCreator(username)) {
			return null;
		}
		String roomName = username + "'s Spielraum";
		Player player = players.get(username);
		GameRoom gameroom = new GameRoom(roomName, player);
		gameroom.setAccount(account);
		gamerooms.add(gameroom);
		notifyModelChange();
		return roomName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#getAllRoomNames()
	 */
	@Override
	public ArrayList<String> getAllRoomNames() throws RemoteException {
		ArrayList<String> roomNames = new ArrayList<String>();
		for (GameRoom room : gamerooms) {
			roomNames.add(room.getRoomName());
		}
		return roomNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#getAllUserNames()
	 */
	@Override
	public ArrayList<String> getAllUserNames() throws RemoteException {
		ArrayList<String> userNames = new ArrayList<String>();
		for (String username : players.keySet()) {
			userNames.add(username);
		}
		return userNames;
	}

	/**
	 * Testet, ob ein Spieler ein Raumersteller ist.
	 *
	 * @param username
	 *            der Spieler
	 * @return
	 * @throws RemoteException
	 */
	private boolean isRoomCreator(String username) throws RemoteException {
		Player player = players.get(username);
		for (GameRoom room : gamerooms) {
			if (player.equals(room.getCreator())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#joinRoom(String username, String roomName)
	 */
	@Override
	public boolean joinRoom(String username, String roomName) throws RemoteException {
		Player player = players.get(username);
		for (GameRoom gameroom : gamerooms) {
			if (gameroom.getRoomName().equals(roomName)) {
				if (gameroom.getPlayerCount() < 5) {
					try {
						gameroom.addPlayerToGame(player);
						return true;
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#getGameRoom(String roomName)
	 */
	@Override
	public IGameRoom getGameRoom(String roomName) throws RemoteException {
		for (GameRoom room : gamerooms) {
			if (room.getRoomName().equals(roomName)) {
				return room;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#isUserInDatabase(String username)
	 */
	@Override
	public boolean isUserInDatabase(String username) throws RemoteException {
		return account.isUserInDB(username);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#checkPasswordForUserInDatabase(String username, String
	 * password)
	 */
	@Override
	public boolean checkPasswordForUserInDatabase(String username, String password) throws RemoteException {
		String storedPassword = account.getUserPswd(username);
		return storedPassword.equals(password);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ILobby#createAccount(String name, String password)
	 */
	@Override
	public boolean createAccount(String name, String password) throws RemoteException {
		account.makeTable();
		return account.insertNewUser(name, password, 0);
	}

}
