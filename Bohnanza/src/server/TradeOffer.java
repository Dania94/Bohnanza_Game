package server;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * Hilfklasse, die ein Kartenangebot repräsentiert.
 *
 */
public class TradeOffer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String playerName;
	private ArrayList<String> varietyList = new ArrayList<String>();

	public TradeOffer(String playerName, ArrayList<String> varieties) {
		this.playerName = playerName;
		this.varietyList = varieties;
	}

	public String getPlayerName() {
		return playerName;
	}

	public ArrayList<String> getVarieties() {
		return varietyList;
	}

}
