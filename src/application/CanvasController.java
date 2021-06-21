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
import javafx.scene.layout.VBox;
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
	
	public static int width = 1000;
	public static int height = 600;
	private static JSONReader reader;
	private Puzzle puzzle;

	double originalX;
	double originalY;
	double originalTX;
	double originalTY;
	double deltaTX;
	double deltaTY;
	double originalGX;
	double originalGY;
	double oldGroupRotate;

	private static int snapRange = 10;
	private static int amountOfCorners = 0;
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private static Group groups;
	
//	MenuController menuController;
	CanvasController canvasController;
	
	static Stage window;
	static Scene sceneMenu;
	static Scene sceneCanvas;
	
	FXMLLoader loaderMenu;
	FXMLLoader loaderCanvas;
	
	Parent parentMenu;
	Parent parentCanvas;
	
	public void solvePuzzle() {
		Puzzlesolver puzzlesolver = new Puzzlesolver();
		puzzlesolver.solvePuzzle();
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
//					System.out.println(filename);
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
			piece.setStaticPoints();
		}
		
	}

	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setFill(Color.BISQUE);
		piece.setCursor(Cursor.HAND);

		piece.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					
					System.out.println("Piece rotation: " + piece.getRotate());
					
//					System.out.println("Translate: " + piece.getTranslateX() + ", " + piece.getTranslateY());
					
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
					oldGroupRotate = piece.getParent().getRotate();
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
//					piece.getParent().getTransforms().add(new Rotate(deltaY, piece.getParent().getLayoutBounds().getCenterX(), piece.getParent().getLayoutBounds().getCenterY()));
								
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
							//Some elements are null, since Piece numbers start at 1 or are not properly incremented (1-2-13-14)
							if (element == null) {
								continue;
							}

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
					
