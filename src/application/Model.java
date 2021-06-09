package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Model {
	private Puzzle puzzle;
	AnchorPane anchor;
	private int amountOfCorners = 0;
	private int snapRange = 10;
	private double originalX;
	private double originalY;
	private double originalTX;
	private double originalTY;
	private double deltaTX;
	private double deltaTY;
	private double originalGX;
	private double originalGY;
	private static JSONReader jr;

	public Model() {
		jr = new JSONReader();
		puzzle = jr.getPuzzle();
        amountOfCorners = JSONReader.getMatches();
        //System.out.println(amountOfCorners);
        
        anchor = new AnchorPane();
		// Initializes all the pieces from puzzle
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i) == null) {
				continue;
			}
			Piece piece = puzzle.getPiece(i);
			initializePiece(piece, i);
			piece.setOriginalCenterX(piece.getCenterX());
			piece.setOriginalCenterY(piece.getCenterY());
			//System.out.println(piece.getPoints().toString());
		}

		// Checks if pieces are identical
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i) == null) {
				continue;
			}
			Piece p1 = puzzle.getPiece(i);
			for (int j = i + 1; j < puzzle.getNoOfPieces(); j++) {
				if (puzzle.getPiece(j) == null) {
					continue;
				}
				Piece p2 = puzzle.getPiece(j);
				if (p1.compareTo(p2) == 0) {
					System.out.print("(" + p1.getNumber() + "," + p2.getNumber() + ")" + " ");
				}
			}
		}
	}

	private void initializePiece(Piece piece, int i) {
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

					StackPane parent = (StackPane) piece.getParent();
					
					//Each element in StackPane
					for (Object element : parent.getChildren().toArray()) {
						if(element.getClass() == javafx.scene.text.Text.class || element.getClass() == javafx.scene.shape.Circle.class) {
							continue;
						}
						Piece piece = (Piece) element;

						piece.updatePoints(event.getSceneX() - originalX, event.getSceneY() - originalY);

					}

//							for (Object element : anchor.getChildren().toArray()) {
//								if(element.getClass() == javafx.scene.text.Text.class) {
//									continue;
//								}
//								StackPane group = (StackPane) element;
//								
//								
//								for (Object things : group.getChildren().toArray()) {
//									if(things.getClass() == javafx.scene.text.Text.class || things.getClass() == javafx.scene.shape.Circle.class) {
//										continue;
//									}
//									Piece piece = (Piece) things;
//									for (Point2D point : piece.getPointList()) {
//										Circle circle = new Circle(point.getX(), point.getY(), 5);
//										group.getChildren().add(circle);
//									}
//								}
//							}
					if (!anchor.getChildren().contains(piece)) { //groups

						for (Piece element : puzzle.getPieces()) {
							// Some elements are null, since Piece numbers start at 1 or are not properly
							// incremented (1-2-13-14)
							if (element == null) {
								continue;
							}

							if (piece != element) {

								if (!parent.getChildren().contains(element)) {

									Shape intersect = Shape.intersect(piece, element);

									if (intersect.getBoundsInLocal().getWidth() > -25) { //Change
										matchPoints(piece, element, amountOfCorners);
									}
								}
							}
						}
					}

				} else if (event.getButton() == MouseButton.SECONDARY) {

					StackPane parent = (StackPane) piece.getParent();
					for (Object element : parent.getChildren().toArray()) {
						if(element.getClass() == javafx.scene.text.Text.class) {
							continue;
						}
						((Piece) element).updateGroupRotate(parent.getRotate(), parent);
					}

//							for (Object element : groups.getChildren().toArray()) {
//								
//								Group group = (Group) element;
//								
//								for (Object things : group.getChildren().toArray()) {
//									
//									Piece piece = (Piece) things;
//									for (Point2D point : piece.getPointList()) {
//										Circle circle = new Circle(point.getX(), point.getY(), 5);
//										pane.getChildren().add(circle);
//										
//									}
//									
//								}
//								
//							}

				}

			}
		});
		Text text = new Text (piece.getNumber()+"");
		//Group group = new Group();
		StackPane stack = new StackPane();
		
		stack.getChildren().add(piece);
		stack.getChildren().add(text);
		
		//group.getChildren().add(piece);
		//group.getChildren().add(text);
//		System.out.println("pre:" + anchor.getChildren());
//		System.out.println(piece.getNumber() + " , " + text);
		anchor.getChildren().add(stack);
//		System.out.println("post:" + anchor.getChildren());
		
		//System.out.println(anchor.getChildren().contains(puzzle.getPiece(0)));
		//System.out.println(stack.getChildren().contains(puzzle.getPiece(1)));
		//System.out.println(stack.getChildren());
		//group.getChildren().add(stack);
		
		//groups.getChildren().add(stack);
		
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
			
			System.out.println((StackPane) a.getParent());
			StackPane A = (StackPane) a.getParent();
			StackPane B = (StackPane) a.getParent();
			//Group A = (Group) a.getParent();
			//Group B = (Group) b.getParent();

			if (A == B) {
				return;
			}

			a.setRotate(Math.round(a.getRotate()));
			a.updatePointsRotate(a.getRotate());

			b.setRotate(Math.round(b.getRotate()));
			b.updatePointsRotate(b.getRotate());

			double[] distances = new double[amountOfCorners];

			boolean notEqualDistances = true;

			double dx = 0;
			double dy = 0;

			while (notEqualDistances) {

				for (int i = 0; i < amountOfCorners * 2; i = i + 2) {

					dx = points.get(i + 1).getX() - points.get(i).getX();
					dy = points.get(i + 1).getY() - points.get(i).getY();
					distances[i / 2] = Math.sqrt(Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));

				}
				
				for (int i = 0; i < distances.length - 1; i++) {
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

			b.updatePoints(b.getParent().getTranslateX(), b.getParent().getTranslateY());

			for (Object element : A.getChildren().toArray()) {

				Piece piece = (Piece) element;
				boolean selected = piece.equals(a);

				piece.setTranslateX(piece.getTranslateX() + dx);
				piece.setTranslateY(piece.getTranslateY() + dy);

				if (selected) {
					piece.updatePoints(B.getTranslateX() + dx, B.getTranslateY() + dy);
				} else {
					piece.updatePoints(B.getTranslateX() - A.getTranslateX() + dx,
							B.getTranslateY() - A.getTranslateY() + dy);
				}

				A.getChildren().remove(piece);
				B.getChildren().add(piece);

			}

			anchor.getChildren().remove(A); //groups

			for (Object element : B.getChildren().toArray()) {
				((Piece) element).updateGroupRotate(B.getRotate(), B);
			}
		}
	}
	
	public static JSONReader getJSONReader() {
		return jr;
	}

	public AnchorPane getGroups() {
		return anchor;
	}
}
