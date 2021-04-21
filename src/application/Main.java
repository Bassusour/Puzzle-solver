package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;

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
	private int amountOfCorners = 5;
	private ArrayList<Circle> circles = new ArrayList<Circle>();
	private Group groups;

	@Override
	public void start(Stage stage) {

		puzzle = new JSONReader().getPuzzle();

		groups = new Group();
		pane = new Pane();
		pane.getChildren().add(groups);

		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, pane, i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
			System.out.println(piece.getPoints().toString());
		}
		
		Scene scene = new Scene(pane, width, height);
		stage.setScene(scene);
		stage.setTitle(puzzle.getName());
		stage.show();
	}

	private void unionPieces(Piece p1, Piece p2, Pane pane) {
		Path p3 = (Path) Polygon.union(p1, p2);

		// Array of points for the new polygon
		Double[] points = new Double[(p3.getElements().size() - 1) * 2];

		int i = 0;
		// going through all the path elements in the path and adding the x and y
		// coordinates to points
		for (PathElement el : p3.getElements()) {
			if (el instanceof MoveTo) {
				MoveTo mt = (MoveTo) el;
				points[i] = mt.getX();
				points[i + 1] = mt.getY();
			}
			if (el instanceof LineTo) {
				LineTo lt = (LineTo) el;
				points[i] = lt.getX();
				points[i + 1] = lt.getY();
			}
			i += 2;
		}

		// creating new Polygon with these points
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
				if (event.getButton() == MouseButton.PRIMARY) {
					
					System.out.println(groups.getChildren().toString());
					System.out.println("Piece's group: " + piece.getParent());
					
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
					
//					if (groups.getChildren().contains(piece)) {
//						groups.setTranslateX(deltaGX);
//						groups.setTranslateY(deltaGY);
//					} else {
//						((Polygon) (event.getSource())).setTranslateX(deltaTX); // transform the object
//						((Polygon) (event.getSource())).setTranslateY(deltaTY);
//					}
				}

				if (event.getButton() == MouseButton.SECONDARY) {
					double deltaY = event.getSceneY() - originalY;
					
					piece.getParent().setRotate(piece.getParent().getRotate() + deltaY);
					
//					if (groups.getChildren().contains(piece)) {
//						groups.setRotate(groups.getRotate() + deltaY);
//					} else {
//						piece.setRotate(piece.getRotate() + deltaY);
//					}
//					
					originalY = event.getSceneY();

					// System.out.println(Math.abs(piece.getRotate()) % 360);

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
							System.out.println("MEN JEG ER HER ");
							for (Point2D point : piece.getPointList()) {
								System.out.println("NU ER JEG HER");
								Circle circle = new Circle(point.getX(), point.getY(), 5);
								pane.getChildren().add(circle);
								
							}
							
						}
						
					}
					
//					if (groups.getChildren().contains(piece)) {
//						
//						for (Object o : groups.getChildren().toArray()) {
//							((Piece) o).updatePoints(event.getSceneX() - originalX, event.getSceneY() - originalY);
//						}
//						
//					} else {
//						piece.updatePoints(event.getSceneX() - originalX, event.getSceneY() - originalY);	
//					}
					
					
					
					
					/*
					for (int i = 0; i < circles.size(); i++) {

						circles.get(i).setCenterX(piece.getPointList().get(i).getX());
						circles.get(i).setCenterY(piece.getPointList().get(i).getY());

					}*/
					

					// System.out.println(piece.getCenterX() + " , " + piece.getCenterY());

					if (!groups.getChildren().contains(piece)) {
						
						for (Piece element : puzzle.getPieces()) {

							if (piece != element) {
								
								if (!parent.getChildren().contains(element)) {
									
									Shape intersect = Shape.intersect(piece, element);

									if (intersect.getBoundsInLocal().getWidth() > -25) {
										matchPoints(piece, element, amountOfCorners);
									}	
									
								}
								
//								if (solved.getChildren().contains(element)) {
//									
//									for (Object o : solved.getChildren().toArray()) {
//										
//										Shape intersect = Shape.intersect(piece, (Piece) o);
//
//										if (intersect.getBoundsInLocal().getWidth() > -snapRange) {
//											matchPoints(piece, element, amountOfCorners);
//										}
//										
//									}
//									
//								} else {
									
//									Shape intersect = Shape.intersect(piece, element);
//
//									if (intersect.getBoundsInLocal().getWidth() > -snapRange) {
//										matchPoints(piece, element, amountOfCorners);
//									}	
									
//								}

							}

						}
						
					}
					

				} else if (event.getButton() == MouseButton.SECONDARY) {

					// System.out.print("Before rotation: ");
					// System.out.println(piece.getPointList().toString());

					piece.updatePointsRotate(piece.getRotate());
					
				
					/*
					for (int i = 0; i < circles.size(); i++) {

						circles.get(i).setCenterX(piece.getPointList().get(i).getX());
						circles.get(i).setCenterY(piece.getPointList().get(i).getY());

					}*/
					

					// System.out.print("After rotation: ");
					// System.out.println(piece.getPointList().toString());

					/*
					double rotation = Math.abs(piece.getRotate()) % 360;
					if (rotation < 10 || rotation > 350) {
						piece.setRotate(0);
						piece.updatePointsRotate(piece.getRotate() * -1);
					} else {
						piece.setRotate(90);
						piece.updatePointsRotate(piece.getRotate() * -1);
					}*/
					

				}
			}
		});

