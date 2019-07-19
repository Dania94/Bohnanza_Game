package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * GameRoomInterface. Implementiert Methoden zur entfernten Kommunikation mit
 * Clients.
 *
 */
public interface IGameRoom extends Remote {

	/**
	 * Entfernt einen Spieler aus dem Spielraum.
	 * 
	 * @param playerName
	 *            Name des zu entfernenden Spielers.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void removePlayerFromGame(String playerName) throws RemoteException;

	/**
	 * Gibt den Namen des Raums zur�ck.
	 *
	 * @return den Namen des Raums.
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public String getRoomName() throws RemoteException;

	/**
	 * Gibt die Anzahl der im Raum anwesenden Spieler zur�ck.
	 *
	 * @return die Anzahl der Spieler im Raum.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public int getPlayerCount() throws RemoteException;

	/**
	 * Gibt die Namen aller anwesenden Spieler zur�ck.
	 *
	 * @return die Namen aller anwesenden Spieler.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public ArrayList<String> getPlayerNames() throws RemoteException;

	/**
	 * Startet das Spiel.
	 * @param string 
	 * 
	 * @return ob das Spiel gestartet wurde oder nicht.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public boolean startGame(String string) throws RemoteException;

	public Variety getVarietyForPhase2Cards(int index) throws RemoteException;

	/**
	 * Implementiert den Handel zwischen dem aktiven Spieler und einem
	 * Handelspartner.
	 *
	 * @param activePlayer
	 *            der aktive Spieler.
	 * 
	 * @param dealPartner
	 *            der Spieler, mit dem aktiven Spieler einen Deal macht.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public void acceptDeal(String activePlayer, String dealPartner) throws RemoteException;

	/**
	 * Lehnt alle Angebote ab.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void declineAllDeals() throws RemoteException;

	/**
	 * Setzt einen GameRoomListener.
	 *
	 * @param listener
	 *            der Listener, der gesetzt werden soll.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void setGameRoomListener(IGameRoomListener listener) throws RemoteException;

	/**
	 * Der Spieler verl舖st den Spielraum und beendet das Spiel.
	 *
	 * @param playerName
	 *            Der Names des Spielers.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void exitGame(String playerName) throws RemoteException;

	/**
	 * F�gt eine Nachricht vom Spieler der Liste der Nachrichten hinzu.
	 *
	 * @param inputMessage
	 *            die geschikte Nachricht.
	 * @param username
	 *            der Namer des Spieler, der die Nachricht geschickt hat.
	 * @param type
	 *            Typ der Nachricht.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public void sendMessage(String inputMessage, String username, String type) throws RemoteException;

	/**
	 * F�ge dem Spiel einen einfachen Bot hinzu.
	 *
	 * @return ob der Bot hinzugef�gt wurde oder nicht.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean addEasyBot() throws RemoteException;

	/**
	 * F�ge dem Spiel einen schweren Bot hinzu.
	 *
	 * @return ob der Bot hinzugef�gt wurde oder nicht.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean addHardBot() throws RemoteException;

	/**
	 * Lche einen einfachen Bot aus dem Spiel.
	 *
	 * @return ob der Bot gelcht wurde.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean deleteEasyBot() throws RemoteException;

	/**
	 * Lche einen schweren Bot aus dem Spiel.
	 *
	 * @return ob der Bot gelcht wurde.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean deleteHardBot() throws RemoteException;

	/**
	 *
	 * @param playerName
	 *            der Spieler, der die Nachricht geschickt hat.
	 * @return String die Nachricht, die der Spieler deschickt hat.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getRoomMessages(String playerName) throws RemoteException;

	/**
	 *
	 * @return den Namen des aktiven Spielers.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getActivePlayerName() throws RemoteException;

	/**
	 *
	 * @return Liste aller im Raum anwesenden Spieler.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public ArrayList<IPlayer> getAllPlayers() throws RemoteException;

	/**
	 * ﾄndert die Phase des Spiels.
	 *
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public void nextGamePhase() throws RemoteException;

	/**
	 *
	 * @param username
	 *            der Name des Spielers
	 * @return IPlayer der Spieler mit dem gleichen Namen.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public IPlayer getPlayer(String username) throws RemoteException;

	/**
	 * Kann von Clients genutzt werden, um zu erfahren, in welchem Zustand das Spiel
	 * gerade ist.
	 *
	 * @return GameRoomState Zustand das Spiel.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public GameRoomState getGameRoomState() throws RemoteException;

	/**
	 * Entfernt einen Listener aus dem Spielraum.
	 *
	 * @param listener
	 *            der Listener des Raums.
	 * @return ob der Listener entfernt wurde als boolean.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean removeGameRoomListener(IGameRoomListener listener) throws RemoteException;

	/**
	 * Baut eine Handkarte des Spielers auf einem gew臧lten Feld an.
	 * 
	 * @param playerName
	 *            der Name des Spielers.
	 *
	 * @param fieldIndex
	 *            Index des Felds.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	void playHandCard(String playerName, int fieldIndex) throws RemoteException;

	/**
	 * 
	 * @param playerName
	 *            der Name des Spielers.
	 * @param cardIndex
	 *            der Incex der Karte, die gespielt werden soll.
	 * @param fieldIndex
	 *            der Index des Feldes, auf dem die Karte angebaut wird.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	void playReceivedCards(String playerName, int cardIndex, int fieldIndex) throws RemoteException;

	/**
	 * Gibt die Phase, in der das Spiel gerade stattfindet, zuruck.
	 *
	 * @return int die Phase, in der das Spiel gerade stattfindet.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int getGamePhase() throws RemoteException;

	/**
	 * W臧le das Bohnenfeld, dass die grtmliche Anzahl Bohnnentaler einbringt.
	 *
	 * @return Index des Bohnenfelds.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int findMostProfitableField() throws RemoteException;

	/**
	 * ﾜberpr�ft, ob eine gegebene Kartensorte auf einem Feld angebaut wird.
	 *
	 * @param cardVariety
	 *            die �bergebene Kartensorte.
	 * 
	 * @return ob die Kartensorte auf dem Feld liegt.
	 * 
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public boolean varietyOnFields(String cardVariety) throws RemoteException;

	/**
	 * Erfnete eine Handelsphase.
	 * 
	 * @param tradeFirstCard
	 *            true, falls der Spieler die erste gezogene Karte anbieten mhte.
	 * @param tradeSecondCard
	 *            true, falls der Spieler die zweite gezogene Karte anbieten mhte.
	 * @param tradingCards
	 *            Indizes der Handkarten, die Spieler zum Handel anbieten mhte.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public void initiateTrading(boolean tradeFirstCard, boolean tradeSecondCard, ArrayList<Integer> tradingCards)
			throws RemoteException;

	/**
	 * Gibt die Liste der vom genannten Spieler angebotenen Kartensorten zur�ck.
	 * 
	 * @param username
	 *            der Spieler, der die Karten anbietet.
	 * @return die Liste der angebotenen Kartensorten.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public ArrayList<String> getTradeOffer(String username) throws RemoteException;

	/**
	 * Erstellt eine Liste von Handkarten, die der Spieler gegen die angeboteten
	 * Karten tauschen mhte.
	 * 
	 * @param username
	 *            der Spieler, der ein Angebot beantworten mhte.
	 * @param selectedHandcardIndices
	 *            die Indices der Handkarten, die der Spieler tausche mhte.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public void answerTradeOffer(String username, ArrayList<Integer> selectedHandcardIndices) throws RemoteException;

	/**
	 * Gibt die Liste aller Tauschangebote zur�ck, die ein Spieler erhalten hat.
	 * 
	 * @param username
	 *            der Name des aktiven Spielers.
	 * @return Liste aller Angebote.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public ArrayList<TradeOffer> getAllTradeOffers(String username) throws RemoteException;

	/**
	 * Gibt die Liste der Karten zur�ck, die in der dritten Spielphase angebaut
	 * werden m�ssen.
	 * 
	 * @return Liste der Kartensorten, die angebaut werden m�ssen.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public ArrayList<String> getCardsToPlant() throws RemoteException;

	/**
	 * Meldet, ob der Spieler das Spiel manuell beendet hat oder ob das Spiel zu
	 * Ende gespielt wurde.
	 * 
	 * @return den Grund f�r das Spielende als String.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getReasonGameEnded() throws RemoteException;

	/**
	 * Gibt die Punkte zur�ck, die die Spieler erzielt haben.
	 * 
	 * @return die Punkte der Spieler.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public HashMap<String, Integer> getPlayerPoints() throws RemoteException;

	/**
	 * Gibt eine Liste der Kartensorten zur�ck, die der aktive Spieler zum Handel
	 * angeboten hat.
	 * 
	 * @param username
	 *            der Spielername.
	 * @return die Liste der Kartensorten.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public ArrayList<String> getMyOffer(String username) throws RemoteException;
}
