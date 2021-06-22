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
import javafx.scene.transform.Translate;
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
	
	public final static Color DEFAULT_COLOR = Color.BISQUE;
	
	public void solvePuzzle() {
		if(puzzle.getSolveable()) {
			//System.out.println("Puzzle is solveable");
			puzzlesolver.solvePuzzle(false, true);
		} else {
			System.out.println("Puzzle is not solveable");
		}
		//showIdenticalPieces();
		//puzzlesolver.giveHint();
	}
	
	public boolean solveable() {
		return puzzlesolver.solveable();
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
			Piece piece = puzzle.getPiece(i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
			piece.setStaticPoints();
			initializePiece(piece, pane, i);
		}
		puzzlesolver = new Puzzlesolver(this);
	}

	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setCursor(Cursor.HAND);
		piece.setFill(DEFAULT_COLOR);
		
		if(piece.getNumber() == 0) {
			piece.setFill(Color.RED);
		} 
//		else if(piece.getNumber() == 1) {
//			piece.setFill(Color.GREEN);
//		} else if(piece.getNumber() == 2) {
//			piece.setFill(Color.BLUE);
//		} else if(piece.getNumber() == 3) {
//			piece.setFill(Color.YELLOW);
//		}
		
		

		piece.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					

					// Position of piece wrt. its starting position
					originalGX = piece.getParent().getTranslateX();
					originalGY = piece.getParent().getTranslateY();
					
					// Position of cursor wrt. top left
					originalX = event.getSceneX();
					originalY = event.getSceneY();
					
					// Position of piece wrt. the difference between starting position and matched position (starting position)
					originalTX = ((Polygon) event.getSource()).getTranslateX();
					originalTY = ((Polygon) event.getSource()).getTranslateY();
					
					//System.out.println("fr venstreklik: " + piece.getParent().getRotate());
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					//Position of cursor wrt. top left
					originalX = event.getSceneX();
					originalY = event.getSceneY();
