package application;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class View {
	private Stage stage;
	public static Stage window;
	public static Scene sceneMenu;
	public static Scene sceneCanvas;
	private FXMLLoader loaderMenu;
	private FXMLLoader loaderCanvas;
	private Parent parentMenu;
	private Parent parentCanvas;
	
	public View(Stage stage) {
		this.stage = stage;
	}
	
	// Victor W.
	public void setView() throws IOException {
		window = stage;
		
		// Loads the relevant FXML files
		loaderMenu = new FXMLLoader(getClass().getResource("Menu.fxml"));
		loaderCanvas = new FXMLLoader(getClass().getResource("Game.fxml"));
		
		parentMenu = loaderMenu.load();
		parentCanvas = loaderCanvas.load();
		
		// Creates the scenes
		sceneMenu = new Scene(parentMenu);
		sceneCanvas = new Scene(parentCanvas);
		
		// Displays the scenes
		window.setScene(sceneMenu);
		window.setTitle("Puzzle");
		window.show();
	}
}
