package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

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
	double originalGX;
	double originalGY;

	private int snapRange = 10;
	private int amountOfCorners = 0;
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private Group groups;

	@Override
	public void start(Stage stage) {

		puzzle = new JSONReader().getPuzzle();
		amountOfCorners = JSONReader.getMatches();
		groups = new Group();
		pane = new Pane();
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

		
		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.setTitle(puzzle.getName());
		stage.show();
	}

	private void initializePiece(Piece piece, Pane pane, int i) {
		piece.setStroke(Color.LIGHTGRAY);
		piece.setFill(Color.BISQUE);
		piece.setCursor(Cursor.HAND);

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
					
//					for (Object element : groups.getChildren().toArray()) {
//						
//						Group group = (Group) element;
//						
//						for (Object things : group.getChildren().toArray()) {
//							
//							Piece piece = (Piece) things;
//							for (Point2D point : piece.getPointList()) {
//								Circle circle = new Circle(point.getX(), point.getY(), 5);
//								pane.getChildren().add(circle);
//								
//							}
//							
//						}
//						
//					}
					
					if (!groups.getChildren().contains(piece)) {
						
						for (Piece element : puzzle.getPieces()) {
							//Some elements are null, since Piece numbers start at 1 or are not properly incremented (1-2-13-14)
							if(element == null) {
								continue;
							}

							if (piece != element) {
								
								if (!parent.getChildren().contains(element)) {
									
									Shape intersect = Shape.intersect(piece, element);

									if (intersect.getBoundsInLocal().getWidth() > -25) {
										matchPoints(piece, element, amountOfCorners);
									}	
								}
							}
						}
					}
					

				} else if (event.getButton() == MouseButton.SECONDARY) {

					piece.updatePointsRotate(piece.getRotate());

				}
			}
		});
		
		Group group = new Group();
		group.getChildren().add(piece);
		groups.getChildren().add(group);

	}

	public void matchPoints(Piece a, Piece b, int threshold) {

		int matches = 0;
		
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		for (Point2D pointA : a.getPointList()) {

			for (Point2D pointB : b.getPointList()) {

				if (pointA.getX() < pointB.getX() + snapRange) {
					if (pointA.getX() > pointB.getX() - snapRange) {
						if (pointA.getY() < pointB.getY() + snapRange) {
							if (pointA.getY() > pointB.getY() - snapRange) {

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
			
			a.setRotate(Math.ceil(a.getRotate()));
			a.updatePointsRotate(a.getRotate());

			b.setRotate(Math.ceil(b.getRotate()));
			b.updatePointsRotate(b.getRotate());
			
			
			double[] distances = new double[amountOfCorners];
			
			boolean notEqualDistances = true;
			
			double dx = 0;
			double dy = 0;
			
			while (notEqualDistances) {
				
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
						a.setRotate(Math.ceil(a.getRotate()) + 1);
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

		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

