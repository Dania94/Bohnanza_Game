package server.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;

import server.IGameRoom;

public interface IBot extends Remote {
	/**
	 * Setzt den Spielraum, in dem sich der Bot befindet.
	 * 
	 * @param room
	 *            der gegebene Spielraum
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void setGameRoom(IGameRoom room) throws RemoteException;
}