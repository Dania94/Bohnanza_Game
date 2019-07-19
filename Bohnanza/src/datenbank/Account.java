package datenbank;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * Die Datenbank Klasse.
 *
 */
public class Account {

	/**
	 * Verbindung zur Datenbank.
	 *
	 * @return Gibt die Verbindung zurueck.
	 */
	public Connection connect() {
		String url = "jdbc:sqlite:BohnanzaDB";
		Connection c = null;

		try {
			c = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return c;
	}

	/**
	 * Initialisiere die Datenbank
	 */
	public void makeTable() {
		Connection c = null;
		Statement stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:BohnanzaDB");
			stmt = c.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS Bohnanza "
					+ "(SPIELERNAME           TEXT PRIMARY KEY    NOT NULL, "
					+ " SPIELERPASSWORT       TEXT                NOT NULL, "
					+ " SPIELERPUNKTE         INT                 NOT NULL )";

			stmt.executeUpdate(sql);
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Test, ob die Tabelle in der Datenbankt leer ist.
	 * 
	 * @return boolean die Resultat des Tests.
	 */
	public boolean isTableEmpty() {
		String sql = "SELECT COUNT(*) FROM BOHNANZA";
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			if (rs.getInt(1) == 0) {
				System.out.println("Tabelle enthält noch keine Einträge!");
				return true;
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Testet, ob der Spieler in der Datenbank eingefuegt wurd.
	 * 
	 * @param SPIELERNAME
	 *            Der Spielername
	 * @param SPIELERPASSWORT
	 *            Das Spielerpasswort
	 * @param SPIELERPUNKTE
	 *            Die Punkte des Spielers
	 * @return boolean.
	 */

	public boolean insertNewUser(String SPIELERNAME, String SPIELERPASSWORT, int SPIELERPUNKTE) {

		String sql = "INSERT INTO Bohnanza(SPIELERNAME, SPIELERPASSWORT, SPIELERPUNKTE ) VALUES(?,?,?)";

		try (Connection c = this.connect(); PreparedStatement pstmt = c.prepareStatement(sql)) {
			pstmt.setString(1, SPIELERNAME);
			pstmt.setString(2, SPIELERPASSWORT);
			pstmt.setInt(3, SPIELERPUNKTE);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Testet, ob der Spieler schon ein Account in der Datenbank hat.
	 * 
	 * @param name
	 *            Der Spielername.
	 * @return boolean.
	 */

	public boolean isUserInDB(String name) {

		String sql = "SELECT SPIELERNAME FROM Bohnanza";

		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				if (rs.getString("SPIELERNAME").equals(name)) {
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Loesche der Spieler von der Datenbank.
	 *
	 * @param name
	 *            Spielername.
	 */

	public void deleteUser(String name) {
		String sql = "DELETE FROM Bohnanza WHERE SPIELERNAME = ?";

		try (Connection c = this.connect(); PreparedStatement pstmt = c.prepareStatement(sql)) {

			pstmt.setString(1, name);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Updaten des Passwort in der Datenbank.
	 *
	 * @param name
	 *            Der Spielername
	 * @param newPswd
	 *            das neues Passwort.
	 */

	public void updateUserPswd(String name, String newPswd) {
		String sql = "UPDATE Bohnanza SET SPIELERPASSWORT = ?" + "WHERE SPIELERNAME = ?";

		try (Connection c = this.connect(); PreparedStatement pstmt = c.prepareStatement(sql)) {

			pstmt.setString(1, newPswd);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Gibt das Passwort des Spieler zurueck.
	 *
	 * @param name
	 *            der Spielername.
	 * @return Das Passwort des Spielers.
	 */
	public String getUserPswd(String name) {

		String sql = "SELECT SPIELERNAME, SPIELERPASSWORT FROM Bohnanza";

		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				if (rs.getString("SPIELERNAME").equals(name)) {
					String rsPswd = rs.getString(2);
					return rsPswd;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * Updated die Punkte des Spielers in der Datenbank.
	 * 
	 * @param name
	 *            Spielername.
	 * @param neuePunkte
	 *            die neue Punkte.
	 */

	public void updateUserPunkte(String name, int neuePunkte) {
		String sql = "UPDATE Bohnanza SET SPIELERPUNKTE = ?" + "WHERE SPIELERNAME = ?";

		try (Connection c = this.connect(); PreparedStatement pstmt = c.prepareStatement(sql)) {

			pstmt.setInt(1, neuePunkte);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Gibt die Anzahl der Punkte in der Datenbank.
	 *
	 * @param name
	 *            der Spielername.
	 * @return die Punkte des Spielers.
	 */

	public int getUserPunkte(String name) {

		String sql = "SELECT SPIELERNAME, SPIELERPUNKTE FROM Bohnanza";

		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				if (rs.getString("SPIELERNAME").equals(name)) {
					int rsPunkte = rs.getInt(3);
					return rsPunkte;
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/**
	 * Gibt die besten 10 Spieler zurueck.
	 * 
	 * @return die Liste der Spieler mit den meisten Punkten.
	 */
	public ArrayList<Account_score> bestenListe() {
		String sql = "SELECT COUNT(*) FROM BOHNANZA";
		ArrayList<Account_score> bestenliste = new ArrayList<Account_score>();
		try (Connection c = this.connect();
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.getInt(1) > 10) {
				String sqlOrder = "SELECT * FROM Bohnanza ORDER BY SPIELERPUNKTE DESC LIMIT 10";

				Statement stmtOrder = c.createStatement();
				ResultSet rsOrder = stmtOrder.executeQuery(sqlOrder);

				while (rsOrder.next()) {
					System.out.println(rsOrder.getString(1) + "   " + rsOrder.getInt(3));
					bestenliste.add(new Account_score(rsOrder.getString(1), rsOrder.getInt(3)));
				}

			} else {
				String sqlOrder = "SELECT * FROM Bohnanza ORDER BY SPIELERPUNKTE DESC";

				Statement stmtOrder = c.createStatement();
				ResultSet rsOrder = stmtOrder.executeQuery(sqlOrder);

				while (rsOrder.next()) {
					System.out.println(rsOrder.getString(1) + "   " + rsOrder.getInt(3));
					bestenliste.add(new Account_score(rsOrder.getString(1), rsOrder.getInt(3)));
				}
			}
			return bestenliste;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

}
