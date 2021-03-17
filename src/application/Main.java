package application;

import java.awt.geom.Point2D;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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


public class Main extends Application {
	
	public static int width = 1000;
	public static int height = 600;
	private Puzzle puzzle;
	private Pane pane;

	double originalX;
	double originalY;
	double originalTX;
	double originalTY;
	double deltaTX;
	double deltaTY;
	
	private int snapRange = 20;

	
	@Override
	public void start(Stage stage) {
		
		puzzle = new JSONReader().getPuzzle();
		
		pane = new Pane();
		
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, pane, i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
			System.out.println(piece.getPoints().toString());
		}
		
		//setOriginalCenters(puzzle.getPieces());
		
		Piece p1 = puzzle.getPiece(0);
		Piece p2 = puzzle.getPiece(1);
		//unionPieces(p1, p2, pane);
		
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

		//creating new Polygon with these points
		Piece newPolygon = new Piece();
		newPolygon.getPoints().addAll(points);
		
		puzzle.addPieceToArray(newPolygon);
		newPolygon.setPoints(newPolygon.getPoints());
		
		initializePiece(newPolygon, pane, 3);
	}
	
	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setFill(Color.AZURE);
		
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
		            deltaTX = originalTX + deltaX;
		            deltaTY = originalTY + deltaY;
		            ((Polygon) (event.getSource())).setTranslateX(deltaTX);  //transform the object
		            ((Polygon) (event.getSource())).setTranslateY(deltaTY);
	        	}
	        	
	        	if (event.getButton() == MouseButton.SECONDARY) {
	        		double deltaY = event.getSceneY() - originalY;
	        		piece.setRotate(piece.getRotate() + deltaY);
	        		originalY = event.getSceneY();
	        		
	        		//System.out.println(Math.abs(piece.getRotate()) % 360);
	        		
	        	}
	            
	        }
	    });
	    
	    piece.setOnMouseReleased(new EventHandler<MouseEvent>() {

	    	@Override
	    	public void handle(MouseEvent event) {
	    		if (event.getButton() == MouseButton.PRIMARY) {
	    			
	    			piece.updatePoints(deltaTX, deltaTY);
		            //System.out.println(piece.getCenterX() + " , " + piece.getCenterY());
		            
		            for (Piece element : puzzle.getPieces()) {
		            	
		            	if (piece != element) {
		            		
		            		Shape intersect = Shape.intersect(piece, element);
				            
				            if (intersect.getBoundsInLocal().getWidth() > -snapRange) {
				            	matchPoints(piece,element,3);
				            }
		            		
		            	}
		            	
		            }
		            
	    		} else if (event.getButton() == MouseButton.SECONDARY) {
	    			
	    			
	    			double rotation = Math.abs(piece.getRotate()) % 360;
	    			if (rotation < 10 || rotation > 350) {
	    				piece.setRotate(0);
	    			}
	    			
	    		}
	    	}
	    });
	    
//	    if (i == 0) {
//	    	piece.setLayoutX(50);
//	    	piece.setLayoutY(50);
//	    } else if (i == 1) {
//	    	piece.setLayoutX(211); //211
//	    	piece.setLayoutY(50);
//	    }

	    pane.getChildren().add(piece);
	}
	
	public void matchPoints(Piece a, Piece b, int threshold) {
		
		int matches = 0;
		
		double deltaX = 0;
		double deltaY = 0;
		
		for (Point2D pointA : a.getPointList()) {
			
			for (Point2D pointB : b.getPointList()) {
				
				if (pointA.getX() < pointB.getX() + snapRange) {
					if (pointA.getX() > pointB.getX() - snapRange) {
						if (pointA.getY() < pointB.getY() + snapRange) {
							if (pointA.getY() > pointB.getY() - snapRange) {
								
								deltaX = pointB.getX() - pointA.getX();
								deltaY = pointB.getY() - pointA.getY();
								
								matches++;
								
							}
						}
					}
				}	
			}
		}
		
		if (matches == threshold) {
			
			double translateX = deltaX - (a.getOriginalCenterX() - a.getCenterX());
			double translateY = b.getCenterY() - a.getOriginalCenterY();
			
			a.setTranslateX(translateX);
			a.setTranslateY(translateY);
			a.updatePoints(translateX, translateY);
			
			unionPieces(a, b, pane);
			
			pane.getChildren().remove(a);
			pane.getChildren().remove(b);
			
		}
		
	}
	
	public void setOriginalCenters(Piece[] pieces) {
		
		for (Piece element : pieces) {
			element.setOriginalCenterX(element.getCenterX());
			element.setOriginalCenterY(element.getCenterY());
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
