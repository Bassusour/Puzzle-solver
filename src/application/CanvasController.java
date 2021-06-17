package application;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class CanvasController {
	
	
	@FXML private MenuBar menuBar;
	@FXML private Pane pane;
	@FXML private MenuItem solve;

	private static String filename = "Puzzles/Puzzle-1r-2c-0995.json";
	
//	public static int width = 1000;
//	public static int height = 600;
	private static JSONReader reader;
	private static Puzzle puzzle;

	private double originalX;
	private double originalY;
	private double originalTX;
	private double originalTY;
	private double deltaTX;
	private double deltaTY;
	private double originalGX;
	private double originalGY;

	private static int snapRange =10;
	private static int amountOfCorners = 0;
	//private ArrayList<Circle> circles = new ArrayList<Circle>();
	public static Group groups;
	
//	private static Stage window;
//	private static Scene sceneMenu;
//	private static Scene sceneCanvas;
	
//	private FXMLLoader loaderMenu;
//	private FXMLLoader loaderCanvas;
//	
//	private Parent parentMenu;
//	private Parent parentCanvas;
	private Puzzlesolver puzzlesolver;
	
	public final Color DEFAULT_COLOR = Color.BISQUE;
	
	public void solvePuzzle() {
		System.out.println(puzzlesolver.solveable());
//		if(puzzlesolver.solveable()) {
//			System.out.println("Puzzle is solveable");
//			puzzlesolver.solvePuzzle(false, true);
//		} else {
//			System.out.println("Puzzle is not solveable");
//		}
		
		//showIdenticalPieces();
		//puzzlesolver.giveHint();
	}
	
	public void puzzleHint() {
		puzzlesolver.giveHint();
	}
	
	public void showIdenticalPieces() {
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			puzzle.getPiece(i).setFill(DEFAULT_COLOR);
		}
		boolean[][] identicals = new boolean[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()];
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			for(int j = i+1; j < puzzle.getNoOfPieces(); j++) {
				if(i != j) {
					if(puzzle.getPiece(i).compareTo(puzzle.getPiece(j)) == 0) {
						identicals[i][j] = true;
					}
				}
			}
		}
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if(puzzle.getPiece(i).getFill() == DEFAULT_COLOR) {
				puzzle.getPiece(i).setFill(Color.color(Math.random(), Math.random(), Math.random()));
			} else {
				continue;
			}
			for(int j = i+1; j < puzzle.getNoOfPieces(); j++) {
				if(identicals[i][j]) {
					puzzle.getPiece(j).setFill(puzzle.getPiece(i).getFill());
				}
			}
		}
	}
	
	
	public void backToMenuButtonPushed(ActionEvent event) throws IOException {
		
		View.window.setScene(View.sceneMenu);
		View.window.show();
		View.window.centerOnScreen();
		
	}
	
	public void initialize() throws IOException {
				
		 View.window.sceneProperty().addListener(new ChangeListener<Scene>() {
			 @Override
			 public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				 pane.getChildren().clear();
				 try {
					//System.out.println(filename);
					puzzleSetup(filename);
				} catch (IOException e) {
					 e.printStackTrace();
				}
			 }
		 });
		
		
		
		
	}
	
	public Pane getPane() {
		return pane;
	}
	
	public static void setPuzzle(String input) {
		filename = input;
	}
	
	
	public void puzzleSetup(String file) throws IOException {		
		
		reader = new JSONReader(file);
		puzzle = reader.getPuzzle();
		amountOfCorners = reader.getMatches();
		groups = new Group();
		
		pane.getChildren().add(groups);

		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if(puzzle.getPiece(i) == null) {
				continue;
			}
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, pane, i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
		}
		puzzlesolver = new Puzzlesolver();
	}

	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setCursor(Cursor.HAND);
		piece.setFill(DEFAULT_COLOR);

		piece.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					originalGX = piece.getParent().getTranslateX();
					originalGY = piece.getParent().getTranslateY();
					
					originalX = event.getSceneX();
					originalY = event.getSceneY();
					originalTX = ((Polygon) event.getSource()).getTranslateX();
					originalTY = ((Polygon) event.getSource()).getTranslateY();
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					originalX = event.getSceneX();
					originalY = event.getSceneY();
				}
			}
		});

		piece.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.getButton() == MouseButton.PRIMARY) {
					
					double deltaX = event.getSceneX() - originalX;
					double deltaY = event.getSceneY() - originalY;
					
					double deltaGX = originalGX + deltaX;
					double deltaGY = originalGY + deltaY;
					deltaTX = originalTX + deltaX;
					deltaTY = originalTY + deltaY;
					
					piece.getParent().setTranslateX(deltaGX);
					piece.getParent().setTranslateY(deltaGY);
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					double deltaY = event.getSceneY() - originalY;
					
					piece.getParent().setRotate(piece.getParent().getRotate() + deltaY);
								
					originalY = event.getSceneY();

				}
			}
		});
		
		piece.setPoints(piece.getPoints());

		piece.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					Group parent = (Group) piece.getParent();
					
					for (Object element : parent.getChildren().toArray()) {
						
						Piece piece = (Piece) element;
						
						piece.updatePoints(event.getSceneX() - originalX, 
										   event.getSceneY() - originalY);
						
						
					}
					
					for (Object element : groups.getChildren().toArray()) {
						
						Group group = (Group) element;
						
						for (Object things : group.getChildren().toArray()) {
							
							Piece piece = (Piece) things;
							for (Point2D point : piece.getPointList()) {
								Circle circle = new Circle(point.getX(), point.getY(), 5);
								pane.getChildren().add(circle);
								
							}
							
						}
						
					}
					
					if (!groups.getChildren().contains(piece)) {
						
						for (Piece element : puzzle.getPieces()) {

							if (piece != element) {
								
								if (!parent.getChildren().contains(element)) {
									
									Shape intersect = Shape.intersect(piece, element);

									if (intersect.getBoundsInLocal().getWidth() > -10) {
										matchPoints(piece, element, amountOfCorners, snapRange);
									}	
								}
							}
						}
					}

				} else if (event.getButton() == MouseButton.SECONDARY) {

					Group parent = (Group) piece.getParent();
					for (Object element : parent.getChildren().toArray()) {
						((Piece) element).updateGroupRotate(parent.getRotate(), parent);
					}
					
					for (Object element : groups.getChildren().toArray()) {
						
						Group group = (Group) element;
						
						for (Object things : group.getChildren().toArray()) {
							
							Piece piece = (Piece) things;
							for (Point2D point : piece.getPointList()) {
								Circle circle = new Circle(point.getX(), point.getY(), 5);
								pane.getChildren().add(circle);
								
							}
							
						}
						
					}
						

				}

			}
		});
		
		Group group = new Group();
		group.getChildren().add(piece);
		groups.getChildren().add(group);

	}

	public static void matchPoints(Piece a, Piece b, int threshold, int snap_range) {

		int matches = 0;
		
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		for (Point2D pointA : a.getPointList()) {

			for (Point2D pointB : b.getPointList()) {

				if (pointA.getX() < pointB.getX() + snap_range) {
					if (pointA.getX() > pointB.getX() - snap_range) {
						if (pointA.getY() < pointB.getY() + snap_range) {
							if (pointA.getY() > pointB.getY() - snap_range) {

								points.add(pointA);
								points.add(pointB);

								matches++;

							}
						}
					}
				}
			}
		}
		
		if (matches == threshold) {
			
			Group A = (Group) a.getParent();
			Group B = (Group) b.getParent();
			
			if (A == B) { return; }
			
			a.setRotate(Math.round(a.getRotate()));
			a.updatePointsRotate(a.getRotate());

			b.setRotate(Math.round(b.getRotate()));
			b.updatePointsRotate(b.getRotate());
			
			
			double[] distances = new double[amountOfCorners];
			
			boolean notEqualDistances = true;
			
			double dx = 0;
			double dy = 0;
			
			while (notEqualDistances) {
				//System.out.println("asd");
				for (int i = 0; i < amountOfCorners * 2; i = i + 2) {
					
					dx = points.get(i + 1).getX() - points.get(i).getX();
					dy = points.get(i + 1).getY() - points.get(i).getY();
					distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
					
				}
				
				for (int i = 0; i < distances.length - 1; i ++) {
					if (Math.abs(distances[i] - distances[i + 1]) < 0.1) {
						notEqualDistances = false;
						continue;
					} else {
						notEqualDistances = true;
						a.setRotate(Math.round(a.getRotate()) + 1);
						a.updatePointsRotate(a.getRotate());
						break;
					}
				}
				
			}
			
			b.updatePoints(b.getParent().getTranslateX(), 
					       b.getParent().getTranslateY());
			
			for (Object element : A.getChildren().toArray()) {
				
				Piece piece = (Piece) element;
				boolean selected = piece.equals(a);

				piece.setTranslateX(piece.getTranslateX() + dx);
				piece.setTranslateY(piece.getTranslateY() + dy);
				
				if (selected) {
					piece.updatePoints(B.getTranslateX() + dx,
									   B.getTranslateY() + dy);
				} else {
					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + dx, 
							           B.getTranslateY() - A.getTranslateY() + dy);
				}
				
				A.getChildren().remove(piece);
				B.getChildren().add(piece);
				
			}
			
			groups.getChildren().remove(A);
			
			for (Object element : B.getChildren().toArray()) {
				((Piece) element).updateGroupRotate(B.getRotate(), B);
			}
			if(a.getFill() == Color.LIGHTBLUE && b.getFill() == Color.LIGHTBLUE) {
				for(int i = 0; i < a.getParent().getChildrenUnmodifiable().size(); i++) {
					((Shape) a.getParent().getChildrenUnmodifiable().get(i)).setFill(Color.BISQUE);
				}
			}
		}
	}
	
	public static JSONReader getReader() {
		return reader;
	}
	
	public static void setGroups(Group groups) {
		CanvasController.groups = groups;
	}

}
