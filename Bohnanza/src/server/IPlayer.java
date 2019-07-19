package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IPlayer extends Remote {
	/**
	 * Gibt die Anzahl der Taler zurueck.
	 *
	 * @return die Anzahl der Taler des Spielers.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int getTaler() throws RemoteException;

	/**
	 * Gibt der Spielername zurueck.
	 *
	 * @return String der Spielername.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getPlayerName() throws RemoteException;

	/**
	 * Gibt die Anzahl der Taler zurueck.
	 *
	 * @return String die Anzahl der Taler des Spielers.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getTalerString() throws RemoteException;

	/**
	 * Gibt der Bohnenfeld des Spielers zurueck.
	 *
	 * @param index
	 *            der Index des Bohnenfelds.
	 * @return String der Bohnenfeld des Spielers.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public String getBeanfieldString(int index) throws RemoteException;

	/**
	 * Gibt die Bohnensorte der Karte zurueck.
	 *
	 * @return Die Sorte einer Karte in der Spielerhand.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public List<String> getHandCardVarieties() throws RemoteException;

	/**
	 * Gibt die Anzahl der Felder des Spielers zurueck.
	 *
	 * @return die Anzahl der Felder des Spielers.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int getNumberOfBeanfields() throws RemoteException;

	/**
	 *
	 * @param index
	 *            der Index des BohnenFelds.
	 * @return int die Anzahl der Karten in einem Bohnenfeld.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int getBeanfieldCardCount(int index) throws RemoteException;

	/**
	 *
	 * @return die Anzahl der Karten in der Spielerhand.
	 * @throws RemoteException
	 *             RMI Exception.
	 */
	public int getCardCount() throws RemoteException;
}
