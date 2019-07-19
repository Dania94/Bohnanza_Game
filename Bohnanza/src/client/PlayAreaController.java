package client;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import server.GameRoomState;
import server.IGameRoom;
import server.IGameRoomListener;
import server.ILobby;
import server.IPlayer;
import server.TradeOffer;

public class PlayAreaController extends UnicastRemoteObject implements SetterInterface, IGameRoomListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private FinalizeTradeDialog finalizeTradeDialog;
	private AnswerTradeProposalDialog proposalDialog;

	/**
	 * Der Konstruktor der Klasse {@link PlayAreaController}
	 * 
	 * @throws RemoteException
	 *             RMI Exception
	 */
	public PlayAreaController() throws RemoteException {
		super();
	}

	@FXML
	private AnchorPane playArea;

	@FXML
	private Button nextPhaseButton;

	@FXML
	private TextField taler;

	@FXML
	private ImageView beanfield1;

	@FXML
	private Button beanfield1Button;

	@FXML
	private ImageView beanfield2;

	@FXML
	private Button beanfield2Button;

	@FXML
	private Button tradeButton;

	@FXML
	private ImageView beanfield3;

	@FXML
	private Button beanfield3Button;

	@FXML
	private Button exitButton;

	@FXML
	private Label whatIsCurrentlyHappening;

	/**
	 * Wird aufgerufen, wenn das Bohnenfeld 1 geklickt wird.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	private void beanfield1Clicked(ActionEvent event) throws RemoteException {
		if (gameroom.getGamePhase() == 3) {
			int receivedCardsIndex = handcardsListView.getSelectedIndex();
			gameroom.playReceivedCards(client.getUsername(), receivedCardsIndex, 0);
		} else {
			gameroom.playHandCard(client.getUsername(), 0);
		}

		System.out.println("Beanfield1 clicked!");
	}

	/**
	 * Wird aufgerufen, wenn das Bohnenfeld 2 geklickt wird.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	private void beanfield2Clicked(ActionEvent event) throws RemoteException {
		if (gameroom.getGamePhase() == 3) {
			int receivedCardsIndex = handcardsListView.getSelectedIndex();
			gameroom.playReceivedCards(client.getUsername(), receivedCardsIndex, 1);
		} else {
			gameroom.playHandCard(client.getUsername(), 1);
		}

		System.out.println("Beanfield2 clicked!");
	}

	/**
	 * Wird aufgerufen, wenn das Bohnenfeld 3 geklickt wird.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	private void beanfield3Clicked(ActionEvent event) throws RemoteException {
		if (gameroom.getGamePhase() == 3) {
			int receivedCardsIndex = handcardsListView.getSelectedIndex();
			gameroom.playReceivedCards(client.getUsername(), receivedCardsIndex, 2);
		} else {
			gameroom.playHandCard(client.getUsername(), 2);
		}

		System.out.println("Beanfield3 clicked!");
	}

	/**
	 * Öffnet das Handelsfenster, falls der Spieler den Button "Handeln" geklickt
	 * hat.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	private void tradeButtonClicked(ActionEvent event) throws RemoteException {
		if (gameroom.getGamePhase() != 2) {
			return;
		}
		OfferTradeDialog offerTrade = new OfferTradeDialog();

		String phase2Card1 = gameroom.getVarietyForPhase2Cards(0).toString();
		String phase2Card2 = gameroom.getVarietyForPhase2Cards(1).toString();

		offerTrade.setDrawCard1(phase2Card1);
		offerTrade.setDrawCard2(phase2Card2);

		List<String> handCardVarieties = gameroom.getPlayer(client.getUsername()).getHandCardVarieties();
		offerTrade.getSelectableCardsListView().setHandcards(handCardVarieties);

		Optional<ButtonType> optional = offerTrade.showAndWait();

		if (!OfferTradeDialog.isSuccess(optional)) {
			return;
		}

		boolean card1Selected = offerTrade.isDrawCard1Selected();
		System.out.println("Draw card 1 selected: " + card1Selected);
		boolean card2Selected = offerTrade.isDrawCard2Selected();
		System.out.println("Draw card 2 selected: " + card2Selected);

		ArrayList<Integer> selectedElementIndices = offerTrade.getSelectableCardsListView().getSelectedElementIndices();

		gameroom.initiateTrading(card1Selected, card2Selected, selectedElementIndices);
	}

	private PlayerInfoControl[] playerInfos;

	private HandcardsListView handcardsListView;
	private ILobby lobby;
	private Updater updater;
	private Client client;
	private IGameRoom gameroom;

	private ChatController chat;

	/**
	 * Aktualisiert die Daten des Spielers.
	 * 
	 * @param ourPlayer
	 * @throws RemoteException
	 */
	private void updateOurPlayerInfo(IPlayer ourPlayer) throws RemoteException {

		int anzahlTaler = ourPlayer.getTaler();
		taler.setText(String.valueOf(anzahlTaler));

		updateImage(ourPlayer.getBeanfieldString(0), beanfield1);
		int cardCount1 = ourPlayer.getBeanfieldCardCount(0);
		// System.out.println("CardCount1: " + cardCount1);
		beanfield1Button.setText("" + cardCount1);

		updateImage(ourPlayer.getBeanfieldString(1), beanfield2);
		int cardCount2 = ourPlayer.getBeanfieldCardCount(1);
		beanfield2Button.setText(String.valueOf(cardCount2));

		if (gameroom.getPlayerCount() == 3) {
			updateImage(ourPlayer.getBeanfieldString(2), beanfield3);
			int cardCount3 = ourPlayer.getBeanfieldCardCount(2);
			beanfield3Button.setText(String.valueOf(cardCount3));
		}
	}

	/**
	 * Ändere das Bild des Bohnenfeld.
	 * 
	 * @param variety
	 *            Die Sorte des Bohnenfelds.
	 * @param beanfield
	 *            Der Bohnenfeld.
	 */
	private void updateImage(String variety, ImageView beanfield) {
		File file = new File(variety + ".png");

		if (file.exists()) {
			Image image = new Image("file:" + file.getAbsolutePath());
			beanfield.setImage(image);
		}
	}

	/**
	 *
	 * Wenn der Button "Anleitung" geklickt wird, öffnet sich die Spielanleitung.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void instructionsButtonClicked(ActionEvent event) throws IOException {
		// Datei, die im Standard-Texteditor geöffnet wird
		File file = new File("instruction_1.png");
		File file1 = new File("instruction_2.png");
		// überprüfe, ob der verwendete Desktop unterstützt wird
		if (!Desktop.isDesktopSupported()) {
			System.out.println("Desktop is not supported");
			return;
		}
		Desktop desktop = Desktop.getDesktop();
		if (file.exists())
			desktop.open(file);
		if (file1.exists())
			desktop.open(file1);
	}

	/**
	 * Wenn der Button "Bestenliste" geklickt wird, zeigt das Spiel die aktuelle
	 * Bestenliste an.
	 * 
	 * @param event
	 *
	 */
	@FXML
	void rankingButtonClicked(ActionEvent event) throws RemoteException {
		updater.guiChange(Updater.SHOW_RANKING);
	}

	/**
	 * Wenn dieser Button geklickt wird, wechselt das Spiel in die nächste
	 * Spielphase.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void nextPhaseButtonClicked(ActionEvent event) throws RemoteException {
		gameroom.nextGamePhase();
	}

	/**
	 * Wenn dieser Button geklickt wird, verlässt der Spieler das Spiel und kehrt in
	 * die Lobby zurück.
	 * 
	 * @param event
	 * @throws RemoteException
	 */
	@FXML
	void exitButtonClicked(ActionEvent event) throws RemoteException {
		gameroom.exitGame(client.getUsername());
		lobby.deleteRoom(client.getUsername());
	}

	private Label whatIsHandCardBoxCurrentlyShowing;

	/**
	 * Diese Methode initialisiert die FXML-Datei PlayArea.fxml
	 * 
	 * @throws RemoteException
	 */
	@FXML
	void initialize() throws RemoteException {
		System.out.println("PlayAreaController initialized!");

		VBox handCardBox = new VBox();

		whatIsHandCardBoxCurrentlyShowing = new Label();
		handCardBox.getChildren().add(whatIsHandCardBoxCurrentlyShowing);

		handcardsListView = new HandcardsListView();
		handCardBox.getChildren().add(handcardsListView);

		handCardBox.setLayoutX(500);
		handCardBox.setLayoutY(600);
		playArea.getChildren().add(handCardBox);

		chat = new ChatController();
		chat.setLayoutX(600.0);
		chat.setLayoutY(300.0);
		playArea.getChildren().add(chat);
	}

	/**
	 * Erstellt die Bereiche, in denen Informationen zu den Spielern angezeigt
	 * werden.
	 * 
	 * @throws RemoteException
	 */
	private void createPlayerInfoControls() throws RemoteException {
		int numPlayers = gameroom.getPlayerNames().size();

		int numOtherPlayers = numPlayers - 1;

		playerInfos = new PlayerInfoControl[numOtherPlayers];

		for (int i = 0; i < numOtherPlayers; i++) {
			PlayerInfoControl playerInfoControl = new PlayerInfoControl();
			playerInfoControl.setLayoutX(32 + i * 290);
			playerInfoControl.setLayoutY(32);
			playArea.getChildren().add(playerInfoControl);
			playerInfos[i] = playerInfoControl;
		}
	}

	/**
	 * Hilfsmethode zum Aktualisieren der Informationen für die richtige Anzahl von
	 * Spielern.
	 * 
	 * @param player
	 *            der Spieler
	 * @param index
	 *            Index der PlayerInfos.
	 * @param threePlayers
	 *            wahr, falls es im Raum 3 Spieler gibt
	 * @throws RemoteException
	 */
	private void setPlayerInfoControl(IPlayer player, int index, boolean threePlayers) throws RemoteException {
		if (threePlayers) {
			playerInfos[index].setPlayerName(player.getPlayerName());
			playerInfos[index].setTaler(player.getTalerString());
			playerInfos[index].setBeanfield1(player.getBeanfieldString(0));
			playerInfos[index].setBeanfield2(player.getBeanfieldString(1));
			playerInfos[index].setBeanfield3(player.getBeanfieldString(2));
		} else {
			playerInfos[index].setPlayerName(player.getPlayerName());
			playerInfos[index].setTaler(player.getTalerString());
			playerInfos[index].setBeanfield1(player.getBeanfieldString(0));
			playerInfos[index].setBeanfield2(player.getBeanfieldString(1));
		}
	}

	/**
	 * Aktualisiere die vom Server gesendeten Daten.
	 * 
	 * @throws RemoteException
	 */
	private void updateServerData() throws RemoteException {
		updatePlayerInfoControls();
		updateHandcardsListView();
		updateCentralStatusView();

		// Im Chat angezeigte Nachrichten updaten.
		String messageString = gameroom.getRoomMessages(client.getUsername());
		chat.setMessages(messageString);

		String activePlayer = gameroom.getActivePlayerName();
		if (activePlayer.equals(client.getUsername())) {
			enableButtons(true);
			showFinalizeTradeDialog();
		} else {
			enableButtons(false);
			showTradeOfferDialog();
		}
	}

	private void showTradeOfferDialog() throws RemoteException {
		if (proposalDialog != null) {
			return;
		}

		if (gameroom.getGamePhase() == 2) {
			ArrayList<String> tradeOffer = gameroom.getTradeOffer(client.getUsername());
			if (tradeOffer != null) {

				System.out.println("Got tradeOffer: "+tradeOffer.size());
				for(String str : tradeOffer) {
					System.out.println("TradeOfferCard: "+str);
				}
				proposalDialog = new AnswerTradeProposalDialog();
				proposalDialog.getTradeHandcardsListView().setHandcards(tradeOffer);
				
				List<String> handCardVarieties = gameroom.getPlayer(client.getUsername()).getHandCardVarieties();
				proposalDialog.getYourHandcardsListView().setHandcards(handCardVarieties);

				Optional<ButtonType> result = proposalDialog.showAndWait();

				if (AnswerTradeProposalDialog.isSuccess(result)) {
					ArrayList<Integer> selectedHandcardIndices = proposalDialog.getYourHandcardsListView()
							.getSelectedElementIndices();
					gameroom.answerTradeOffer(client.getUsername(), selectedHandcardIndices);
				} else {
					gameroom.answerTradeOffer(client.getUsername(), null);
				}
				proposalDialog = null;
			}
		}
	}

	private void showFinalizeTradeDialog() throws RemoteException {
		if (finalizeTradeDialog != null) {
			return;
		}

		ArrayList<TradeOffer> tradeOffers = gameroom.getAllTradeOffers(client.getUsername());
		if (tradeOffers != null) {
			
			// Warten, bis jeder sein Angebot abgegeben hat (oder abgelehnt hat).
			int otherPlayers = gameroom.getPlayerCount()-1;
			if(tradeOffers.size()!=otherPlayers) {
				return;
			}
			
			finalizeTradeDialog = new FinalizeTradeDialog();

			ArrayList<String> offeredCards = gameroom.getMyOffer(client.getUsername());
			finalizeTradeDialog.getYourHandcardsListView().setHandcards(offeredCards);

			finalizeTradeDialog.getSelectableTradeAnswersListView().setTradeOffers(tradeOffers);

			Optional<ButtonType> optional = finalizeTradeDialog.showAndWait();

			if (OfferTradeDialog.isSuccess(optional)) {
				System.out.println("Success!");
				String playerName = finalizeTradeDialog.getSelectableTradeAnswersListView()
						.getPlayerNameForSelectedTradeOffer();
				System.out.println("You selected the offer for player: " + playerName);
				gameroom.acceptDeal(client.getUsername(), playerName);
			} else {
				System.out.println("Cancel!");
				gameroom.declineAllDeals();
			}
			finalizeTradeDialog = null;
		}
	}

	/**
	 * Aktualisiert die Anzeige, welcher Spieler am Zug ist und in welcher
	 * Spielphase sich das Spiel befindet.
	 * 
	 * @throws RemoteException
	 */
	private void updateCentralStatusView() throws RemoteException {
		String activePlayerName = gameroom.getActivePlayerName();
		if (activePlayerName.equals(client.getUsername())) {
			activePlayerName = "Du";
		}

		int gamePhase = gameroom.getGamePhase();

		String status = "Aktuell am Zug: " + activePlayerName + ", Phase " + gamePhase;
		whatIsCurrentlyHappening.setText(status);
	}

	/**
	 * Aktualisiere die Ansicht der Handkarten.
	 * 
	 * @throws RemoteException
	 */
	private void updateHandcardsListView() throws RemoteException {

		if (gameroom.getGamePhase() == 3 && gameroom.getActivePlayerName().equals(client.getUsername())) {
			whatIsHandCardBoxCurrentlyShowing.setText("Deine getauschten und gezogenen Karten:");

		} else {
			whatIsHandCardBoxCurrentlyShowing.setText("Deine Handkarten:");
		}

		// Falls wir die getauschten und gezogenen Handkarten verteilen müssen...
		if (gameroom.getGamePhase() == 3 && gameroom.getActivePlayerName().equals(client.getUsername())) {
			// Setze den HandkartenView auf die zu verteilenden Karten.
			ArrayList<String> zuVerteilendeKarten = gameroom.getCardsToPlant();
			handcardsListView.setHandcards(zuVerteilendeKarten);
			return;
		}

		// Sonst zeige die normalen Handkarten.
		List<String> handCardVarieties = gameroom.getPlayer(client.getUsername()).getHandCardVarieties();
		handcardsListView.setHandcards(handCardVarieties);
	}

	/**
	 * Ändert den Status eines Buttons (aktiv oder passiv).
	 * 
	 * @param enabled
	 */
	private void enableButtons(boolean enabled) {
		boolean disable = !enabled;
		nextPhaseButton.setDisable(disable);
		beanfield1Button.setDisable(disable);
		beanfield2Button.setDisable(disable);
		beanfield3Button.setDisable(disable);
		handcardsListView.setDisable(disable);
	}

	/**
	 * Aktualisiert die in den PlayerInfoControls angezeigten Informationen mit
	 * Informationen, die vom Server bereitgestellt werden.
	 *
	 * @throws RemoteException
	 */
	private void updatePlayerInfoControls() throws RemoteException {
		int index = 0;
		if (gameroom.getPlayerNames().size() > 3) {
			for (IPlayer player : gameroom.getAllPlayers()) {
				if (player.getPlayerName().equals(client.getUsername())) {
					updateOurPlayerInfo(player);
				} else {
					setPlayerInfoControl(player, index, false);
					index++;
				}
			}
		} else {
			for (IPlayer player : gameroom.getAllPlayers()) {
				if (player.getPlayerName().equals(client.getUsername())) {
					updateOurPlayerInfo(player);
				} else {
					setPlayerInfoControl(player, index, true);
					index++;
				}
			}
		}
	}

	/**
	 * Setzt Client, Lobby, Spielraum und Updater.
	 */
	@Override
	public void setClient(Client client) {
		this.lobby = client.getLobby();
		this.updater = client.getUpdater();
		this.client = client;
		chat.setClient(client);
		try {
			this.gameroom = lobby.getGameRoom(client.getGameRoom());
			gameroom.setGameRoomListener(this);
			createPlayerInfoControls();
			updateServerData();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Diese Methode wird aus geführt, wenn der Listener über Änderungen im
	 * Spielraum informiert wurde und aktualisiert die Anzeige entsprechend.
	 */
	@Override
	public void notifyGameRoomChange() throws RemoteException {

		if (gameroom.getGameRoomState() == GameRoomState.GAME_ENDED) {
			PlayAreaController listener = this;

			Platform.runLater(new Runnable() {
				public void run() {
					try {
						boolean success = gameroom.removeGameRoomListener(listener);
						if (success) {

							GameEndedDialog gameEndedDialog = new GameEndedDialog();

							String reason = gameroom.getReasonGameEnded();
							gameEndedDialog.setGameEndedReason(reason);

							HashMap<String, Integer> punkte = gameroom.getPlayerPoints();
							gameEndedDialog.setPunkte(punkte);

							Optional<ButtonType> optional = gameEndedDialog.showAndWait();

							updater.guiChange(exitButton, Updater.LOBBY);
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			});
		}
		Platform.runLater(new Runnable() {
			public void run() {
				try {
					updateServerData();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
