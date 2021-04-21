package application;

import java.util.ArrayList;

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
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class Main extends Application  {
	
	public static int width = 1000;
	public static int height = 600;
	private Puzzle puzzle;

	double originalX;
	double originalY;
	double originalTX;
	double originalTY;
	double deltaTX;
	double deltaTY;

	
	@Override
	public void start(Stage stage) {
		
		puzzle = new JSONReader().getPuzzle();
		Pane pane = new Pane();
		
		for (int i = 1; i < puzzle.getNoOfPieces(); i++) {
			if(puzzle.getPiece(i) == null) {
				continue;
			}
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, pane, i);
			System.out.println(piece.getPoints().toString());
		}
		
		//Checks if pieces are identical
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if(puzzle.getPiece(i) == null) {
				continue;
			}
			Piece p1 = puzzle.getPiece(i);
			for(int j = i+1; j < puzzle.getNoOfPieces(); j++) {
				if(puzzle.getPiece(j) == null) {
					continue;
				}
				Piece p2 = puzzle.getPiece(j);
				if(p1.compareTo(p2) == 0) {
					System.out.print("(" + p1.getNumber() + "," + p2.getNumber() + ")" + " ");
				}
			}
		}

//		Piece p1 = puzzle.getPiece(0);
//		Piece p2 = puzzle.getPiece(1);
//		unionPieces(p1, p2, pane);
		
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
//		Piece newPolygon = new Piece();
//		newPolygon.getPoints().addAll(points);
//		
//		initializePiece(newPolygon, pane, 3);
	}
	
	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setFill(Color.BISQUE);
		
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
	            	String msg =
	        		          "(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " +
	        		          "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") -- " +
	        		          "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")";
	            	System.out.println(msg);
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
		            deltaTX = originalTX + deltaX;
		            deltaTY = originalTY + deltaY;
		            ((Polygon) (event.getSource())).setTranslateX(deltaTX);  //transform the object
		            ((Polygon) (event.getSource())).setTranslateY(deltaTY);
		            piece.updatePoints(deltaX, deltaY);
		            //System.out.println(piece.getCenterX() + " , " + piece.getCenterY());
	        	}
	        	
	        	if (event.getButton() == MouseButton.SECONDARY) {
	        		String msg =
	        		          "(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " +
	        		          "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") -- " +
	        		          "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")";
	        		System.out.println(msg);
	        		double deltaY = event.getSceneY() - originalY;
	        		piece.setRotate(piece.getRotate()+deltaY);
	        		originalY = event.getSceneY();
	        	}
	            
	        }
	    });
//	    if(piece.getNumber() == 3) {
//	    	piece.setLayoutX(100);
//	    	piece.setLayoutY(100);
//	    } 
	    
	    piece.setPoints(piece.getPoints());
	    
	    piece.setOnMouseReleased(new EventHandler<MouseEvent>() {
	    	public void handle(MouseEvent event) {
	        	if(event.getButton() == MouseButton.PRIMARY) {
	        		piece.updatePoints(deltaTX, deltaTY);
	        	}
	        	if(event.getButton() == MouseButton.SECONDARY) {
	        		piece.updatePointsRotate(piece.getRotate());
	        	}

	        }
	    });
//	    if(i == 0) {
//	    	piece.setLayoutX(50);
//	    	piece.setLayoutY(50);
//	    } else if(i == 1) {
//	    	piece.setLayoutX(211); //212
//	    	piece.setLayoutY(50);
//	    }
	    
	    pane.getChildren().add(piece);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
