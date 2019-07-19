package client;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class OfferTradeDialog extends Dialog<ButtonType> {

	public OfferTradeDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OfferTrade.fxml"));
		try {
			fxmlLoader.setController(this);
			Node node = (Node) fxmlLoader.load();

			getDialogPane().setContent(node);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private ToggleButton cardImageDraw1Button;

	@FXML
	private ToggleButton cardImageDraw2Button;
	
	@FXML
	private ImageView cardImageDraw1;

	@FXML
	private ImageView cardImageDraw2;

	@FXML
	private VBox handkartenHalter;

	private SelectableCardsListView handcardsListView;

	@FXML
	void initialize() {
		handcardsListView = new SelectableCardsListView();

		handkartenHalter.getChildren().add(handcardsListView);

		getDialogPane().getButtonTypes().add(new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));
		getDialogPane().getButtonTypes().add(new ButtonType("Angebot versenden", ButtonData.APPLY));
	}
	
	private Image loadImage(String element) {
		if (element != null) {
			File file = new File(element + ".png");

			if (file.exists()) {
				Image image = new Image("file:" + file.getAbsolutePath());
				return image;
			}
			
		}
		return null;
	}
	
	public void setDrawCard1(String variety) {
		Image image = loadImage(variety);
		cardImageDraw1.setImage(image);
	}
	
	public void setDrawCard2(String variety) {
		Image image = loadImage(variety);
		cardImageDraw2.setImage(image);
	}

	public SelectableCardsListView getSelectableCardsListView() {
		return handcardsListView;
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

	public boolean isDrawCard1Selected() {
		return cardImageDraw1Button.isSelected();
	}

	public boolean isDrawCard2Selected() {
		return cardImageDraw2Button.isSelected();
	}

}