//					oldGroupRotate = piece.getParent().getRotate();

				}
			}
		});

		piece.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				if (event.getButton() == MouseButton.PRIMARY) {
					
					// Movement from picked up to current position
					double deltaX = event.getSceneX() - originalX;
					double deltaY = event.getSceneY() - originalY;
					//System.out.println("delta: " + deltaX);
					//System.out.println(deltaY);
					
					// Position of piece wrt. starting position
					double deltaGX = originalGX + deltaX;
					double deltaGY = originalGY + deltaY;
//					System.out.println(deltaGX);
//					System.out.println(deltaGY);
					// Position of piece wrt. picked up position
					deltaTX = originalTX + deltaX;
					deltaTY = originalTY + deltaY;
					
//					Translate translate = new Translate();
//					translate.setX(deltaX);
//					translate.setY(deltaY);
//					piece.getTransforms().addAll(translate); 
//					originalX = event.getSceneX();
//					originalY = event.getSceneY();
//					System.out.println(piece.getPoints());
//					//System.out.println(piece.localToParent(0,0));
//					System.out.println(piece.getTransforms());
					//System.out.println("("+piece.getCenterX()+","+piece.getCenterY()+") "+ piece.localToParent(piece.getCenterX(),piece.getCenterY()));
//					for(int i = 0; i < piece.getPoints().size()-1; i++) {
//						
//					}
//					Circle circle = new Circle(deltaGX, deltaGY, 5);
//					pane.getChildren().add(circle);
					
					
//					System.out.println(piece.getPoints());
					piece.getParent().setTranslateX(deltaGX);
					piece.getParent().setTranslateY(deltaGY);
//					piece.getTransforms().add(new Rotate(piece.getCenterY(), piece.getCenterX(), 1));
//					System.out.println(piece.getParent().getTranslateX());
//					System.out.println(piece.getParent().getTranslateY());
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
//					System.out.println(piece.getPoints());
//					System.out.println(piece.getPointList());
					//System.out.println("efter venstreklik: " + piece.getParent().getRotate());
					
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
					
//					System.out.println("Piece rotate: " + piece.getRotate());

				} else if (event.getButton() == MouseButton.SECONDARY) {

					Group parent = (Group) piece.getParent();
					for (Object element : parent.getChildren().toArray()) {
						Piece piece = (Piece) element;
						piece.updateGroupRotate(parent.getRotate(), parent);
//						piece.updatePieceRotate(piece.getRotate() - oldGroupRotate);
					}
					//System.out.println("efter rotation: " + piece.getParent().getRotate());
					
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
		if(!puzzle.getSolveable()) {
			return;
		}
		
		int matches = 0;
		
		ArrayList<Point2D> points = new ArrayList<Point2D>();
//		System.out.println("a");
//		System.out.println(a.getPointList());
//		System.out.println("b");
//		System.out.println(b.getPointList());
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
			
			a.updatePointsRotate(a.getRotate());
			b.updatePointsRotate(b.getRotate());
			
			//Distances between points in each piece
			double[] distances = new double[amountOfCorners];
			
			boolean notEqualDistances = true;
			
			double dx = 0;
			double dy = 0;
			double rot = 0;
			
			//Distances should be same when orientation fits
			while (notEqualDistances) {
				for (int i = 0; i < amountOfCorners * 2; i = i + 2) {
					//Distances are calculated
					dx = points.get(i + 1).getX() - points.get(i).getX();
					dy = points.get(i + 1).getY() - points.get(i).getY();
					distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
					
				}
				
				notEqualDistances = false;
				
				for (int i = 0; i < distances.length - 1; i ++) {
                    if (Math.abs(distances[i] - distances[i + 1]) > 0.1) {
//                        a.getTransforms().add(new Rotate(rot, a.getLayoutBounds().getCenterX(), a.getLayoutBounds().getCenterY()));
                        a.setRotate(rot);
                    	a.updateGroupRotate2(rot, A);
                        notEqualDistances = true;
                        break;
                    }
				}
				
				rot += 0.1;
			}
			
			for (Object element : A.getChildren().toArray()) {
				Piece piece = (Piece) element;
				
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
				
				A.getChildren().remove(piece);
				B.getChildren().add(piece);
				
			}
			
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

			if(a.getFill() == Color.LIGHTBLUE && b.getFill() == Color.LIGHTBLUE) {
				for(int i = 0; i < a.getParent().getChildrenUnmodifiable().size(); i++) {
					((Shape) a.getParent().getChildrenUnmodifiable().get(i)).setFill(DEFAULT_COLOR);
				}
			}

		}
	}
	
	public void powerMatchPoints(Piece a, Piece b, ArrayList<Point2D> points) {
		Group A = (Group) a.getParent();
		Group B = (Group) b.getParent();
		
		a.updatePointsRotate(a.getRotate());
		b.updatePointsRotate(b.getRotate());
		
		//Distances between points in each piece
		double[] distances = new double[3];
		
		boolean notEqualDistances = true;
		
		double dx = 0;
		double dy = 0;
		double rot = 0;
		
		for(int g = 0; g < 4; g++) {
			Circle circle1 = new Circle(points.get(g).getX(), points.get(g).getY(),5);
			pane.getChildren().addAll(circle1);
		}
		
		//Distances should be same when orientation fits
		while (notEqualDistances) {
			if(rot == 360) {
				break;
			}
			
			for (int i = 0; i < 6; i+=2) {
				//Distances are calculated
				dx = points.get(i).getX() - points.get(i+1).getX();
				dy = points.get(i).getY() - points.get(i+1).getY();
//				System.out.println(dx);
				distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
			}
			
			notEqualDistances = false;
			
			for (int i = 0; i < distances.length - 1; i ++) {
                if (Math.abs(distances[i] - distances[i + 1]) > 0.05) {
//                    a.getTransforms().add(new Rotate(rot, a.getLayoutBounds().getCenterX(), a.getLayoutBounds().getCenterY()));
                    a.setRotate(rot);
                	a.updateGroupRotate2(rot, A);
                    notEqualDistances = true;
                    break;
                }
			}
			
			rot += 0.1;
		}
		
		for (Object element : A.getChildren().toArray()) {
			Piece piece = (Piece) element;
			
			double newX = piece.getTranslateX() + dx;
			double newY = piece.getTranslateY() + dy;
			piece.setTranslateX(newX);
			piece.setTranslateY(newY);

//			System.out.println("A " + a.getRotate());
//			System.out.println("Piece " + piece.getRotate());
			if (piece != a) {
				piece.setRotate(a.getRotate() + piece.getRotate());
//				System.out.println("Sum " + piece.getRotate());
			} 
			
			A.getChildren().remove(piece);
			B.getChildren().add(piece);
			
		}
		
		for (Object element : B.getChildren().toArray()) {
			Piece piece = (Piece) element;
			if (piece == a) {
				piece.updatePieceRotate(piece.getRotate());
			} 
//			System.out.println("Rotation: " + piece.getRotate());
			
		}
		
		for (Object element : B.getChildren().toArray()) {
			Piece piece = (Piece) element;
			piece.updateGroupRotate(B.getRotate(), B);
		}
		
		groups.getChildren().remove(A);

		if(a.getFill() == Color.LIGHTBLUE && b.getFill() == Color.LIGHTBLUE) {
			for(int i = 0; i < a.getParent().getChildrenUnmodifiable().size(); i++) {
				((Shape) a.getParent().getChildrenUnmodifiable().get(i)).setFill(DEFAULT_COLOR);
			}
		}

	}
	
	public static JSONReader getReader() {
		return reader;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public static void setGroups(Group groups) {
		CanvasController.groups = groups;
	}

}
