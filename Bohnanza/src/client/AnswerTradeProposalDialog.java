package client;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public class AnswerTradeProposalDialog extends Dialog<ButtonType> {

	public AnswerTradeProposalDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AnswerTradeProposal.fxml"));
		try {
			fxmlLoader.setController(this);
			Node node = (Node) fxmlLoader.load();

			getDialogPane().setContent(node);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private VBox tradeOfferHandkartenHalter;

	@FXML
	private VBox yourHandkartenHalter;

	private SelectableCardsListView tradeHandcardsListView;

	private SelectableCardsListView yourHandcardsListView;

	@FXML
	void initialize() {
		tradeHandcardsListView = new SelectableCardsListView();
		tradeOfferHandkartenHalter.getChildren().add(tradeHandcardsListView);
		tradeHandcardsListView.setDisable(true);

		yourHandcardsListView = new SelectableCardsListView();
		yourHandkartenHalter.getChildren().add(yourHandcardsListView);

		getDialogPane().getButtonTypes().add(new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));
		getDialogPane().getButtonTypes().add(new ButtonType("Angebot beantworten", ButtonData.APPLY));
	}
	
	public SelectableCardsListView getTradeHandcardsListView() {
		return tradeHandcardsListView;
	}

	public SelectableCardsListView getYourHandcardsListView() {
		return yourHandcardsListView;
	}

	public static boolean isSuccess(Optional<ButtonType> optional) {
		if (!optional.isPresent()) {
			return false;
		}

		if (optional.get().getButtonData().equals(ButtonData.APPLY)) {
			return true;
		}

		return false;
	}
}
