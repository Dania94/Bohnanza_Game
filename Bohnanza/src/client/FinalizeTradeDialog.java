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

public class FinalizeTradeDialog extends Dialog<ButtonType> {

	public FinalizeTradeDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FinalizeTrade.fxml"));
		try {
			fxmlLoader.setController(this);
			Node node = (Node) fxmlLoader.load();

			getDialogPane().setContent(node);

		} catch (IOException e) {
			e.printStackTrace();
		}
		setResizable(true);
	}

	@FXML
	private VBox tradeOfferHandkartenHalter;
	
	@FXML
	private VBox yourHandkartenHalter;
	
	private SelectableTradeAnswersListView tradeHandcardsListView;

	private SelectableCardsListView yourHandcardsListView;

	@FXML
	void initialize() {
		tradeHandcardsListView = new SelectableTradeAnswersListView();
		tradeOfferHandkartenHalter.getChildren().add(tradeHandcardsListView);
		
		yourHandcardsListView = new SelectableCardsListView();
		yourHandcardsListView.setDisable(true);
		yourHandkartenHalter.getChildren().add(yourHandcardsListView);

		getDialogPane().getButtonTypes().add(new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));
		getDialogPane().getButtonTypes().add(new ButtonType("Angebot beantworten", ButtonData.APPLY));
	}
	
	public SelectableCardsListView getYourHandcardsListView() {
		return yourHandcardsListView;
	}
	
	public SelectableTradeAnswersListView getSelectableTradeAnswersListView() {
		return tradeHandcardsListView;
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
