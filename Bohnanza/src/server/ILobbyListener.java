package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILobbyListener extends Remote {
	/**
	 * Shickt eine Banchrichtigung an alle angemeldeten Listener, wenn sich etwas in
	 * der Lobby aendert.
	 *
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public void notifyLobbyChange() throws RemoteException;
}
