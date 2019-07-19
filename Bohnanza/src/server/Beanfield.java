package server;

import java.util.ArrayList;

public class Beanfield {

	private ArrayList<Card> cards;

	/**
	 * Der Konstruktor der Klasse Beanfield
	 */
	public Beanfield() {
		this.cards = new ArrayList<Card>();
	}

	/**
	 * Die Methode gibt die Anzahl der Karten auf dem Bohnenfeld zurueck.
	 *
	 * @return the number of cards in this field
	 */
	public int getCardCount() {
		return cards.size();
	}

	/**
	 * Die Methode erntet die Karten auf einem Bohnenfeld und gibt sie zurueck.
	 *
	 * @return die Liste der von einem Feld geernteten Karten.
	 *
	 */
	public ArrayList<Card> harvestCards() {
		ArrayList<Card> harvestedCards = cards;
		cards = new ArrayList<Card>();
		return harvestedCards;
	}

	/**
	 * Diese Methode baut eine Karte  auf einem Bohnenfeld an.
	 *
	 * @param card
	 *            die Karte, die der Spieler auf dem Bohnenfeld anbauen will.
	 */
	public void addCard(Card card) {
		if (cards.size() == 0) {
			cards.add(card);
		} else if (cards.get(0).getVariety().equals(card.getVariety())) {
			cards.add(card);
		} else {
			System.out.println("Error: Wrong Card (has wrong Variety)!");
			return;
		}
	}

	/**
	 * Die Liste der Karten im Bohnenfeld.
	 *
	 * @return die Liste der Karten.
	 */
	public ArrayList<Card> getCards() {
		return cards;
	}

	/**
	 * Die Methode gibt Sorte des Bohnofelds zurueck.
	 *
	 * @return Variety
	 */
	public Variety getFieldVariety() {
		if (cards.size() == 0) {
			return Variety.NULL;
		}
		return cards.get(0).getVariety();
	}

}
