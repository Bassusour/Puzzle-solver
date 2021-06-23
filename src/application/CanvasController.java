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
import javafx.scene.image.ImageView;
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
	@FXML private MenuItem identical;
	@FXML private MenuItem hint;
	@FXML private ImageView solvable;
	@FXML private ImageView notSolvable;

	private static String filename = "Puzzles/Puzzle-1r-2c-0995.json";
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
	
	private static int snapRange = 10;
	private static int amountOfCorners = 0;
	public static Group groups;
	private Puzzlesolver puzzlesolver;
	
	public final static Color DEFAULT_COLOR = Color.BISQUE;
	
	public void solvePuzzle() {
		if(solveable()) {
			try {
				puzzlesolver.solvePuzzle(false, true);
			} catch(Exception e) {
				System.out.println("Puzzle broke an assumption");
			}
		} else {
			System.out.println("Puzzle is not solveable");
		}
	}
	
	public boolean solveable() {
		try {
			return puzzlesolver.solveable();
		} catch (Exception e) {
			System.out.println("Puzzle broke an assumption");
			return false;
		}
	}
	
	public void puzzleHint() {
		puzzlesolver.giveHint();
//		try {
//			puzzlesolver.giveHint();
//		} catch (Exception e) {
//			System.out.println("Puzzle broke an assumption");
//		}
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
		
		int green = 0;
		int red = 0;
		
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
	
	public void showSolvable() {
		solvable.setOpacity(1);
		notSolvable.setOpacity(0);
	}
	
	public void showNotSolvable() {
		solvable.setOpacity(0);
		notSolvable.setOpacity(1);
	}
	
	public void backToMenuButtonPushed(ActionEvent event) throws IOException {
		View.window.setScene(View.sceneMenu);
		View.window.show();
		View.window.centerOnScreen();
	}
	
	public void initialize() throws IOException {
		 View.window.sceneProperty().addListener(new ChangeListener<Scene>() {
			 @Override
			 public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
				 pane.getChildren().clear();
				 if (newScene == View.sceneCanvas) {
					 try {
						puzzleSetup(filename);
						if (solveable()) {
							showSolvable();
						} else {
							showNotSolvable();
						}
					} catch (IOException e) {
						 e.printStackTrace();
					}
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
		
		// Initialize pieces
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece piece = puzzle.getPiece(i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
			piece.setStaticPoints();
			initializePiece(piece, pane);
		}
		puzzlesolver = new Puzzlesolver(this);
		
		Object[] children = groups.getChildren().toArray();
		
		for (Object groups : children) {
			Group group = (Group) groups; 
			
			double diffX = 500 - group.getLayoutBounds().getCenterX();
			double diffY = 250 - group.getLayoutBounds().getCenterY();
			
			group.setTranslateX(diffX);
			group.setTranslateY(diffY);
			
			Piece piece = (Piece) group.getChildren().get(0);
			piece.updatePoints(diffX, diffY);
			
		}
	}

	private void initializePiece(Piece piece, Pane pane) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setCursor(Cursor.HAND);
		piece.setFill(DEFAULT_COLOR);

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
					
					// Position of piece wrt. the difference between starting position and matched position
					originalTX = ((Polygon) event.getSource()).getTranslateX();
					originalTY = ((Polygon) event.getSource()).getTranslateY();
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					//Position of cursor wrt. top left
					originalX = event.getSceneX();
					originalY = event.getSceneY();
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
					
					// Position of piece wrt. starting position
					double deltaGX = originalGX + deltaX;
					double deltaGY = originalGY + deltaY;
					
					// Position of piece wrt. picked up position
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
						Piece piece = (Piece) element;
						piece.updateGroupRotate(parent.getRotate(), parent);
					}
				}
			}
		});
		
		Group group = new Group();
		group.getChildren().add(piece);
		groups.getChildren().add(group);
	}

	public void matchPoints(Piece a, Piece b, int threshold, int snap_range) {
	
		if(!puzzle.getSnapable()) {	
			return;
		}
		
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
			
			a.updatePointsRotate(a.getRotate());
			b.updatePointsRotate(b.getRotate());
			
			//Distances between matching points in each piece
			double[] distances = new double[amountOfCorners];
			
			boolean notEqualDistances = true;
			double dx = 0;
			double dy = 0;
			double rot = 0;
			
			while (notEqualDistances && rot <= 360) {

				for (int i = 0; i < amountOfCorners * 2; i = i + 2) {
					//Distances are calculated
					dx = points.get(i + 1).getX() - points.get(i).getX();
					dy = points.get(i + 1).getY() - points.get(i).getY();
					distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
				}
				
				notEqualDistances = false;
				
				for (int i = 0; i < distances.length - 1; i ++) {
                    if (Math.abs(distances[i] - distances[i + 1]) > 0.1) {
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
	
				if (piece != a) {
					piece.setRotate(a.getRotate() + piece.getRotate());
				} 
				A.getChildren().remove(piece);
				B.getChildren().add(piece);
			}
			
			for (Object element : B.getChildren().toArray()) {
				Piece piece = (Piece) element;
				if (piece == a) {
					piece.updatePieceRotate(piece.getRotate());
				} 
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
		
		//Distances should be same when orientation fits
		while (notEqualDistances) {
			if(rot == 360) {
				break;
			}
			
			for (int i = 0; i < 6; i+=2) {
				//Distances are calculated
				dx = points.get(i).getX() - points.get(i+1).getX();
				dy = points.get(i).getY() - points.get(i+1).getY();
				distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
			}
			
			notEqualDistances = false;
			
			for (int i = 0; i < distances.length - 1; i ++) {
                if (Math.abs(distances[i] - distances[i + 1]) > 0.05) {
                    a.setRotate(rot);
                	a.updateGroupRotate2(rot, A);
                    notEqualDistances = true;
                    break;
                }
			}
			rot += 0.01;
		}
		
		for (Object element : A.getChildren().toArray()) {
			Piece piece = (Piece) element;
			
			double newX = piece.getTranslateX() + dx;
			double newY = piece.getTranslateY() + dy;
			piece.setTranslateX(newX);
			piece.setTranslateY(newY);
			if (piece != a) {
				piece.setRotate(a.getRotate() + piece.getRotate());
			} 
			A.getChildren().remove(piece);
			B.getChildren().add(piece);
		}
		
		for (Object element : B.getChildren().toArray()) {
			Piece piece = (Piece) element;
			if (piece == a) {
				piece.updatePieceRotate(piece.getRotate());
			} 
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
