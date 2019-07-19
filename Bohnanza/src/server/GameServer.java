package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {

	/**
	 * Die Mainmethode des Servers.
	 *
	 * @param args
	 *            main Attributt .
	 */
	public static void main(String[] args) {

		establishConnection(1099);

	}

	/**
	 * Die Funktion öffnet eine Verbindung.
	 *
	 * @param port
	 *            der Port des RMI Servers.
	 */
	public static void establishConnection(int port) {
		try {
			System.out.println("Starting Server");
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind("gameServer", new Lobby());
			System.out.println("Server Started");
		} catch (RemoteException ex) {
			Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
