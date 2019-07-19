package datenbank;

public class Account_score {

	String usename;
	int score;

	/**
	 *
	 * @param username
	 *            Der Spielername
	 * @param score
	 *           Die Punkte des Spielers
	 */
	public Account_score(String username, int score) {

		this.usename = username;
		this.score = score;
	}

	/**
	 * Gibt den Spielername zurueck.
	 * @return String der Spielername.
	 */
	public String getUsename() {
		return usename;
	}

	/**
	 * Set den Spielername.
	 * @param usename
	 *            Der Spielername.
	 */
	public void setUsename(String usename) {
		this.usename = usename;
	}

	/**
	 * Gibt die Punkte des Spielers zurueck.
	 * @return Die Punkte des Spielers.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Set Die Punkte des Spielers.
	 * @param score
	 *           Die Punkte des Spielers.
	 */
	public void setScore(int score) {
		this.score = score;
	}

}
