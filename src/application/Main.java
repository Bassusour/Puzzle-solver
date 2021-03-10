package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
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
		
		
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, pane, i);
		}
		
		Piece p1 = puzzle.getPiece(0);
		Piece p2 = puzzle.getPiece(1);
		unionPieces(p1, p2, pane);
		
		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.setTitle(puzzle.getName());
		stage.show();
	}
	
	private void unionPieces(Piece p1,Piece p2, Pane pane) {
		Path p3 = (Path) Polygon.union(p1, p2);

		// Array of points for the new polygon
		Double[] points = new Double[(p3.getElements().size() - 1)*2];

		int i = 0;
		// going through all the path elements in the path and adding the x and y coordinates to points
		for(PathElement el : p3.getElements()){
		    if(el instanceof MoveTo){
		        MoveTo mt = (MoveTo) el;
		        points[i] = mt.getX();
		        points[i+1] = mt.getY();
		    }
		    if(el instanceof LineTo){
		        LineTo lt = (LineTo) el;
		        points[i] = lt.getX();
		        points[i+1] = lt.getY();
		    }
		    i += 2;
		}

		// creating new Polygon with these points
		Piece newPolygon = new Piece();
		newPolygon.getPoints().addAll(points);
		
		initializePiece(newPolygon, pane, 3);
	}
	
	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setFill(Color.AZURE);
		
		piece.setCursor(Cursor.HAND);
		
	    piece.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            originalX = event.getSceneX();
	            originalY = event.getSceneY();
	            originalTX = ((Polygon) event.getSource()).getTranslateX();
	            originalTY = ((Polygon) event.getSource()).getTranslateY();
	        }
	    });

	    piece.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	//System.out.println(event.getSceneX() + " " + event.getSceneY());
	            double deltaX = event.getSceneX() - originalX;
	            double deltaY = event.getSceneY() - originalY;
	            double deltaTX = originalTX + deltaX;
	            double deltaTY = originalTY + deltaY;
	            ((Polygon) (event.getSource())).setTranslateX(deltaTX);  //transform the object
	            ((Polygon) (event.getSource())).setTranslateY(deltaTY);
	        }
	    });
	    if(i == 0) {
	    	piece.setLayoutX(50);
	    	piece.setLayoutY(50);
	    } else if(i == 1) {
	    	piece.setLayoutX(211); //212
	    	piece.setLayoutY(50);
	    }
	    piece.updatePoints(); 
	    
	    System.out.println(piece.getPoints());
	    pane.getChildren().add(piece);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
