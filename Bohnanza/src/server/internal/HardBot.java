package server.internal;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.Card;
import server.GameRoomState;
import server.IGameRoom;
import server.IPlayer;
import server.TradeOffer;

/**
 *
 * Dise Klasse reprèˆ–entiert den schweren Bot.
 *
 */
public class HardBot implements IBot, Runnable {

	private String botName;
	private volatile IGameRoom gameroom;
	Card phase2Card1;
	Card phase2Card2;
	ArrayList<Card> tradeCards;
	ArrayList<Card> receivedCards;

	/**
	 * Der Konstruktor des Bots.
	 *
	 * @param botName
	 *            der Botname
	 */
	public HardBot(String botName) {
		this.botName = botName;

		// Separater Thread zum Spielen
		new Thread(this).start();
	}

	/*
	 * Lèˆ«ft in einem separaten Thread. Prï¿½ft periodisch, ob der Bot an der Reihe
	 * ist. Spielt dann genau eine Spielphase.
	 */
	@Override
	public void run() {
		while (true) {
			try {
				if (gameroom != null && gameroom.getGameRoomState() == GameRoomState.GAME_ENDED) {
					// Schleife beenden
					return;
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			try {
				if (gameroom != null && gameroom.getGameRoomState() == GameRoomState.PLAYING) {
					// Ist dieser Bot am Zug?
					if (gameroom.getActivePlayerName().equals(botName)) {
						// Phase ausspielen
						try {
							int gamePhase = gameroom.getGamePhase();
							play(gamePhase);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					} else {
						// Decline all trades.
						if (gameroom.getGamePhase() == 2) {
							ArrayList<String> tradeOffer = gameroom.getTradeOffer(botName);
							if (tradeOffer != null) {
								gameroom.answerTradeOffer(botName, null);
							}
						}
					}
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Der Bot beginnt seinen Spielzug.
	 *
	 * @param gamePhase
	 *            die aktuelle Spielphase.
	 * @throws RemoteException
	 *             RMI Exception.
	 */

	private void play(int gamePhase) throws RemoteException {
		if (gamePhase == 1) {
			gameroom.sendMessage("Playing Phase 1", botName, "INFO");
			playFirstPhase();
			gameroom.nextGamePhase();
		}
		if (gamePhase == 2) {
			gameroom.sendMessage("Playing Phase 2", botName, "INFO");
			playSecondPhase();
			gameroom.nextGamePhase();
		}
		if (gamePhase == 3) {
			gameroom.sendMessage("Playing Phase 3", botName, "INFO");
			playThirdPhase();
			gameroom.nextGamePhase();
		}
		if (gamePhase == 4) {
			gameroom.sendMessage("Playing Phase 4", botName, "INFO");
			playPhaseFour();
			gameroom.nextGamePhase();
		}
	}

	/**
	 * Der Bot spielt die erste Speilphase, indem er seine beiden ersten Handkarten
	 * anbaut.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void playFirstPhase() throws RemoteException {
		IPlayer thePlayer = gameroom.getPlayer(botName);
		List<String> varieties = thePlayer.getHandCardVarieties();

		String varietyCard1 = varieties.get(0);
		String varietyCard2 = varieties.get(1);

		gameroom.playHandCard(botName, chooseField(varietyCard1));
		gameroom.playHandCard(botName, chooseField(varietyCard2));
	}

	/**
	 * Der Bot spielt die zweite Spielphase, indem er zwei Karten vom Stapel zieht
	 * und entscheidet, ob er sie behalten oder zum Tausch anbieten mî’‹hte.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void playSecondPhase() throws RemoteException {

		String varietyCard1 = gameroom.getVarietyForPhase2Cards(0).toString();
		String varietyCard2 = gameroom.getVarietyForPhase2Cards(1).toString();

		IPlayer thePlayer = gameroom.getPlayer(botName);

		boolean tradeFirstCard = false;
		boolean tradeSecondCard = false;

		if (!gameroom.varietyOnFields(varietyCard1)) {
			tradeFirstCard = true;
		}
		if (!gameroom.varietyOnFields(varietyCard2)) {
			tradeSecondCard = true;
		}

		// startet Handel nicht, wenn nicht mind. eine der gezogenen Karten angeboten
		// wird
		if (!tradeFirstCard && !tradeSecondCard) {
			return;
		}

		if (thePlayer.getCardCount() > 2) {
			ArrayList<Integer> tradingCards = new ArrayList<Integer>();
			// wè‡§lt zufèˆ�lig, ob er eine oder zwei Karten zum Handel anbietet
			Random random = new Random();
			boolean isOne = random.nextBoolean();
			if (isOne) {
				int index = random.nextInt(thePlayer.getCardCount());
				tradingCards.add(index);
			} else {
				int index1 = random.nextInt(thePlayer.getCardCount());
				int index2 = random.nextInt(thePlayer.getCardCount());
				if (index1 != index2) {
					tradingCards.add(index1);
					tradingCards.add(index2);
					// falls zweimal der gleiche Index gewè‡§lt wird, handele nur eine Karte
				} else {
					tradingCards.add(index1);
				}
			}

			gameroom.initiateTrading(tradeFirstCard, tradeSecondCard, tradingCards);

			boolean loop = true;

			while (loop) {

				ArrayList<TradeOffer> offers = gameroom.getAllTradeOffers(botName);
				if (offers.size() == gameroom.getPlayerCount() - 1) {
					gameroom.sendMessage("Niemand will handeln.", botName, "INFO");
					gameroom.declineAllDeals();
					return;
				}

				if (offers != null && offers.size() > 0) {
					int accept = findBestOffer(offers);
					// Falls es keine guten Angebote gibt, akzeptiere einfach das erste Angebot
					if (accept < 0) {
						accept = 0;
					}
					TradeOffer offer = offers.get(accept);

					if (offer.getVarieties() != null) {
						String dealPartner = offer.getPlayerName();

						gameroom.acceptDeal(botName, dealPartner);
						gameroom.sendMessage("Akzeptiere " + dealPartner + "'s Angebot", botName, "INFO");
						loop = false;
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private int findBestOffer(ArrayList<TradeOffer> offers) {
		int varietyCounter = 0;
		int index = -1;
		for (int i = 0; i < offers.size(); i++) {
			int temp = 0;
			if (offers.get(i).getVarieties() != null) {
				for (String variety : offers.get(i).getVarieties()) {
					try {
						if (gameroom.varietyOnFields(variety)) {
							temp++;
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				if (temp > varietyCounter) {
					varietyCounter = temp;
					index = i;
				}
			}
		}
		return index;
	}

	/**
	 * Der Bot spielt die dritte Spielphase, indem er die beiden gezogenen Karten
	 * und alle erhandelten Karten anbaut.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void playThirdPhase() throws RemoteException {

		ArrayList<String> plantCards = gameroom.getCardsToPlant();

		if (plantCards != null) {

			gameroom.sendMessage("I've got " + plantCards.size() + " cards to plant.", botName, "INFO");

			for (int i = plantCards.size() - 1; i >= 0; i--) {
				gameroom.sendMessage(i + " Karten anzubauen.", botName, "INFO");
				gameroom.playReceivedCards(botName, i, chooseField(plantCards.get(i)));
			}
		} else {
			gameroom.sendMessage("That's a nice null.", botName, "INFO");
		}
	}

	/**
	 * Der Bot spielt die vierte Spielphase, indem er drei neue Karten vom Stapel
	 * zieht und seinen Handkarten hinzufï¿½gt.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	private void playPhaseFour() throws RemoteException {
	}

	/**
	 * Wè‡§lt das Feld, auf dem der Bot die Karte anbauen wird.
	 *
	 * @param cardVariety
	 *            die Sorte der anzubauenden Karte.
	 * @return den Index des Felds, auf dem die Karte angebaut wird.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int chooseField(String cardVariety) throws RemoteException {
		IPlayer thePlayer = gameroom.getPlayer(botName);

		int numberOfBeanfields = thePlayer.getNumberOfBeanfields();

		for (int i = 0; i < numberOfBeanfields; i++) {
			String beanfieldVariety = thePlayer.getBeanfieldString(i);

			if (cardVariety.equals(beanfieldVariety)) {
				return i;
			} else if (thePlayer.getBeanfieldCardCount(i) == 0) {
				return i;
			}
		}
//		int harvest = gameroom.findMostProfitableField();
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.internal.IBot#setGameRoom(IGameRoom room)
	 */
	@Override
	public void setGameRoom(IGameRoom room) throws RemoteException {
		this.gameroom = room;
	}
}