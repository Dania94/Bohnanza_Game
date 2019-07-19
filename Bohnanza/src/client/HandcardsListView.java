package client;

import java.io.File;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class HandcardsListView extends ListView<String> {

	public static final int HANDCARDS_HEIGHT = 150;

	ObservableList<String> data = FXCollections.observableArrayList("nothing", "to", "see", "here");

	public HandcardsListView() {
		setItems(data);
		setPrefHeight(HANDCARDS_HEIGHT + 30);
		setPrefWidth(400);
		setOrientation(Orientation.HORIZONTAL);

		setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> list) {
				return new CardImageCell();
			}
		});
	}

	public void setHandcards(List<String> handcards) {
		data.setAll(handcards);
	}

	public String getSelectedCard() {
		return getSelectionModel().getSelectedItem();
	}

	static class CardImageCell extends ListCell<String> {
		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			ImageView imageView = new ImageView();

			if (item != null) {
				File file = new File(item + ".png");

				if (file.exists()) {
					Image image = new Image("file:" + file.getAbsolutePath());
					imageView.setImage(image);
				}
			}

			imageView.setFitHeight(HANDCARDS_HEIGHT);
			imageView.setPreserveRatio(true);

			setGraphic(imageView);
		}
	}

	public int getSelectedIndex() {
		return getSelectionModel().getSelectedIndex();
	}
}