//					System.out.println("Piece rotate: " + piece.getRotate());

				} else if (event.getButton() == MouseButton.SECONDARY) {

					Group parent = (Group) piece.getParent();
					for (Object element : parent.getChildren().toArray()) {
						Piece piece = (Piece) element;
						piece.updateGroupRotate(parent.getRotate(), parent);
//						piece.updatePieceRotate(piece.getRotate() - oldGroupRotate);
					}
					
					for (Object element : groups.getChildren().toArray()) {
						
						Group group = (Group) element;
						
						for (Object things : group.getChildren().toArray()) {
							
							Piece piece = (Piece) things;
							for (Point2D point : piece.getPointList()) {
								Circle circle = new Circle(point.getX(), point.getY(), 5);
								pane.getChildren().add(circle);
								
							}
							
//							Circle circle = new Circle(piece.getPointList().get(4).getX(), piece.getPointList().get(4).getY(), 5);
//							pane.getChildren().add(circle);
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
			
			//Already same group
			if (A == B) { return; }
			
//			a.setMatchingPoints(points);
//			b.setMatchingPoints(points);
			
			//Distances between points in each piece
			double[] distances = new double[amountOfCorners];
			
			boolean notEqualDistances = true;
			
			double dx = 0;
			double dy = 0;
			double rot = 0;
			
//			for (Point2D point : a.getPointList()) {
//				Circle circle = new Circle(point.getX(), point.getY(), 5);
//				pane.getChildren().add(circle);
//				circle.setFill(Color.RED);
//				
//			}
			
			for (Object element : A.getChildren().toArray()) {
				Piece piece = (Piece) element;
				double changeX = piece.getTranslateX() - a.getTranslateX();
				double changeY = piece.getTranslateY() - a.getTranslateY();
				piece.setTranslateX(changeX);
				piece.setTranslateY(changeY);
				piece.updatePoints(changeX, changeY);
			}
			
			a.updatePointsRotate(a.getRotate());
			b.updatePointsRotate(b.getRotate());
			
			//Distances should be same when orientation fits
			while (notEqualDistances) {
				
//				Circle circle = new Circle(a.getPointList().get(4).getX(), a.getPointList().get(4).getY(), 5);
//				pane.getChildren().add(circle);
//				circle.setFill(Color.RED);
				
				for (int i = 0; i < amountOfCorners * 2; i = i + 2) {
					
					//Distances are calculated
					dx = points.get(i + 1).getX() - points.get(i).getX();
					dy = points.get(i + 1).getY() - points.get(i).getY();
					distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
					
				}
				
				notEqualDistances = false;
				
//				System.out.println(Arrays.toString(distances));
				
//				System.out.println("Old: " + a.getPoints().toString());
				
				for (int i = 0; i < distances.length - 1; i ++) {
                    if (Math.abs(distances[i] - distances[i + 1]) > 0.1) {
//                        a.getTransforms().add(new Rotate(rot, a.getLayoutBounds().getCenterX(), a.getLayoutBounds().getCenterY()));
                        a.setRotate(rot);
                    	a.updateGroupRotate2(rot, A);
                        notEqualDistances = true;
                        break;
                    }
				}

//				System.out.println("New: " + a.getPointList().toString());
//				System.out.println("Rotation: " + rot);
				
				rot += 0.1;
				
//				System.out.println("Piece number: " + a.getNumber());
//				System.out.println("Piece rotation: " + a.getRotate());
//				System.out.println("Center X: " + a.getLayoutBounds().getCenterX());
//				System.out.println("Center X: " + a.getLayoutBounds().getCenterY());
//				System.out.println("Piece original points: " + a.getPoints().toString());
//				System.out.println("Piece points: " + a.getPointList().toString());
//				System.out.println();
				
				
			}
			
//			b.updatePoints(b.getParent().getTranslateX(), 
//					       b.getParent().getTranslateY());
			
			for (Object element : A.getChildren().toArray()) {
				
				Piece piece = (Piece) element;
//				boolean selected = piece.equals(a);
//				
//				System.out.println("X: " + piece.getParent().getTranslateX());
//				System.out.println("Y: " + piece.getParent().getTranslateY());
//
//				piece./*getParent().*/setTranslateX(piece./*getParent().*/getTranslateX() + dx);
//				piece./*getParent().*/setTranslateY(piece./*getParent().*/getTranslateY() + dy);
//				
//				if (selected) {
//					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + dx,
//									   B.getTranslateY() - A.getTranslateX() + dy);
//				} else {
//					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + dx, 
//							           B.getTranslateY() - A.getTranslateY() + dy);
//				}
				
//				piece.setTranslateX(0);
//				piece.setTranslateY(0);
//				System.out.println("Piece translateX: " + piece.getTranslateX());
//				for (Object e : A.getChildren().toArray()) {
//					Piece p = (Piece) e;
//					double newX;
//					double newY;
//					newX = piece.getTranslateX() + dx;
//					newY = piece.getTranslateY() + dy;
////					if (piece == a) {
////						newX = piece.getTranslateX() + dx;
////						newY = piece.getTranslateY() + dy;
////					} else {
////						newX = dx;
////						newY = dy;
////					}
//					piece.setTranslateX(newX);
//					piece.setTranslateY(newY);
//				}
				
//				System.out.println("Piece: " + piece);
//				System.out.println("Translate: " + piece.getTranslateX() + ", " + piece.getTranslateY());
//				System.out.println("Distance: " + dx + ", " + dy);
				
				
				double newX = piece.getTranslateX() + dx;
				double newY = piece.getTranslateY() + dy;
				piece.setTranslateX(newX);
				piece.setTranslateY(newY);
	
				System.out.println("A " + a.getRotate());
				System.out.println("Piece " + piece.getRotate());
				if (piece != a) {
					piece.setRotate(a.getRotate() + piece.getRotate());
					System.out.println("Sum " + piece.getRotate());
				} 
				
//				System.out.println("Piece translateX: " + piece.getTranslateX());
//				System.out.println("Piece translateX: " + piece.getTranslateX());
				
//				piece.setTranslateX(piece.getTranslateX() + dx);
				
//				ArrayList<Point2D> pointList = a.getPointList();
//				
//				Piece newPiece = new Piece();
//				
//				for (Point2D point : pointList) {
//					newPiece.getPoints().addAll(point.getX(), point.getY());
//				}
//				
//				System.out.println(newPiece.getRotate());
				
//				for (Object elementA : A.getChildren().toArray()) {
//					((Piece) elementA).updateGroupRotate(A.getRotate(), A);
//				}
				
				A.getChildren().remove(piece);
				B.getChildren().add(piece);
				
			}
			
			
//			for (Object element : B.getChildren().toArray()) {
//				Piece piece = (Piece) element;
//				ArrayList<Point2D> matchpoints = piece.getMatchingPoints();
//				double deltaX = matchpoints.get(1).getX() - points.get(0).getX();
//				double deltaY = matchpoints.get(1).getY() - points.get(0).getY();
//				System.out.println("Delta: " + deltaX + ", " + deltaY);
//				piece.setTranslateX(piece.getTranslateX() + deltaX);
//				piece.setTranslateY(piece.getTranslateY() + deltaY);
//
//			}
			
			for (Object element : B.getChildren().toArray()) {
				Piece piece = (Piece) element;
				if (piece == a) {
					piece.updatePieceRotate(piece.getRotate());
				} 
				System.out.println("Rotation: " + piece.getRotate());
				
			}
			
			for (Object element : B.getChildren().toArray()) {
				Piece piece = (Piece) element;
				piece.updateGroupRotate(B.getRotate(), B);
			}
			
			groups.getChildren().remove(A);
		

		}
	}
	
	public static JSONReader getReader() {
		return reader;
	}

}
