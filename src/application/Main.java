package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

//Main class acts as the controller
public class Main extends Application {
	@Override
	public void start(Stage stage) {
		Model model = new Model();
		View view = new View(stage);
		
		//Model and View only communicate through this class
		Group groups = model.getGroups();
		view.setView(groups);
		//Puzzlesolver puzzlesolver = new Puzzlesolver();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

