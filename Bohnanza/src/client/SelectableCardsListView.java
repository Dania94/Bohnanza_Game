package client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class SelectableCardsListView extends ScrollPane {

	public static final int HANDCARDS_HEIGHT = 150;

	private HBox contentBox = new HBox();

	private HashMap<ToggleButton, String> varietyLookup = new HashMap<>();

	/**
	 * Der Konstruktor der Klasse SelectableCardsListView
	 */
	public SelectableCardsListView() {
		// Add box containing the content to this scroll pane.
		setContent(contentBox);

		setPrefHeight(HANDCARDS_HEIGHT + 30);
		setPrefWidth(400);
	}

	/**
	 * Ändere die View der Kartenliste.
	 * 
	 * @param list
	 *            die neue Liste der Karten
	 */
	private void recreateContentBox(List<String> list) {
		contentBox.getChildren().clear();
		varietyLookup.clear();
		for (String element : list) {
			ToggleButton toggleButton = createToggleButton(element);
			contentBox.getChildren().add(toggleButton);
			varietyLookup.put(toggleButton, element);
		}
	}

	/**
	 * Addiere einen neuen Imagview, wenn der Spieler eine neue Karte zieht.
	 * 
	 * @param element
	 *            die Sorte der Karte.
	 * @return {@link ToggleButton}
	 */
	private ToggleButton createToggleButton(String element) {
		ImageView imageView = new ImageView();

		if (element != null) {
			File file = new File(element + ".png");

			if (file.exists()) {
				Image image = new Image("file:" + file.getAbsolutePath());
				imageView.setImage(image);
			} else {
				// TODO Remove later
				System.out.println("File not found: " + file.getAbsolutePath());
			}
		}

		imageView.setFitHeight(HANDCARDS_HEIGHT);
		imageView.setPreserveRatio(true);

		ToggleButton toggleButton = new ToggleButton();
		toggleButton.setGraphic(imageView);

		return toggleButton;
	}

	/**
	 * Ruft die recreateContentBox auf.
	 * 
	 * @param handcards
	 *            die Handkarten des Spielers.
	 */
	public void setHandcards(List<String> handcards) {
		recreateContentBox(handcards);
	}

	/**
	 * Gibt die Karten, die der Spieler ausgewaelt hat, zurueck.
	 * 
	 * @return die Sorten der ausgewaelten Karten.
	 */
	public List<String> getSelectedElements() {
		ArrayList<String> list = new ArrayList<>();
		for (Node node : contentBox.getChildren()) {
			if (node instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) node;
				if (toggleButton.isSelected()) {
					String variety = varietyLookup.get(toggleButton);
					list.add(variety);
				}
			}
		}
		return list;
	}
	 
	public void selectedOnlyFirstElement() {
		boolean foundFirst = false;
		for (Node node : contentBox.getChildren()) {
			if (node instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) node;
				if (foundFirst) {
					toggleButton.setSelected(false);
				} else {
					toggleButton.setSelected(true);
					foundFirst = true;
				}
			}
		}

	}

	public ArrayList<Integer> getSelectedElementIndices() {
		ArrayList<Integer> selectedIndices = new ArrayList<>();
		
		for (int i=0;i<contentBox.getChildren().size();i++) {
			Node node = contentBox.getChildren().get(i);
			
			if (node instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) node;
				if(toggleButton.isSelected()) {
					selectedIndices.add(i);
				}
			}
		}
		
		return selectedIndices;
	}
}
