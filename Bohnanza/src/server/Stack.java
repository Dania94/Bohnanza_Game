package server;

import java.util.ArrayList;
import java.util.Collections;

public class Stack {

	private ArrayList<Card> cards = new ArrayList<Card>();
	private ArrayList<Pair> augenbohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> gartenbohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> sojabohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> brechbohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> saubohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> feuerbohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> blauebohneBohnoMeter = new ArrayList<Pair>();
	private ArrayList<Pair> rotebohneBohnoMeter = new ArrayList<Pair>();

	/**
	 * Der Kostruktor der Klasse Stack.
	 */
	public Stack() {
		setBohnoMeterLists();
	}

	/**
	 * Der Kostruktor der Klasse Stack.
	 * 
	 * @param cardList
	 *            die Karten des Stapels.
	 */
	public Stack(ArrayList<Card> cardList) {
		setBohnoMeterLists();
		this.cards = cardList;
	}

	/**
	 * Setzt die Bohnometer für alle Bohnensorten.
	 *
	 */
	private final void setBohnoMeterLists() {
		augenbohneBohnoMeter.add(new Pair(1, 2));
		augenbohneBohnoMeter.add(new Pair(2, 4));
		augenbohneBohnoMeter.add(new Pair(3, 5));
		augenbohneBohnoMeter.add(new Pair(4, 6));

		gartenbohneBohnoMeter.add(new Pair(2, 2));
		gartenbohneBohnoMeter.add(new Pair(3, 3));

		rotebohneBohnoMeter.add(new Pair(1, 2));
		rotebohneBohnoMeter.add(new Pair(2, 3));
		rotebohneBohnoMeter.add(new Pair(3, 4));
		rotebohneBohnoMeter.add(new Pair(4, 5));

		sojabohneBohnoMeter.add(new Pair(1, 2));
		sojabohneBohnoMeter.add(new Pair(2, 4));
		sojabohneBohnoMeter.add(new Pair(3, 6));
		sojabohneBohnoMeter.add(new Pair(4, 7));

		brechbohneBohnoMeter.add(new Pair(1, 3));
		brechbohneBohnoMeter.add(new Pair(2, 5));
		brechbohneBohnoMeter.add(new Pair(3, 6));
		brechbohneBohnoMeter.add(new Pair(4, 7));

		saubohneBohnoMeter.add(new Pair(1, 3));
		saubohneBohnoMeter.add(new Pair(2, 5));
		saubohneBohnoMeter.add(new Pair(3, 7));
		saubohneBohnoMeter.add(new Pair(4, 8));

		feuerbohneBohnoMeter.add(new Pair(1, 3));
		feuerbohneBohnoMeter.add(new Pair(2, 6));
		feuerbohneBohnoMeter.add(new Pair(3, 8));
		feuerbohneBohnoMeter.add(new Pair(4, 9));

		blauebohneBohnoMeter.add(new Pair(1, 4));
		blauebohneBohnoMeter.add(new Pair(2, 6));
		blauebohneBohnoMeter.add(new Pair(3, 8));
		blauebohneBohnoMeter.add(new Pair(4, 10));
	}

	/**
	 *
	 * Wenn der Stapel erstellt wird, fulle ihn mit der Karten des Spiels Bohnzana.
	 */
	public void fillInitialStack() {
		refillVariety(10, Variety.AUGENBOHNE, augenbohneBohnoMeter);
		refillVariety(20, Variety.BLAUEBOHNE, blauebohneBohnoMeter);
		refillVariety(14, Variety.BRECHBOHNE, brechbohneBohnoMeter);
		refillVariety(18, Variety.FEUERBOHNE, feuerbohneBohnoMeter);
		refillVariety(6, Variety.GARTENBOHNE, gartenbohneBohnoMeter);
		refillVariety(8, Variety.ROTEBOHNE, rotebohneBohnoMeter);
		refillVariety(16, Variety.SAUBOHNE, saubohneBohnoMeter);
		refillVariety(12, Variety.SOJABOHNE, sojabohneBohnoMeter);
	}

	/**
	 *
	 * @param index
	 *            die Anzahl der Karten einer Sorte im Stapel
	 * @param variety
	 *            Die Bohnensorte.
	 * @param bohnoMeter
	 *
	 *            Es zeigt an, wie viel Geld der Spieler bekommt, wenn er diese
	 *            Sorte ernt. Die Nummer zeigen an, wie viele Karten der Spieler
	 *            ernten soll, um dieses Geld zu bekommen.
	 *
	 */
	private void refillVariety(int index, Variety variety, ArrayList<Pair> bohnoMeter) {
		for (int i = 0; i < index; i++) {
			cards.add(new Card(variety, index, bohnoMeter, i));
		}
	}

	/**
	 * mischen die Karten des Stapels.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * fuege eine Karte im Stapel
	 * 
	 * @param card
	 *            die Karte, die im Stapel eingefuegt werden soll.
	 */
	public void addCard(Card card) {
		cards.add(card);
	}

	/**
	 * Gibt die erste Karte vom Stapel zurueck.
	 * 
	 * @return Die erste Karte von Stapel.
	 */
	public Card drawCard() {
		if (this.getNumberOfCards() > 0) {
			Card drawnCard = cards.get(0);
			cards.remove(0);
			return drawnCard;
		} else {
			return null;
		}
	}

	/**
	 * Gibt die Anzahl der Karten im Stapel zurueck.
	 * 
	 * @return die Anzahl der Karten im Stapel.
	 */
	public int getNumberOfCards() {
		return cards.size();
	}
}
