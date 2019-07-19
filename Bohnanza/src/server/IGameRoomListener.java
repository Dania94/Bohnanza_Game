package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGameRoomListener extends Remote {
	/**
	 * Shickt eine Benachrichtigung an alle angemeldeten Listener, wenn sich etwas im
	 * Spielraum aendert.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void notifyGameRoomChange() throws RemoteException;
}
