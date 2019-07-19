package server;

import java.util.Date;

/**
 * Die Klasse Message repäsentiert vom Nutzer verfasste Nachrichten im Chat.
 *
 *
 */
public class Message {

	private String username;
	private String msg;
	private String type;
	private Date date;

	/**
	 * * Der Konstuktor die Klasse Message.
	 */
	public Message() {
	}

	/**
	 * Der Konstuktor die Klasse Message.
	 *
	 * @param msgInput
	 *            die Nachricht, die geschickt wird.
	 * @param sender
	 *            der Sender der Nachrict.
	 * @param type
	 *            der Type dieser Nachricht.
	 * @param date
	 *            das Datum, in dem die Nachricht geschickt wird.
	 */
	public Message(String msgInput, String sender, String type, Date date) {
		this.msg = msgInput;
		this.username = sender;
		this.type = type;
		this.date = date;
	}

	/**
	 * Getter des Attributes date.
	 *
	 * @return Date das Datum, in dem die Nachricht geschickt wird.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setter des Attributes date.
	 *
	 * @param date
	 *            das Datum, an dem die Nachricht geschickt wird.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Getter des Attributes type.
	 *
	 * @return String type der Type dieser Nachricht.
	 **/
	public String getType() {
		return type;
	}

	/**
	 * Setter des Attributes type.
	 *
	 * @param type
	 *            der Type dieser Nachricht.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter des Attributes username.
	 *
	 * @return String der Spielername, der die Nachricht geschickt hat.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter des Attributes username.
	 *
	 * @param username
	 *            der Spielername, der die Nachricht geschickt hat.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter des Attributes msg.
	 *
	 * @return die geschickte Nachricht.
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Setter des Attributes msg.
	 *
	 * @param msg
	 *            die geschickte Nachricht.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
