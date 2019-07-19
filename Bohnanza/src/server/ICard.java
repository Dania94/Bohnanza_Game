package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ICard extends Remote {

	/**
	 * Getter des Attributes frequency
	 *
	 * @return die Anzahl der Karten einer bestimmten Bohnensorte.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public int getFrequency() throws RemoteException;

	/**
	 * Getter des Attributes bohnoMeter.
	 *
	 * @return den BohnoMeter. Er zeigt an, wie viele Bohnenentaler pro Kartenanzahl
	 *         ein Spieler erhaelt, wenn er diese Bohnensorte erntet.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public ArrayList<Pair> getBohnoMeter() throws RemoteException;

	/**
	 * Getter des Attributs variety.
	 *
	 * @return die Bohnensorte dieser Karte.
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public Variety getVariety() throws RemoteException;

	/**
	 * Getter des Attributes CardID
	 *
	 * @return die Karten-ID
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public int getCardID() throws RemoteException;
}
