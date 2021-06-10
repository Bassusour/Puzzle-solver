package application;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
	private JSONReader reader;
	private Puzzle puzzle;
	private Pane pane;

	double originalX;
	double originalY;
	double originalTX;
	double originalTY;
	double deltaTX;
	double deltaTY;
	double originalGX;
	double originalGY;

	private int snapRange = 10;
	private int amountOfCorners = 0;
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private Group groups;
	
//	MenuController menuController;
	CanvasController canvasController;
	
	static Stage window;
	static Scene sceneMenu;
	static Scene sceneCanvas;
	
	FXMLLoader loaderMenu;
	FXMLLoader loaderCanvas;
	
	Parent parentMenu;
	Parent parentCanvas;

	@Override
	public void start(Stage stage) throws IOException {
		
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
		
////		puzzle = new JSONReader().getPuzzle();
//		
//		try {
////			Parent menu = FXMLLoader.load(getClass().getResource("Menu.fxml"));
////			Parent canvas = FXMLLoader.load(getClass().getResource("Canvas.fxml"));
//			
//			loaderMenu = new FXMLLoader(getClass().getResource("Menu.fxml"));
//			menu = loaderMenu.load();
//			menuController = loaderMenu.getController();
//			
//			loaderCanvas = new FXMLLoader(getClass().getResource("Canvas.fxml"));
//			canvas = loaderCanvas.load();
//			canvasController = loaderCanvas.getController();
//			
//			System.out.println("Main Menu Parent: " + menu);
//			System.out.println("Main Canvas Parent: " + canvas);
//			
//			reader = new JSONReader(); 
//			puzzle = reader.getPuzzle();
//			
//			amountOfCorners = reader.getMatches();
//			groups = new Group();
//			canvasController.getPane().getChildren().add(groups);
//
//			for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
//				if(puzzle.getPiece(i) == null) {
//					continue;
//				}
//				Piece piece = puzzle.getPiece(i);
//				initializePiece(piece, canvasController.getPane(), i);
//				piece.setOriginalCenterX(piece.getCenterX());
//				piece.setOriginalCenterY(piece.getCenterY());
//			}
//			
//			//Checks if pieces are identical
//			for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
//				if(puzzle.getPiece(i) == null) {
//					continue;
//				}
//				Piece p1 = puzzle.getPiece(i);
//				for(int j = i+1; j < puzzle.getNoOfPieces(); j++) {
//					if(puzzle.getPiece(j) == null) {
//						continue;
//					}
//					Piece p2 = puzzle.getPiece(j);
//					if(p1.compareTo(p2) == 0) {
//						System.out.print("(" + p1.getNumber() + "," + p2.getNumber() + ")" + " ");
//					}
//				}
//			}
//			//puzzleSetup("Puzzle-3r-3c-7811");
//			Scene scene = new Scene(menu);
//			stage.setScene(scene);
////			stage.setTitle(puzzle.getName());
//			stage.show();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}


	public static void main(String[] args) {
		launch(args);
	}
}

