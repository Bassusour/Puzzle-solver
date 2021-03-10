package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static int width = 1000;
	public static int height = 600;
	private Puzzle puzzle;

	double originalX;
	double originalY;
	double originalTX;
	double originalTY;

	
	@Override
	public void start(Stage stage) {
		
		puzzle = new JSONReader().getPuzzle();
		
		Pane pane = new Pane();
		Group pieces = new Group();
		
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			
			Piece piece = puzzle.getPiece(i);
			//piece.setTranslateX(100 + (i * 200));
		    //piece.setTranslateY(200);
			piece.setStroke(Color.BLACK);
			piece.setFill(Color.BEIGE);
			Circle center = new Circle(piece.getCenterX(), piece.getCenterY(), 5, Color.BLACK);
			piece.setCursor(Cursor.HAND);
			
		    piece.setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {
		            if(event.getButton() == MouseButton.PRIMARY) {
		            	originalX = event.getSceneX();
			            originalY = event.getSceneY();
			            originalTX = ((Polygon) event.getSource()).getTranslateX();
			            originalTY = ((Polygon) event.getSource()).getTranslateY();
		            }
		            
		            if(event.getButton() == MouseButton.SECONDARY) {
		            	originalX = event.getSceneX();
			            originalY = event.getSceneY();
		            }
		        	
		        }
		    });

		    piece.setOnMouseDragged(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {
		        	if(event.getButton() == MouseButton.PRIMARY) {
		        		double deltaX = event.getSceneX() - originalX;
			            double deltaY = event.getSceneY() - originalY;
			            double deltaTX = originalTX + deltaX;
			            double deltaTY = originalTY + deltaY;
			            ((Polygon) (event.getSource())).setTranslateX(deltaTX);  //transform the object
			            ((Polygon) (event.getSource())).setTranslateY(deltaTY);
			            piece.updatePoints(deltaX, deltaY);
			            System.out.println(piece.getCenterX() + " , " + piece.getCenterY());
		        	}
		        	
		        	if (event.getButton() == MouseButton.SECONDARY) {
		        		double deltaY = event.getSceneY() - originalY;
		        		piece.setRotate(piece.getRotate()+deltaY);
		        		originalY = event.getSceneY();
		        	}
		            
		        }
		    });
			pieces.getChildren().add(piece);
			pieces.getChildren().add(center);
		}
		
		pane.getChildren().add(pieces);
		
		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.setTitle(puzzle.getName());
		stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
