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

import datenbank.Account;
import server.internal.EasyBot;
import server.internal.HardBot;

public class GameRoom extends UnicastRemoteObject implements IGameRoom {

	private static final long serialVersionUID = 1L;
	private String name;
	private Player creator;
	private List<Player> players = Collections.synchronizedList(new ArrayList<Player>());
	private Stack cardStack;
	private Stack discardStack = new Stack();
	private List<IGameRoomListener> gameroomListeners = Collections.synchronizedList(new ArrayList<IGameRoomListener>());
	private List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());
	private int botIndex = 0;
	private int activePlayer;
	private int gamePhase = 1;
	private int timesShuffled = 0;
	private List<Player> easyBots = Collections.synchronizedList(new ArrayList<Player>());
	private List<Player> hardBots = Collections.synchronizedList(new ArrayList<Player>());
	private volatile Map<String, TradeOffer> tradeOffers = null;
	private int cardsPlayedThisRound = 0;
	private String reason;

	// Karten, die in Phase 2 vom Kartenstapel gezogen werden
	private Card phase2Card1;
	private Card phase2Card2;

	private GameRoomState gameRoomState = GameRoomState.PREPARING;

	public static final int PHASE_1_PLANT_CARDS = 1;
	public static final int PHASE_2_TRADE = 2;
	public static final int PHASE_3_BLUB = 3;
	public static final int PHASE_4_STUFF = 4;
	public static final int MOVE_TO_NEXT_PLAYER = 5;

	private Account account;

	/**
	 *
	 * @param name
	 *            der Name des Raums.
	 * @param creator
	 *            der Spieler, der den Raum erstellen moechte.
	 * @throws RemoteException
	 *             RMI Exception
	 *
	 */
	public GameRoom(String name, Player creator) throws RemoteException {
		this.name = name;
		this.creator = creator;
		this.addPlayerToGame(creator);
		cardStack = createInitialCardStack();
	}

	/**
	 * Getter der Ersteller des Raums.
	 *
	 * @return Player der Ersteller des Raums.
	 */
	public Player getCreator() {
		return creator;
	}

	/**
	 * F�llt den initialen Kartenstapel.
	 *
	 * @return den kompletten Stapel.
	 */
	private Stack createInitialCardStack() {
		ArrayList<Card> createStack = new ArrayList<Card>();
		Stack stack = new Stack(createStack);
		stack.fillInitialStack();
		stack.shuffle();
		return stack;
	}

	/**
	 * F�gt einen Spieler zum Raum hinzu.
	 *
	 * @param player
	 *            der Spieler, der im Raum eigef�gt werden soll.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void addPlayerToGame(Player player) throws RemoteException {
		if (!(this.players.contains(player))) {
			if (this.players.size() < 5) {
				players.add(player);
				notifyRoomChange();
			} else {
				System.out.println("Error: Room is full.");
			}
		} else {
			System.out.println("Warning: Player already in Room.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#removePlayerFromGame(String playerName)
	 */
	@Override

	public void removePlayerFromGame(String playerName) throws RemoteException {
		Player player = getPlayerForName(playerName);
		players.remove(player);
		notifyRoomChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getPlayerCount()
	 */
	@Override

	public int getPlayerCount() throws RemoteException {
		return players.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getRoomName()
	 */
	@Override

	public String getRoomName() throws RemoteException {
		return name;
	}

	/**
	 * Verteilt Bohnenfelder an die Spieler entsprechend der Anzahl der Spieler.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void giveBeanfields() throws RemoteException {
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setBeanfields(players.size());
			notifyRoomChange();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getPlayerNames()
	 */
	@Override

	public ArrayList<String> getPlayerNames() throws RemoteException {
		ArrayList<String> playerNames = new ArrayList<String>();
		for (Player player : players) {
			playerNames.add(player.getPlayerName());
		}
		return playerNames;
	}

	/**
	 * Gibt jedem Spieler fuenf Handkarten am Spielanfang.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void giveInitialHandcards() throws RemoteException {
		for (Player player : players) {
			for (int i = 0; i < 5; i++) {
				player.addHandCard(cardStack.drawCard());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#startGame()
	 */
	@Override
	public boolean startGame(String playerName) throws RemoteException {
		
		if(!playerName.equals(creator.getPlayerName())) {
			return false;
		}
		
		if (players.size() >= 3) {
			giveBeanfields();
			giveInitialHandcards();
			gameRoomState = GameRoomState.PLAYING;
			notifyRoomChange();
			return true;
		}
		return false;
	}

	/**
	 * Gibt der Spieler Geld nachdem Erntent des Felds.
	 *
	 * @param player
	 *            Der Spieler, der das Geld bekommt.
	 * @param harvest
	 *            Die Karten, die im Bohnofeld stattfinden.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void getCoinsForHarvest(Player player, ArrayList<Card> harvest) throws RemoteException {
		int cardCount = harvest.size();
		ArrayList<Pair> bohnoMeter = harvest.get(0).getBohnoMeter();
		int amount = calculateCoins(bohnoMeter, cardCount);
		player.addTaler(amount);
		for (int i = 0; i < amount; i++) {
			harvest.remove(harvest.size() - 1);
		}
		discard(harvest);
		notifyRoomChange();
	}

	/**
	 * Eine Helfermethoder, die die korrekte Anzahl der Coins nachdem Ernten des
	 * Bohnofelds gibt.
	 *
	 *
	 * @param bohnoMeter
	 *            Eine Liste von Coins und Anzahl der Karten, die der Spieler ernten
	 *            soll, um die Coins zu bekommen.
	 *
	 * @param cardCount
	 *            Die Anzahl der Karten im Ernten.
	 *
	 * @return int Die Anzahl der Coins.
	 */
	private int calculateCoins(ArrayList<Pair> bohnoMeter, int cardCount) {
		Pair p = getHighestCardCount(bohnoMeter, cardCount);
		if (p == null) {
			// Menge an Karten ist nichts wert.
			return 0;
		}
		return p.getNumberOfCoins();
	}

	/**
	 * Finde das Pair mit der hoechsten Kartenanzahl, dessen Kartenanzahl kleiner
	 * gleich der tatsaechlichen Karten ist.
	 *
	 * @param bohnoMeter
	 *            Es zeigt an, wie viel Geld der Spieler bekommt, wenn er diese
	 *            Sorte ernt. Die Nummer zeigen an, wie viele Karten der Spieler
	 *            ernten soll, um dieses Geld zu bekommen.
	 * @param cardCount
	 *            die Anzahl der Karten im Bohnenfeld.
	 * @return Pair das Pair mit der höchsten Kartenanzahl
	 */
	private Pair getHighestCardCount(ArrayList<Pair> bohnoMeter, int cardCount) {
		Pair highest = null;

		for (Pair pair : bohnoMeter) {
			if (pair.getNumberOfCards() <= cardCount) {
				// Groesser als das bisher groesste gefundene Pair.
				if (highest == null || highest.getNumberOfCards() < pair.getNumberOfCards()) {
					highest = pair;
				}
			}
		}

		return highest;
	}

	/**
	 *
	 * Addiere Karten im Ablagestapel.
	 *
	 * @param cards
	 *            Die Karten, die abgeworfen werden.
	 */
	private void discard(ArrayList<Card> cards) {
		for (Card card : cards) {
			discardStack.addCard(card);
		}
		cards.clear();
		notifyRoomChange();
	}

	/**
	 *
	 * Helfermethode, die eine Karte im Bohnofeld addiert.
	 *
	 * @param player
	 *            Der Spieler, der diese Karte spielt.
	 * @param card
	 *            Die Karte, die im Bohnofeld eingefuegt werden soll.
	 * @param field
	 *            Der Bohnofeld, im dem die Karte des Spielers eingefuegt werden
	 *            soll.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void playCard(Player player, Card card, Beanfield field) throws RemoteException {
		if (field.getCardCount() == 0) {
			field.addCard(card);
			notifyRoomChange();
			return;
		}
		if (fieldProtected(field)) {
			return;
		}
		if (field.getFieldVariety().equals(card.getVariety())) {
			field.addCard(card);
			notifyRoomChange();
			return;
		} else {
			ArrayList<Card> harvestedCards = field.harvestCards();
			getCoinsForHarvest(player, harvestedCards);
			field.addCard(card);
			notifyRoomChange();
			return;
		}
	}

	/**
	 * Testet, ob ein gewaehltes Feld unter die Bohnenschutzregel faellt.
	 *
	 * @param field
	 *            das gewaehlte Bohnenfeld.
	 * @return boolean
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private boolean fieldProtected(Beanfield field) throws RemoteException {
		Player player = players.get(activePlayer);
		if (players.size() > 3) {
			if (player.getBeanfield(0).getCardCount() == 1 && player.getBeanfield(1).getCardCount() == 1) {
				return false;
			}
		} else {
			if (player.getBeanfield(0).getCardCount() == 1 && player.getBeanfield(1).getCardCount() == 1
					&& player.getBeanfield(2).getCardCount() == 1) {
				return false;
			}
		}
		int limit = 0;
		if (players.size() == 3) {
			limit = 3;
		} else {
			limit = 2;
		}
		for (int i = 0; i < limit; i++) {
			int count = player.getBeanfield(i).getCardCount();
			boolean sameField = player.getBeanfield(i).equals(field);
			if (!sameField && count == 1) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#playHandCard(int fieldIndex)
	 */
	@Override
	public void playHandCard(String playerName, int fieldIndex) throws RemoteException {
		Player player = players.get(activePlayer);
		// Verhindern, dass andere Player ein anbauen auslöen.
		if (!playerName.equals(player.getPlayerName())) {
			return;
		}
		// Anbau nur in Phase 1 oder 3 möglich.
		if (gamePhase != 1 && gamePhase != 3) {
			return;
		}

		if (gamePhase == 1 && cardsPlayedThisRound >= 2) {
			return;
		}
		cardsPlayedThisRound++;

		Card card = player.getHandCards().remove(0);
		Beanfield field = player.getBeanfields().get(fieldIndex);
		playCard(player, card, field);
	}

	/**
	 * Testet, ob der Kartenstapel leer ist und zaehlt, wie haeufig er bereits leer
	 * war.
	 * 
	 * @throws RemoteException
	 */
	private void testShuffle() {
		if (cardStack.getNumberOfCards() == 0) {
			if (timesShuffled < 3) {
				cardStack = discardStack;
				cardStack.shuffle();
				discardStack = new Stack();
				timesShuffled++;
			} else {
				try {
					exitGame(null);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom# acceptDeal(String activePlayerName, String
	 * dealPartnerName)
	 */
	@Override
	public void acceptDeal(String activePlayerName, String dealPartnerName) throws RemoteException {
		tradeOffers = null;
		Player activePlayer = getPlayerForName(activePlayerName);
		Player dealPartner = getPlayerForName(dealPartnerName);
		
		if(activePlayer!=null && dealPartner!=null) {

		for (Card card : dealPartner.getOffer()) {
			activePlayer.getReceivedCards().add(card);
			dealPartner.removeCardFromHand(card);
		}
		for (Card card : activePlayer.getOffer()) {
			dealPartner.getReceivedCards().add(card);
			if (card.equals(phase2Card1)) {
				phase2Card1 = null;
			} else if (card.equals(phase2Card2)) {
				phase2Card2 = null;
			} else {
				activePlayer.removeCardFromHand(card);
			}
		}
		
		}
		clearOffers();
		notifyRoomChange();
	}

	/**
	 * Gibt der Spieler mit dem gleichen Namen zurück.
	 *
	 * @param playerName
	 *            Der Name des Spielers.
	 * @return Player Der Spieler mit dem gleichen Namen.
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private Player getPlayerForName(String playerName) throws RemoteException {
		for (Player p : players) {
			if (p.getPlayerName().equals(playerName)) {
				return p;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#declineDeal(String activePlayerName, String
	 * dealPartnerName)
	 */
	@Override
	public void declineAllDeals() throws RemoteException {
		clearOffers();
		notifyRoomChange();
	}

	private void clearOffers() {
		tradeOffers = null;
		for (Player player : players) {
			player.getOffer().clear();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#setGameRoomListener(IGameRoomListener listener)
	 */
	@Override

	public synchronized void setGameRoomListener(IGameRoomListener listener) throws RemoteException {
		gameroomListeners.add(listener);
	}

	/**
	 * Teilt allen GameRoomListenern mit, dass es eine ﾄnderung im Spielraum gibt.
	 */
	private synchronized void notifyRoomChange() {
		for (IGameRoomListener listener : gameroomListeners) {
			try {
				listener.notifyGameRoomChange();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#exitGame(String playerName)
	 */
	@Override
	public void exitGame(String playerName) throws RemoteException {

		if (playerName == null) {
			String winner = findWinner();
			reason = winner + " hat gewonnen!";

			int punkte = account.getUserPunkte(winner);
			account.updateUserPunkte(winner, punkte + 1);
		} else {
			reason = playerName + " hat das Spiel beendet.";
		}

		gameRoomState = GameRoomState.GAME_ENDED;
		notifyRoomChange();
	}

	private String findWinner() throws RemoteException {
		int winnerTaler = -1;
		Player winner = null;

		for (Player p : players) {
			if (p.getTaler() > winnerTaler) {
				winner = p;
			}
		}
		return winner.getPlayerName();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getReasonGameEnded()
	 */
	@Override
	public String getReasonGameEnded() throws RemoteException {
		return reason;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#sendMessage(String inputMessage, String username,
	 * String type)
	 */
	@Override

	public void sendMessage(String inputMessage, String username, String type) throws RemoteException {
		Message message = new Message(inputMessage, username, type, new Date());
		messages.add(message);
		notifyRoomChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#addEasyBot()
	 */
	@Override
	public boolean addEasyBot() throws RemoteException {
		if (players.size() >= 5) {
			return false;
		} else {
			String botName = "EasyBot" + botIndex;
			EasyBot easy = new EasyBot(botName);
			Player easyBot = new Player(botName);
			easyBot.setBot(true, easy);
			easy.setGameRoom(this);
			botIndex++;
			players.add(easyBot);
			easyBots.add(easyBot);
			notifyRoomChange();
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#addHardBot()
	 */
	@Override
	public boolean addHardBot() throws RemoteException {
		if (players.size() >= 5) {
			return false;
		} else {
			String botName = "HardBot" + botIndex;
			HardBot hard = new HardBot(botName);
			Player hardBot = new Player(botName);
			hardBot.setBot(true, hard);
			hard.setGameRoom(this);
			botIndex++;
			players.add(hardBot);
			hardBots.add(hardBot);
			notifyRoomChange();
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom# deleteEasyBot()
	 */
	@Override
	public boolean deleteEasyBot() throws RemoteException {
		if (easyBots.size() > 0) {
			players.remove(easyBots.get(easyBots.size() - 1));
			easyBots.remove(easyBots.size() - 1);
			notifyRoomChange();
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#deleteHardBot()
	 */
	@Override
	public boolean deleteHardBot() throws RemoteException {
		if (hardBots.size() > 0) {
			players.remove(hardBots.get(hardBots.size() - 1));
			hardBots.remove(hardBots.size() - 1);
			notifyRoomChange();
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getRoomMessages(String playerName)
	 */
	@Override
	public String getRoomMessages(String playerName) throws RemoteException {
		String messageList = "";
		for (Message m : messages) {
			if (!((m.getUsername().equals(playerName)) && (m.getType().equals("join")))) {
				Date date = m.getDate();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String a = dateFormat.format(date);
				SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
				try {
					Date time = parseFormat.parse(a);
					messageList += printFormat.format(time) + ":" + m.getUsername() + ": " + m.getMsg() + "\n";
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
			}
		}
		return messageList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getActivePlayerName()
	 */
	@Override
	public synchronized String getActivePlayerName() throws RemoteException {
		return players.get(activePlayer).getPlayerName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getAllPlayers()
	 */
	@Override
	public ArrayList<IPlayer> getAllPlayers() throws RemoteException {
		ArrayList<IPlayer> playerList = new ArrayList<IPlayer>();
		for (Player player : players) {
			playerList.add(player);
		}
		return playerList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#nextGamePhase()
	 */
	@Override
	public void nextGamePhase() throws RemoteException {

		switch (gamePhase) {
		case PHASE_1_PLANT_CARDS:
			// Spieler muss mindestens eine Karte spielen.
			if (cardsPlayedThisRound == 0) {
				break;
			}
			cardsPlayedThisRound = 0;
			gamePhase = PHASE_2_TRADE;
			drawPhase2Cards(players.get(activePlayer).getPlayerName());
			break;
		case PHASE_2_TRADE:
			gamePhase = PHASE_3_BLUB;
			if (phase2Card1 != null) {
				players.get(activePlayer).getReceivedCards().add(phase2Card1);
				phase2Card1 = null;
			}
			if (phase2Card2 != null) {
				players.get(activePlayer).getReceivedCards().add(phase2Card2);
				phase2Card2 = null;
			}
			break;
		case PHASE_3_BLUB:
			// Man kann erst weitergehen, wenn alle Spieler ihre erhaltenen Karten angebaut
			// haben.
			if (receivedCardsLeft()) {
				break;
			}

			tradeOffers = null;
			gamePhase = PHASE_4_STUFF;
			drawPhase4Cards(players.get(activePlayer).getPlayerName());
			break;
		case PHASE_4_STUFF:
			gamePhase = PHASE_1_PLANT_CARDS;
			switchToNextPlayer();
			break;
		}

		notifyRoomChange();
	}

	private boolean receivedCardsLeft() {
		for (Player p : players) {
			if (p.getReceivedCards().size() > 0) {
				return true;
			}
		}
		return false;
	}

	private void drawPhase2Cards(String playerName) {
		testShuffle();
		phase2Card1 = cardStack.drawCard();
		testShuffle();
		phase2Card2 = cardStack.drawCard();
		notifyRoomChange();
	}

	private void drawPhase4Cards(String playerName) throws RemoteException {
		for (int i = 0; i < 3; i++) {
			testShuffle();
			Card card = cardStack.drawCard();
			players.get(activePlayer).addHandCard(card);
			notifyRoomChange();
		}
	}

	/**
	 * Macht den nächsten Spieler zum aktiven Spieler.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void switchToNextPlayer() throws RemoteException {
		if (activePlayer == players.size() - 1) {
			activePlayer = 0;
			notifyRoomChange();
		} else {
			activePlayer++;
			notifyRoomChange();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getPlayer(java.lang.String)
	 */
	@Override
	public IPlayer getPlayer(String username) throws RemoteException {
		for (IPlayer player : players) {
			if (player.getPlayerName().equals(username)) {
				return player;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#gameEnded()
	 */
	@Override
	public GameRoomState getGameRoomState() throws RemoteException {
		return gameRoomState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#removeGameRoomListener(IGameRoomListener)
	 */
	@Override
	public synchronized boolean removeGameRoomListener(IGameRoomListener listener) throws RemoteException {
		return gameroomListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getGamePhase()
	 */
	@Override
	public int getGamePhase() throws RemoteException {
		return gamePhase;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#getVarietyForPhase2Cards(int)
	 */
	@Override
	public Variety getVarietyForPhase2Cards(int index) throws RemoteException {
		if (index == 0) {
			if (phase2Card1 == null) {
				throw new NullPointerException();
			}
			return phase2Card1.getVariety();
		}
		if (index == 1) {
			if (phase2Card2 == null) {
				throw new NullPointerException();
			}
			return phase2Card2.getVariety();
		}
		throw new IndexOutOfBoundsException();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#movePhase2CardToHand(int index)
	 */
	@Override
	public void playReceivedCards(String playerName, int cardIndex, int fieldIndex) throws RemoteException {
		Player player = getPlayerForName(playerName);
		Beanfield field = player.getBeanfields().get(fieldIndex);
		// System.out.println("Kartenindex: " + cardIndex);
		Card card = player.getReceivedCards().get(cardIndex);
		playCard(player, card, field);
		player.getReceivedCards().remove(card);
		notifyRoomChange();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#findMostProfitableField()
	 */
	@Override
	public int findMostProfitableField() throws RemoteException {
		Player player = players.get(activePlayer);
		ArrayList<Pair> bohnoMeter1 = player.getBeanfield(0).getCards().get(0).getBohnoMeter();
		int cardCount1 = player.getBeanfield(0).getCardCount();
		ArrayList<Pair> bohnoMeter2 = player.getBeanfield(1).getCards().get(0).getBohnoMeter();
		int cardCount2 = player.getBeanfield(1).getCardCount();
		int field1 = calculateCoins(bohnoMeter1, cardCount1);
		int field2 = calculateCoins(bohnoMeter2, cardCount2);
		int result = Math.max(field1, field2);
		if (players.size() == 3) {
			ArrayList<Pair> bohnoMeter3 = player.getBeanfield(2).getCards().get(0).getBohnoMeter();
			int cardCount3 = player.getBeanfield(2).getCardCount();
			int field3 = calculateCoins(bohnoMeter3, cardCount3);
			result = Math.max(result, field3);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see server.IGameRoom#varietyOnFields(String cardVariety)
	 */
	@Override
	public boolean varietyOnFields(String cardVariety) throws RemoteException {
		int limit = 0;
		if (players.size() == 3) {
			limit = 3;
		} else if (players.size() > 3) {
			limit = 2;
		}
		for (int i = 0; i < players.size(); i++) {
			if (i < limit) {
				if (players.get(activePlayer).getBeanfieldString(i).equals(cardVariety)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#initiateTrading(boolean, boolean, ArrayList)
	 */
	@Override
	public void initiateTrading(boolean tradeFirstCard, boolean tradeSecondCard, ArrayList<Integer> tradingCards)
			throws RemoteException {
		tradeOffers = Collections.synchronizedMap(new HashMap<String, TradeOffer>());
		Player player = players.get(activePlayer);
		if (tradeFirstCard) {
			player.addToOffer(phase2Card1);
		}
		if (tradeSecondCard) {
			player.addToOffer(phase2Card2);
		}
		for (Integer index : tradingCards) {
			player.addToOffer(player.getHandCards().get(index));
		}
		notifyRoomChange();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getTradeOffer(String)
	 */
	@Override
	public ArrayList<String> getTradeOffer(String username) throws RemoteException {
		if (tradeOffers == null) {
			return null;
		}
		if (tradeOffers.containsKey(username)) {
			return null;
		}

		// Gib die angebotenen Karten vom aktiven Spieler zurück. 
		Player player = players.get(activePlayer);
		
		ArrayList<String> tradeOffer = new ArrayList<String>();
		for (Card card : player.getOffer()) {
			tradeOffer.add(card.getVariety().toString());
		}
		return tradeOffer;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#answerTradeOffer(String, ArrayList)
	 */
	@Override
	public void answerTradeOffer(String username, ArrayList<Integer> selectedHandcardIndices) throws RemoteException {
		try {
		Player player = getPlayerForName(username);
		if (selectedHandcardIndices != null) {
			for (Integer index : selectedHandcardIndices) {
				player.addToOffer(player.getHandCards().get(index));
			}
			
			ArrayList<String> offerVarieties = new ArrayList<>();
			for(Card card : player.getOffer()) {
				offerVarieties.add(card.getVariety().toString());
			}

			TradeOffer offer = new TradeOffer(username, offerVarieties);
			tradeOffers.put(username, offer);
		} else {
			TradeOffer offer = new TradeOffer(username, null);
			tradeOffers.put(username, offer);
		}
		} catch(NullPointerException e) {
			// tradeOffers was set to null in the meantime.
		}

		notifyRoomChange();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getAllTradeOffers(String)
	 */
	@Override
	public ArrayList<TradeOffer> getAllTradeOffers(String username) throws RemoteException {
		if (tradeOffers == null) {
			return null;
		}
		ArrayList<TradeOffer> offers = new ArrayList<TradeOffer>();
		for (String name : tradeOffers.keySet()) {
			offers.add(tradeOffers.get(name));
		}
		return offers;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getCardsToPlant()
	 */
	@Override
	public ArrayList<String> getCardsToPlant() throws RemoteException {
		ArrayList<String> cardsToPlant = new ArrayList<String>();
		for (Card card : players.get(activePlayer).getReceivedCards()) {
			cardsToPlant.add(card.getVariety().toString());
		}
		return cardsToPlant;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getPlayerPoints()
	 */
	@Override
	public HashMap<String, Integer> getPlayerPoints() throws RemoteException {
		HashMap<String, Integer> playerPoints = new HashMap<String, Integer>();
		for (Player player : players) {
			playerPoints.put(player.getPlayerName(), player.getTaler());
		}
		return playerPoints;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see server.IGameRoom#getMyOffer(String)
	 */
	@Override
	public ArrayList<String> getMyOffer(String username) throws RemoteException {
		Player player = getPlayerForName(username);
		ArrayList<String> playerOffer = new ArrayList<String>();
		for (Card card : player.getOffer()) {
			playerOffer.add(card.getVariety().toString());
		}
		return playerOffer;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
