package application;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	private CanvasController canvasController;
	private MenuController menuController;
	private View view;

	public void start(Stage stage) throws IOException {
		CanvasController canvasController = new CanvasController();
		MenuController menuController = new MenuController();
		View view = new View(stage);
		view.setView();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

