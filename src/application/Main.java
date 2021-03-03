package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static int width = 1000;
	public static int height = 600;
	private Puzzle puzzle;
	
	@Override
	public void start(Stage stage) {
		
		JSONReader jsonReader = new JSONReader();
		puzzle = jsonReader.getPuzzle();
		System.out.println(puzzle.getName());
		System.out.println(puzzle.getNoOfPieces());
		
		try {
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//Piece piece = puzzle.getPiece(0);
			//System.out.println(piece.getPoints());
			//System.out.println("Piece number: " + piece.getNumber());
			
			Pane pane = new Pane();
			Group pieces = new Group();
			
			for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
				
				Piece piece = puzzle.getPiece(i);
				piece.setTranslateX(100 + (i * 200));
			    piece.setTranslateY(200);
				piece.setStroke(Color.LIGHTGRAY);
				piece.setFill(Color.BEIGE);
				pieces.getChildren().add(piece);
				
			}
			
			pane.getChildren().add(pieces);
			
			Scene scene = new Scene(pane, width, height);
			stage.setScene(scene);
			stage.setTitle(puzzle.getName());
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