//		pane.getChildren().add(piece);
		
		Group group = new Group();
		group.getChildren().add(piece);
		groups.getChildren().add(group);
		
		/*
		for (Point2D element : piece.getPointList()) {
			Circle point = new Circle(element.getX(), element.getY(), 5);
			pane.getChildren().add(point);
			circles.add(point);
		}
		*/

	}

	public void matchPoints(Piece a, Piece b, int threshold) {

		int matches = 0;

		double deltaX = 0;
		double deltaY = 0;
//		System.out.println("New one...");
//		System.out.println("Piece A POINTS : " + a.getPointList().toString());
//		System.out.println("Piece B POINTS : " + b.getPointList().toString());
//		
		ArrayList<Point2D> points = new ArrayList<Point2D>();

		for (Point2D pointA : a.getPointList()) {

			for (Point2D pointB : b.getPointList()) {

				if (pointA.getX() < pointB.getX() + snapRange) {
					if (pointA.getX() > pointB.getX() - snapRange) {
						if (pointA.getY() < pointB.getY() + snapRange) {
							if (pointA.getY() > pointB.getY() - snapRange) {

								//System.out.println(a.getPointList().toString());

								System.out.println("Match " + matches + " found");
								System.out.println("Point A: " + pointA.getX() + ", " + pointA.getY());
								System.out.println("Point B: " + pointB.getX() + ", " + pointB.getY());

								points.add(pointA);
								points.add(pointB);
								
								/*
								deltaX = pointB.getX() - pointA.getX();
								deltaY = pointB.getY() - pointA.getY();
								*/

								matches++;

							}
						}
					}
				}
			}
		}
		
		if (matches == threshold) {
			
			System.out.println("Snapped");
			
			Group A = (Group) a.getParent();
			Group B = (Group) b.getParent();
			
			if (A == B) { return; }
			
			// System.out.println("A. TranslateX: " + a.getParent().getTranslateX());
			// System.out.println("A. TranslateY: " + a.getParent().getTranslateY());
			
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
			
//			A.setTranslateX(A.getTranslateX() + dx);
//			A.setTranslateY(A.getTranslateY() + dy);
			
//			for (Object element : A.getChildren().toArray()) {
//				
//				Piece piece = (Piece) element;
//				piece.setTranslateX(piece.getTranslateX() + dx);
//				piece.setTranslateY(piece.getTranslateY() + dy);
//				piece.updatePoints(dx, dy);
//				
//			}
			
//			a.setTranslateX(a.getTranslateX() + dx);
//			a.setTranslateY(a.getTranslateY() + dy);
//			a.updatePoints(dx, dy);
		
			
//			if (a.getParent().equals(singles)) {
//				if (b.getParent().equals(singles)) {
//					Group group = new Group();
//					group.getChildren().add(a);
//					group.getChildren().add(b);
//					groups.getChildren().add(group);
//				}
//				if (b.getParent().getParent().equals(groups)) {
//					((Group) b.getParent()).getChildren().add(a);
//				}
//			} else {
//				
//			}
			
//			((Group) a.getParent()).getChildren().remove(a);
//			groups.getChildren().remove(a.getParent());
//			((Group) b.getParent()).getChildren().add(a);
			
			b.updatePoints(b.getParent().getTranslateX(), 
					       b.getParent().getTranslateY());
			
			
			for (Object element : A.getChildren().toArray()) {
				
				Piece piece = (Piece) element;
				boolean selected = piece.equals(a);
				
				double pieceTranslateX = dx;
				double pieceTranslateY = dy;

				piece.setTranslateX(piece.getTranslateX() + dx);
				piece.setTranslateY(piece.getTranslateY() + dy);
				
				if (selected) {
					piece.updatePoints(B.getTranslateX() + dx,
									   B.getTranslateY() + dy);
				} else {
					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + dx, 
							           B.getTranslateY() - A.getTranslateY() + dy);
					
				}
				
				
//				if (piece.equals(a)) {
//					piece.updatePoints(B.getTranslateX() + pieceTranslateX, 
//					           B.getTranslateY() + pieceTranslateY);
//				} else {
//					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + pieceTranslateX, 
//					           B.getTranslateY() - A.getTranslateY() + pieceTranslateY);
//				}
				
//				piece.updatePoints(B.getTranslateX() + pieceTranslateX, 
//						           B.getTranslateY() + pieceTranslateY);
				
				A.getChildren().remove(piece);
				B.getChildren().add(piece);
				
			}
			
//			B.getChildren().addAll(A.getChildren());
//			A.getChildren().removeAll(A.getChildren());
			
			System.out.println("Group A: " + A);
			System.out.println("Group B: " + B);
			
			groups.getChildren().remove(A);

			/*
			
			double translateX = deltaX - (a.getOriginalCenterX() - a.getCenterX());
			double translateY = b.getCenterY() - a.getOriginalCenterY();

			a.setTranslateX(translateX);
			a.setTranslateY(translateY);
			a.updatePoints(translateX, translateY);

			unionPieces(a, b, pane);

			pane.getChildren().remove(a);
			pane.getChildren().remove(b);
			
			*/

		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}

