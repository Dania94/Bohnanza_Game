package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import server.internal.IBot;

public class Player extends UnicastRemoteObject implements IPlayer {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int playerSeatNumber;
	private boolean readyToPlay = false;
	private List<Beanfield> beanfields = Collections.synchronizedList(new ArrayList<Beanfield>());
	private List<Card> handCards = Collections.synchronizedList(new ArrayList<Card>());
	private int coins;
	private List<Card> offeredCards = Collections.synchronizedList(new ArrayList<Card>());
	private List<Card> receivedCards = Collections.synchronizedList(new ArrayList<Card>());
	private boolean isBot;
	private IBot bot;

	/**
	 * Der Konstruktor der Klasse Player.
	 *
	 * @param name
	 *            der Spielername.
	 * @throws RemoteException
	 *             RMI Exception
	 */

	public Player(String name) throws RemoteException {
		this.name = name;
		this.isBot = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getTaler()
	 */
	@Override
	public int getTaler() throws RemoteException {
		return coins;
	}

	/**
	 * Gibt den Sitzplatz des Spielers zurueck.
	 *
	 *
	 * @return int Sitzplatz des Spielers..
	 */
	public int getPlayerSeatNumber() {
		return playerSeatNumber;
	}

	/**
	 * Set den Sitzplatz des Spielers.
	 *
	 * @param playerSeatNumber
	 *            Sitzplatz des Spieler
	 */
	public void setPlayerSeatNumber(int playerSeatNumber) {
		this.playerSeatNumber = playerSeatNumber;
	}

	/**
	 * Set Spielerbereit.
	 *
	 * @param ready
	 *            Spieler ist bereit zu spielen.
	 */
	public void setReadyToPlay(boolean ready) {
		this.readyToPlay = ready;
	}

	/**
	 * Get des Attributs readyToPlay
	 *
	 * @return boolean readyToPlay.
	 */
	public boolean getReadyToPlay() {
		return readyToPlay;
	}

	/**
	 * Gibt die Bohnenfelder des Spielers zurueck.
	 *
	 * @return die Liste der Bohnenfelder
	 */
	public List<Beanfield> getBeanfields() {
		return beanfields;
	}

	/**
	 * Gibt dem Spieler die Bohnenfelder.
	 *
	 * @param playerCount
	 *            die Anzahl der Spieler.
	 */
	public void setBeanfields(int playerCount) {
		if (this.beanfields.isEmpty()) {
			beanfields.add(new Beanfield());
			beanfields.add(new Beanfield());
			if (playerCount == 3) {
				beanfields.add(new Beanfield());
			}
		}
	}

	/**
	 * Gibt die Karten des Spieler zurueck.
	 *
	 * @return die Liste der Handkarten
	 */
	public List<Card> getHandCards() {
		return handCards;
	}

	/**
	 * Entferne eine Karte von der Spielerhand.
	 *
	 * @param card
	 *            die Karte, die von der Spielerhand entfernt werden soll.
	 */
	public void removeCardFromHand(Card card) {
		handCards.remove(card);
	}

	/**
	 * Aendert die Anzahl der Taler des Spielers.
	 *
	 * @param amount
	 *            die Anzahl der Coins, die der Spieler beim Ernten bekommen hat.
	 * 
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public void addTaler(int amount) throws RemoteException {
		coins = coins + amount;
	}

	/**
	 * Gibt alle Karten, die der Spieler beim Handeln bekommen hat, zurueck.
	 *
	 * @return die Karten, die der Spieler beim Handln bekommen hat.
	 */
	public List<Card> getReceivedCards() {
		return receivedCards;
	}

	/**
	 * Addiere eine Karte zu der Angebotkartenliste.
	 *
	 * @param card
	 *            die Karte, die eingef�gt werden soll.
	 */
	public void addToOffer(Card card) {
		offeredCards.add(card);
	}

	/**
	 * Gibt die Karten, die der Spieler anbietet, zurueck.
	 *
	 * @return die Karten, die der Spieler anbietet.
	 */
	public List<Card> getOffer() {
		return offeredCards;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getPlayerName()
	 */
	@Override
	public String getPlayerName() throws RemoteException {
		return name;
	}

	public Beanfield getBeanfield(int index) throws RemoteException {
		if ((!(this.beanfields.isEmpty())) && (this.beanfields.size() > index)) {
			return beanfields.get(index);
		} else {
			System.out.println("Error: Beanfield not found.");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getTalerString()
	 */
	@Override
	public String getTalerString() throws RemoteException {
		String talerString = Integer.toString(coins);
		return talerString;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getBeanfieldString(int index)
	 */
	@Override
	public String getBeanfieldString(int index) throws RemoteException {
		String fieldString = "";
		if (beanfields.size() == 0 || beanfields.get(index).getFieldVariety() == Variety.NULL) {
			fieldString = "leer";
		} else {
			fieldString = beanfields.get(index).getFieldVariety().toString();
		}
		return fieldString;
	}

	public void addHandCard(Card card) throws RemoteException {
		handCards.add(card);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getHandCardVarieties()
	 */
	@Override
	public ArrayList<String> getHandCardVarieties() throws RemoteException {
		ArrayList<String> varieties = new ArrayList<String>();
		for (Card card : handCards) {
			varieties.add(card.getVariety().toString());
		}
		return varieties;
	}

	/**
	 * Set des Attribut isBot, falls der Spieler ein Bot ist.
	 *
	 * @param isBot
	 *            prueft, ob der Spieler ein Bot ist.
	 * @param bot
	 *            der Art des Bots.
	 */
	public void setBot(boolean isBot, IBot bot) {
		this.isBot = isBot;
		this.bot = bot;
	}

	/**
	 * Gibt der Art des Bots zurueck.
	 *
	 * @return der Art des Bots.
	 */
	public IBot getBot() {
		if (isBot) {
			return bot;
		} else {
			return null;
		}
	}

	/**
	 * Gibt des Attribute isBot zurueck.
	 *
	 * @return boolean isBot.
	 */
	public boolean isBot() {
		return isBot;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getNumberOfBeanfields()
	 */
	@Override
	public int getNumberOfBeanfields() {
		return beanfields.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getBeanfieldCardCount(int index)
	 */
	@Override
	public int getBeanfieldCardCount(int index) throws RemoteException {
		return beanfields.get(index).getCardCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.IPlayer#getCardCount()
	 */
	@Override
	public int getCardCount() throws RemoteException {
		return handCards.size();
	}
}
