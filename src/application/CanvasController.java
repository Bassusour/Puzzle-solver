package application;

import javafx.geometry.Point2D;
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

	@FXML
	private MenuBar menuBar;
	@FXML
	private Pane pane;
	@FXML
	private MenuItem solve;

	private static String filename = "Puzzles/Puzzle-1r-2c-0995.json";

//	public static int width = 1000;
//	public static int height = 600;
	private static JSONReader reader;
	private static Puzzle puzzle;

	private double originalX;
	private double originalY;
	private double pressX;
	private double pressY;
	private static int snapRange = 10;
	private static int amountOfCorners = 0;
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
		if (puzzle.getSolveable()) {
			// System.out.println("Puzzle is solveable");
			puzzlesolver.solvePuzzle(false, true);
		} else {
			System.out.println("Puzzle is not solveable");
		}
		// showIdenticalPieces();
		// puzzlesolver.giveHint();
	}

	public boolean solveable() {
		return puzzlesolver.solveable();
	}

	public void puzzleHint() {
		puzzlesolver.giveHint();
	}

	public void showIdenticalPieces() {
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			puzzle.getPiece(i).setFill(DEFAULT_COLOR);
		}
		boolean[][] identicals = new boolean[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()];
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			for (int j = i + 1; j < puzzle.getNoOfPieces(); j++) {
				if (i != j) {
					if (puzzle.getPiece(i).compareTo(puzzle.getPiece(j)) == 0) {
						identicals[i][j] = true;
					}
				}
			}
		}
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getFill() == DEFAULT_COLOR) {
				puzzle.getPiece(i).setFill(Color.color(Math.random(), Math.random(), Math.random()));
			} else {
				continue;
			}
			for (int j = i + 1; j < puzzle.getNoOfPieces(); j++) {
				if (identicals[i][j]) {
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
					// System.out.println(filename);
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
//			piece.setOriginalCenterX(piece.getCenterX());
//			piece.setOriginalCenterY(piece.getCenterY());
			initializePiece(piece, pane);
		}

		puzzlesolver = new Puzzlesolver(this);
	}

	private void initializePiece(Piece piece, Pane pane) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setCursor(Cursor.HAND);
		piece.setFill(Color.RED);

		piece.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					originalX = event.getSceneX();
					originalY = event.getSceneY();
					pressX = event.getX();
					pressY = event.getY();
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

					Translate translate = new Translate();
					translate.setX(event.getX() - pressX);
					translate.setY(event.getY() - pressY);
					piece.getParent().getTransforms().add(translate);
					
					originalX = event.getSceneX();
					originalY = event.getSceneY();
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					double deltaY = event.getSceneY() - originalY;
					double pivotPointX = 0;
					double pivotPointY = 0;
					
					for (int i = 0; i < piece.getParent().getChildrenUnmodifiable().size(); i++) {
						Piece currPiece = (Piece) piece.getParent().getChildrenUnmodifiable().get(i);
						pivotPointX += currPiece.getLocalCenterX();
						pivotPointY += currPiece.getLocalCenterY();
					}

					pivotPointX = pivotPointX / piece.getParent().getChildrenUnmodifiable().size();
					pivotPointY = pivotPointY / piece.getParent().getChildrenUnmodifiable().size();

					for(int i = 0; i < piece.getParent().getChildrenUnmodifiable().size(); i++) {
						Piece currPiece = (Piece) piece.getParent().getChildrenUnmodifiable().get(i);
						currPiece.setGroupedPivotPointX(pivotPointX);
						currPiece.setGroupedPivotPointY(pivotPointY);
					}

					piece.getParent().getTransforms().add(new Rotate(deltaY, pivotPointX, pivotPointY));
					originalY = event.getSceneY();
				}
			}
		});

		piece.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					Group parent = (Group) piece.getParent();
					if (!groups.getChildren().contains(piece)) {
						for (Piece element : puzzle.getPieces()) {
							if (piece != element) {
								if (!parent.getChildren().contains(element)) {
									Shape intersect = Shape.intersect(piece, element);
									if (intersect.getBoundsInLocal().getWidth() > -10) {
										matchPoints(piece, element, amountOfCorners, snapRange, pane);
									}
								}
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

//	private double computeAngle(Piece piece, MouseEvent e) {
//        return new Point2D(piece.getLocalCenterX(), piece.getLocalCenterY())
//                   .angle(new Point2D(e.getX(), e.getY()));
//    }

	public static void matchPoints(Piece a, Piece b, int threshold, int snap_range, Pane pane) {
		if (!puzzle.getSolveable()) {
			return;
		}
		int matches = 0;

		ArrayList<Integer> indexes = new ArrayList<Integer>();

		for (int i = 0; i < a.getPoints().size() - 1; i += 2) {
			Point2D pointA = a.getLocalToSceneTransform().transform(a.getPoints().get(i),
					a.getPoints().get(i + 1));
			double pointAx = pointA.getX();
			double pointAy = pointA.getY();

			for (int j = 0; j < b.getPoints().size() - 1; j += 2) {
				if (a.getParent() == b.getParent()) {
					continue;
				}
				Point2D pointB = b.getLocalToSceneTransform().transform(b.getPoints().get(j),
						b.getPoints().get(j + 1));
				double pointBx = pointB.getX();
				double pointBy = pointB.getY();

				if (pointAx < pointBx + snap_range) {
					if (pointAx > pointBx - snap_range) {
						if (pointAy < pointBy + snap_range) {
							if (pointAy > pointBy - snap_range) {
//								Circle circle1 = new Circle(pointA.getX(), pointA.getY(), 5);
//								Circle circle2 = new Circle(pointB.getX(), pointB.getY(), 5);
								
								indexes.add(i);
								indexes.add(j);
								
//								points.add(pointA);
//								points.add(pointB);

								matches++;
							}
						}
					}
				}
			}
		}
		
		if (matches == threshold) {
			
			System.out.println("Match! wit piece a: " + a.getNumber() + " and piece b: " + b.getNumber());
			Group A = (Group) a.getParent();
			Group B = (Group) b.getParent();

			a.setFill(Color.RED);
			b.setFill(Color.BLUE);

			if (A == B) {
				return;
			}

//			double[] distances = new double[amountOfCorners];
//			
//			boolean notEqualDistances = true;
			for(int i = 0; i < A.getChildren().size(); i++) {
				i--;
				Piece piece = (Piece) A.getChildren().get(0);
				System.out.println("Piece is " + piece.getNumber());
				piece.setFill(Color.AQUA);
					B.getChildren().add(piece);
					double dx = -piece.getLocalToSceneTransform().transform(piece.getPoints().get(indexes.get(0)),
							piece.getPoints().get(indexes.get(0) + 1)).getX() + 
						b.getLocalToSceneTransform().transform(b.getPoints().get(indexes.get(1)),
							b.getPoints().get(indexes.get(1) + 1)).getX();
				
					double dy = -piece.getLocalToSceneTransform().transform(piece.getPoints().get(indexes.get(0)),
							piece.getPoints().get(indexes.get(0) + 1)).getY() + 
								b.getLocalToSceneTransform().transform(b.getPoints().get(indexes.get(1)),
									b.getPoints().get(indexes.get(1) + 1)).getY();
						
					Circle circle1 = new Circle(
							a.getLocalToSceneTransform().transform(a.getPoints().get(indexes.get(0)),
									a.getPoints().get(indexes.get(0) + 1)).getX(),
							a.getLocalToSceneTransform().transform(a.getPoints().get(indexes.get(0)),
									a.getPoints().get(indexes.get(0) + 1)).getY(), 5);
						
					Circle circle2 = new Circle(
							b.getLocalToSceneTransform().transform(b.getPoints().get(indexes.get(1)),
									b.getPoints().get(indexes.get(1) + 1)).getX(), 
							b.getLocalToSceneTransform().transform(b.getPoints().get(indexes.get(1)),
									b.getPoints().get(indexes.get(1) + 1)).getY(), 5);
					pane.getChildren().add(circle1);
					pane.getChildren().add(circle2);
						
					System.out.println(dx + " " + dy);
					if(!(piece.getNumber() == 0)) {
						piece.getTransforms().add(new Translate(dx, dy));
					} else {
						piece.getTransforms().add(new Translate(12.539073957032883, 84.50436511310551));
					}
					
				
				
				
//				piece.getTransforms().add(new Translate(-centerBX+centerAX, -centerBY+centerAY));
				
				
				
				//double dx = b.getLocalToSceneTransform().transform(b.getPoints().get(indexes.get(1)), dx) - a.getLocalToSceneTransform().transform(a.getPoints().get(indexes.get(0)), dx);
				
//				while (notEqualDistances) {
//					//System.out.println("asd");
//					for (int i = 0; i < amountOfCorners * 2; i+=2) {
//						dx = points.get(i + 1).getX() - points.get(i).getX();
//						dy = points.get(i + 1).getY() - points.get(i).getY();
//						distances[i/2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
//					}
//					
//					for (int i = 0; i < distances.length - 1; i ++) {
//						if (Math.abs(distances[i] - distances[i + 1]) < 0.1) {
//							notEqualDistances = false;
//							continue;
//						} else {
//							notEqualDistances = true;
////							a.setRotate(Math.round(a.getRotate()) + 1);
////							a.updatePointsRotate(a.getRotate());
//							break;
//						}
//					}
//				}
				
				A.getChildren().remove(piece);
			}
			//groups.getChildren().remove(A);
			

			
			

			if (a.getFill() == Color.LIGHTBLUE && b.getFill() == Color.LIGHTBLUE) {
				for (int i = 0; i < a.getParent().getChildrenUnmodifiable().size(); i++) {
					((Shape) a.getParent().getChildrenUnmodifiable().get(i)).setFill(Color.BISQUE);
				}
			}
			
//			for(int i = 0; i < A.getChildren().size(); i++) {
//			
//			Piece currPiece = (Piece) A.getChildren().get(i);
//			A.getChildren().remove(currPiece);
//			B.getChildren().add(currPiece);
//		}
//		groups.getChildren().remove(A);
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
