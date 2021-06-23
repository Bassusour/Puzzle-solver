package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) throws IOException {
		View view = new View(stage);
		view.setView();
	}

}

