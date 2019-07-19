package server;

/**
 * Die Klasse Pair is eine HelferKlasse zur Repraesentation des BohnoMeters.
 *
 */
public class Pair {

	private int numberOfCoins;
	private int numberOfCards;

	/**
	 * Der Konstruktor der Klasse
	 *
	 * @param numberOfCoins
	 *            Die Anzahl der Bohnentaler, die der Spieler bekommt, wenn er x
	 *            Karten ernt.
	 *
	 * @param numberOfCards
	 *            Die Anzahl der Karten, die man ernten muss, um die entsprechende
	 *            Anzahl Taler zu bekommen.
	 */
	public Pair(int numberOfCoins, int numberOfCards) {
		this.numberOfCoins = numberOfCoins;
		this.numberOfCards = numberOfCards;
	}

	/**
	 * Setzt die Anzahl der Karten-
	 * 
	 * @param numberOfCards Anzahl der Karten
	 */
	public void setNumberOfCards(int numberOfCards) {
		this.numberOfCards = numberOfCards;
	}

	/**
	 * Gibt die Anzahl der Karten zurueck.
	 * 
	 * @return die Anzahl der Karten.
	 */
	public int getNumberOfCards() {
		return numberOfCards;
	}

	/**
	 * Setzt die Anzahl der Bohnentaler.
	 * 
	 * @param numberOfCoins Anzahl der Bohnentaler
	 */
	public void setNumberOfCoins(int numberOfCoins) {
		this.numberOfCoins = numberOfCoins;
	}

	/**
	 * Gibt die Anzahl der Bohnentaler zurueck.
	 * 
	 * @return die Anzahl der Bohnentaler.
	 */
	public int getNumberOfCoins() {
		return numberOfCoins;
	}
}
