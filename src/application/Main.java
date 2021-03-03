package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class Main extends Application {
	
	double originalX;
	double originalY;
	double originalTX;
	double originalTY;
	
	@Override
	public void start(Stage primaryStage) {
		
		new JSONReader();
		
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(new Double[]{
		    0.0, 0.0,
		    50.0, 20.0,
		    10.0, 50.0 });
		polygon.setFill(Color.DARKCYAN);
		
		polygon.setCursor(Cursor.HAND);
		
		
		
	    polygon.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            originalX = event.getSceneX();
	            originalY = event.getSceneY();
	            originalTX = ((Polygon) event.getSource()).getTranslateX();
	            originalTY = ((Polygon) event.getSource()).getTranslateY();
	        }
	    });

	    polygon.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	System.out.println(event.getSceneX() + " " + event.getSceneY());
	            double deltaX = event.getSceneX() - originalX;
	            double deltaY = event.getSceneY() - originalY;
	            double deltaTX = originalTX + deltaX;
	            double deltaTY = originalTY + deltaY;
	            ((Polygon) (event.getSource())).setTranslateX(deltaTX);  //transform the object
	            ((Polygon) (event.getSource())).setTranslateY(deltaTY);
	        }
	    });
		
		BorderPane root = new BorderPane(polygon);
		Scene scene = new Scene(root,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
