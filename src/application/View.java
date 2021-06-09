package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class View {
	private static int width = 1000;
	private static int height = 600;
	private Pane pane;
	private Stage stage;
	
	public View(Stage stage) {
		this.stage = stage;
		pane = new Pane();
	}
	
	public void setView(AnchorPane groups) {
		pane.getChildren().add(groups);
		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.setTitle("test");
		stage.show();
	}
	
	
	
}
