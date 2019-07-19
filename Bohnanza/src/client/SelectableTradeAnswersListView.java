package client;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import server.TradeOffer;

public class SelectableTradeAnswersListView extends ScrollPane {

	public static final int HANDCARDS_HEIGHT = 150;

	private VBox contentBox = new VBox();

	private HashMap<ToggleButton, String> playerLookup = new HashMap<>();

	private ToggleGroup toggleGroup;

	public SelectableTradeAnswersListView() {
		// Add box containing the content to this scroll pane.
		setContent(contentBox);

		setPrefHeight(2 * HANDCARDS_HEIGHT + 30);
		setPrefWidth(400);
	}

	private ToggleButton createToggleButton(TradeOffer element) {

		VBox offerBox = new VBox();

		offerBox.getChildren().add(new Label(element.getPlayerName()));

		SelectableCardsListView listView = new SelectableCardsListView();
		listView.setHandcards(element.getVarieties());
		listView.setDisable(true);
		offerBox.getChildren().add(listView);

		ToggleButton toggleButton = new ToggleButton();
		toggleButton.setToggleGroup(toggleGroup);
		toggleButton.setGraphic(offerBox);

		return toggleButton;
	}

	public void setTradeOffers(ArrayList<TradeOffer> tradeOffers) {
		contentBox.getChildren().clear();
		playerLookup.clear();
		toggleGroup = new ToggleGroup();
		for (TradeOffer tradeOffer : tradeOffers) {
			if (tradeOffer.getVarieties() != null) {
				ToggleButton toggleButton = createToggleButton(tradeOffer);
				contentBox.getChildren().add(toggleButton);
				playerLookup.put(toggleButton, tradeOffer.getPlayerName());
			}
		}
	}

	public String getPlayerNameForSelectedTradeOffer() {
		for (Node node : contentBox.getChildren()) {
			if (node instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) node;
				if (toggleButton.isSelected()) {
					String playerName = playerLookup.get(toggleButton);
					return playerName;
				}
			}
		}
		return null;
	}
}
