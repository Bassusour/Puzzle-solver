package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
	
	public void setView() throws IOException {
		window = stage;
		
		loaderMenu = new FXMLLoader(getClass().getResource("Menu.fxml"));
		loaderCanvas = new FXMLLoader(getClass().getResource("Canvas.fxml"));
		
		parentMenu = loaderMenu.load();
		parentCanvas = loaderCanvas.load();
		
		sceneMenu = new Scene(parentMenu);
		sceneCanvas = new Scene(parentCanvas);
		
		window.setScene(sceneMenu);
		window.setTitle("Puzzle");
		window.show();
	}
}
