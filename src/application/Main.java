package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public void start(Stage stage) throws IOException {
		//CanvasController model = new CanvasController();
		View view = new View(stage);
		
		//Model and View only communicate through this class
//		Group groups = model.getGroups();
		view.setView();

	}

	public static void main(String[] args) {
		launch(args);
	}
}

