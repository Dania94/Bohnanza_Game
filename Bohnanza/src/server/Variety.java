/**
 *
 */
package server;

/**
 * Diese Enum, Listet alle Bohnensorten auf, sie dient dazu eine einheitliche
 * Benennung der Bohnensorten zu gewährleisten. Durch die Methode asStr() kann
 * auf die String-Repräsentation der Sorte zugegriffen werden.
 */
public enum Variety {
	// Definiere alle Sorten als Konstante mit zugehöriger String-Repraesentation
	GARTENBOHNE("Gartenbohne"), ROTEBOHNE("RoteBohne"), AUGENBOHNE("Augenbohne"), SOJABOHNE("Sojabohne"), BRECHBOHNE(
			"Brechbohne"), SAUBOHNE("Saubohne"), FEUERBOHNE("Feuerbohne"), BLAUEBOHNE("BlaueBohne"), NULL("NOT A CARD");

	/**
	 * Konstruktor Wird automatisch bei jeder Verwendung eines Sorte-Objekts
	 * aufgerufen und sorgt dafuer das die jeweilige String-Repraesentation zur
	 * Verfuegung gestellt wird.
	 */
	private final String asStr;

	Variety(String st) {
		this.asStr = st;
	}

	/**
	 *
	 * @return String die Location des Image eine Karte.
	 */
	public String getImageLocation() {
		String location = "/" + asStr + ".png";
		return location;
	}

	/**
	 * Stellt eine Moeglichkeit zur Verfuegung die Bohnensorten als String zu
	 * verwenden.
	 *
	 * @return Gibt die String-Repräsentation des Sorte-Objekts zurueck
	 */
	public String toString() {
		return asStr;
	}

}
