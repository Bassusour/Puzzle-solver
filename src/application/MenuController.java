package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MenuController {

	@FXML private ListView<String> listView;
	@FXML private TextField searchField;
	@FXML private RadioButton randomButton;
	@FXML private Button playButton;

	private Scene scene;
	private ArrayList<String> puzzleList = new ArrayList<String>(); 

	// Victor W.
	// Changes the scene to the game window and loads the selected puzzle
	public void playButtonPushed(ActionEvent event) throws IOException {
		GameController.setPuzzle("Puzzles/" + getSelectedPuzzle());
		View.window.setScene(View.sceneCanvas);
		View.window.show();
		View.window.centerOnScreen();
	}

	// Victor W.
	// Creates the functional search bar for the puzzle list
	public void filterList(String oldValue, String newValue) {
		ObservableList<String> items = FXCollections.observableArrayList();

		if ((searchField == null) || (newValue == null)) {
			listView.setItems(items);
		} else {
			newValue = newValue.toUpperCase();
			for (String puzzles : puzzleList) {
				if (puzzles.toUpperCase().contains(newValue)) {
					items.add(puzzles);
				}
			}
			listView.setItems(items);
		}
	}

	// Victor W.
	public void initialize() {
		
		File folder = new File("Puzzles");
		File[] puzzles = folder.listFiles();
		
		for (File file : puzzles) {
			puzzleList.add(file.getName());
		}

		// Updates the search bar
		searchField.textProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				filterList((String) oldValue, (String) newValue);
			}
		});

		// Updates the list
		listView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					String selectedItems = listView.getSelectionModel().getSelectedItem();
					randomButton.selectedProperty().set(false);
				});

		randomButton.selectedProperty().set(true);
		listView.getItems().addAll(puzzleList);
	}

	// Victor W.
	public String getSelectedPuzzle() {
		String selectedPuzzle;
		if (randomButton.isSelected() || listView.getSelectionModel().getSelectedItem() == null) {
			Random random = new Random();
			int randomInteger = random.nextInt(puzzleList.size());
			selectedPuzzle = puzzleList.get(randomInteger);
		} else {
			selectedPuzzle = listView.getSelectionModel().getSelectedItem();
		}
		return selectedPuzzle;
	}

	// Victor W.
	public Scene getScene() {
		return scene;
	}

}
