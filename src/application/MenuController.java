package application;

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

	@FXML
	private ListView<String> listView;
	@FXML
	private TextField textField;
	@FXML
	private RadioButton randomButton;
	@FXML
	private Button playButton;

	private Scene scene;

	private String[] puzzleList = { "Puzzle-1r-2c-0995", "Puzzle-1r-2c-6816-rot-sol", "Puzzle-1r-2c-7537-rot",
			"Puzzle-1r-2c-8100-sol", "Puzzle-1r-2c-8949-pure", "Puzzle-1r-3c-1848-rot-sol", "Puzzle-1r-3c-2774-pure",
			"Puzzle-1r-3c-2988-sol", "Puzzle-1r-3c-5511-rot", "Puzzle-1r-3c-9961", "Puzzle-1r-4c-0236-rot-sol",
			"Puzzle-1r-4c-3106", "Puzzle-1r-4c-5435-pure", "Puzzle-1r-4c-7284-rot", "Puzzle-1r-4c-8400-sol",
			"Puzzle-2r-2c-1430", "Puzzle-2r-2c-1558-rot", "Puzzle-2r-2c-2450-rot-sol", "Puzzle-2r-2c-2640-rot-sol",
			"Puzzle-2r-2c-3083-pure", "Puzzle-2r-2c-3797-pure", "Puzzle-2r-2c-4062-rot", "Puzzle-2r-2c-5863-rot",
			"Puzzle-2r-2c-6497-pure", "Puzzle-2r-2c-6984", "Puzzle-2r-2c-8259-sol", "Puzzle-2r-2c-8267",
			"Puzzle-2r-2c-8442-rot-sol", "Puzzle-2r-2c-9002-sol", "Puzzle-2r-2c-9345-sol", "Puzzle-2r-3c-0198",
			"Puzzle-2r-3c-0770-pure", "Puzzle-2r-3c-0908-sol", "Puzzle-2r-3c-4820-rot", "Puzzle-2r-3c-6855-rot-sol",
			"Puzzle-3r-3c-0094-pure", "Puzzle-3r-3c-0271-pure", "Puzzle-3r-3c-1064-rot", "Puzzle-3r-3c-2445-rot",
			"Puzzle-3r-3c-3354", "Puzzle-3r-3c-3756-sol", "Puzzle-3r-3c-4257-sol", "Puzzle-3r-3c-6866-rot-sol",
			"Puzzle-3r-3c-7811", "Puzzle-3r-3c-8950-rot-sol", "Puzzle-4r-4c-0083-sol", "Puzzle-4r-4c-3544-rot-sol",
			"Puzzle-4r-4c-4808", "Puzzle-4r-4c-8132-rot", "Puzzle-4r-4c-8551-pure", "Puzzle-4r-6c-0775",
			"Puzzle-4r-6c-0781-pure", "Puzzle-4r-6c-3898-rot", "Puzzle-4r-6c-8642-sol", "Puzzle-4r-6c-9592-rot-sol",
			"Puzzle-5r-8c-1920-sol", "Puzzle-5r-8c-3603-pure", "Puzzle-5r-8c-4228-rot", "Puzzle-5r-8c-5144-rot-sol",
			"Puzzle-5r-8c-6916", "Puzzle-8r-10c-0670-pure", "Puzzle-8r-10c-1628-sol", "Puzzle-8r-10c-2615-rot-sol",
			"Puzzle-8r-10c-4267-rot", "Puzzle-8r-10c-8480", "Puzzle-15r-20c-0945-rot-sol", "Puzzle-15r-20c-3290-pure",
			"Puzzle-15r-20c-4590-rot", "Puzzle-15r-20c-5737", "Puzzle-15r-20c-8696-sol", "PieceList01", "PieceList02",
			"PieceList03", "PieceList04", "Classic-003-005-1331", "Classic-003-005-4813", "Classic-003-005-8825",
			"Classic-005-008-4343", "Classic-005-008-4858", "Classic-005-008-7076", "Classic-008-015-0314",
			"Classic-008-015-2625", "Classic-008-015-8137", "Classic-040-060-3882", "Spejlvendt", "checkIdentical",
			"puzzle_01_manual", "puzzle_02_auto", "puzzle_03_manual", "puzzle_04_auto", "puzzle_05_auto", "puzzle_11_auto",
			"puzzle_12_auto"};

	public void playButtonPushed(ActionEvent event) throws IOException {

//		Parent canvasParent = Main.canvas;	
//		Scene canvasScene = Main.sceneCanvas;
//		Node source = (Node) event.getSource();
//		Stage window = (Stage) source.getScene().getWindow();
//		CanvasController.getPane().getChildren().clear();
		CanvasController.setPuzzle("Puzzles/" + getSelectedPuzzle() + ".json");
		View.window.setScene(View.sceneCanvas);
		View.window.show();
		View.window.centerOnScreen();

	}

	public void filterList(String oldValue, String newValue) {

		ObservableList<String> items = FXCollections.observableArrayList();

		if ((textField == null) || (newValue == null)) {
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

	public void initialize() {

//		playButton.setOnAction(new EventHandler<ActionEvent>() {
//		    @Override public void handle(ActionEvent e) {
//		        System.out.println("Puzzles/" + getSelectedPuzzle() + ".json");
//		    }
//		});

		textField.textProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				filterList((String) oldValue, (String) newValue);
			}
		});

		listView.getSelectionModel().selectedItemProperty()
				.addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
					String selectedItems = listView.getSelectionModel().getSelectedItem();
					randomButton.selectedProperty().set(false);
				});

		randomButton.selectedProperty().set(true);

		listView.getItems().addAll(puzzleList);

	}

	public String getSelectedPuzzle() {
		String selectedPuzzle;
		if (randomButton.isSelected() || listView.getSelectionModel().getSelectedItem() == null) {
			Random random = new Random();
			int randomInteger = random.nextInt(puzzleList.length);
			selectedPuzzle = puzzleList[randomInteger];
		} else {
			selectedPuzzle = listView.getSelectionModel().getSelectedItem();
		}
		return selectedPuzzle;
	}

	public Scene getScene() {
		return scene;
	}

}
