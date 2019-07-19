package server;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * This class represent the components of the card in the play
 *
 */
public class Card implements ICard {

	private Variety variety;
	private int frequency;
	private ArrayList<Pair> bohnoMeter = new ArrayList<Pair>();
	private int cardID;

	/**
	 * Der Konstruktor der Klasse Card
	 *
	 * @param variety
	 *            Die Bohnensorte einer Karte.
	 *
	 * @param frequency
	 *            Die Anzahl der Karten einer Sorte im Spiel.
	 *
	 * @param bohnoMeter
	 *
	 *            Es zeigt an, wie viel Geld der Spieler bekommt, wenn er diese
	 *            Sorte ernt. Die Nummer zeigen an, wie viele Karten der Spieler
	 *            ernten soll, um dieses Geld zu bekommen.
	 *
	 * @param id
	 *            CardID jede Karte hat ihre eigene ID.
	 *
	 */
	public Card(Variety variety, int frequency, ArrayList<Pair> bohnoMeter, int id) {
		this.variety = variety;
		this.frequency = frequency;
		this.bohnoMeter = bohnoMeter;
		this.cardID = id;
	}

	/**
	 * Setter Das Value des Attributes Variety
	 *
	 * @param variety
	 *            Die Bohnensorte einer Karte.
	 */
	public void setVariety(Variety variety) {
		this.variety = variety;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ICard# getVariety()
	 */

	@Override
	public Variety getVariety() {
		return variety;
	}

	/**
	 * Setter des Attributes frequency
	 *
	 * @param frequency
	 *            Die Anzahl der Karten einer Sorte im Spiel.
	 *
	 *
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ICard#getFrequency()
	 */

	@Override
	public int getFrequency() {
		return frequency;
	}

	/**
	 * Setter des Attributes bohnoMeter *
	 *
	 * @param bohnoMeter
	 *            Es zeigt an, wie viel Geld der Spieler bekommt, wenn er diese
	 *            Sorte ernt. Die Nummer zeigen an, wie viele Karten der Spieler
	 *            ernten soll, um dieses Geld zu bekommen.
	 */
	public void setBohnoMeter(ArrayList<Pair> bohnoMeter) {
		this.bohnoMeter = bohnoMeter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ICard#getBohnoMeter()
	 */
	@Override
	public ArrayList<Pair> getBohnoMeter() throws RemoteException {
		return bohnoMeter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Server.ICard#getCardID()
	 */
	@Override
	public int getCardID() throws RemoteException {
		return cardID;
	}
}
