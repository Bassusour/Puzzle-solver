package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class View {
	private static int width = 1000;
	private static int height = 600;
	private Pane pane;
	private Stage stage;
	
	static Stage window;
	static Scene sceneMenu;
	static Scene sceneCanvas;
	
	FXMLLoader loaderMenu;
	FXMLLoader loaderCanvas;
	
	Parent parentMenu;
	Parent parentCanvas;
	
	public View(Stage stage) {
		this.stage = stage;
	}
	
	public void setView() throws IOException {
//		pane.getChildren().add(groups);
//		Scene scene = new Scene(pane, width, height);
//		stage.setScene(scene);
//		stage.setTitle("test");
//		stage.show();
		
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
