package application;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.geom.Shape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class Puzzlesolver {
	private Puzzle puzzle;
	private int sideMatch;
	private int noOfMatches = 0;
	private int[] matchingPieces;
	private boolean[][] matchingPieces2;
	private Point2D[][][] matchingPoints;
	private int[][][] indexes;
	private JSONReader js;
	CanvasController canvasController;
	// Group groups = new Group();

	public Puzzlesolver(CanvasController canvasController) {
		js = CanvasController.getReader();
		puzzle = js.getPuzzle();
		sideMatch = js.getMatches() - 1;
		matchingPieces = new int[2];
		
		matchingPieces2 = new boolean[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()];
		matchingPoints = new Point2D[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()][6];
		indexes = new int[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()][2];
		this.canvasController = canvasController;
	}

	public void solvePuzzle(boolean hint, boolean solve) {
		// Loop for current piece
		for (int i = 0; i < puzzle.getNoOfPieces() - 1; i++) {
			Piece currPiece = puzzle.getPiece(i);
			ArrayList<Double> currLenghts = new ArrayList<Double>(currPiece.getLengths());

			// Loop for all sublists for current piece
			for (int j = 0; j < currPiece.getPointList().size(); j++) {
				List<Double> currSublist = currLenghts.subList(0, sideMatch);

				// Loop for all other pieces
				for (int k = i + 1; k < puzzle.getNoOfPieces(); k++) {
					Piece otherPiece = puzzle.getPiece(k);
					ArrayList<Double> otherLengths = new ArrayList<Double>(otherPiece.getLengths());

					// Loop for all sublists for other piece, and match them
					for (int h = 0; h < otherPiece.getPointList().size(); h++) {
						List<Double> otherSublist = otherLengths.subList(0, sideMatch);
						List<Double> otherSublistRev = new ArrayList<Double>(otherSublist);
						Collections.reverse(otherSublistRev);
						if (!currPiece.getParent().equals(otherPiece.getParent())) {
							if (closeEnoughLists(currSublist, otherSublistRev)) {
								if (checkMiddleAngles(currPiece, otherPiece, j, h)) {
									if (!currPiece.getParent().getChildrenUnmodifiable().contains(otherPiece)
											|| !otherPiece.getParent().getChildrenUnmodifiable().contains(currPiece)) {
										System.out.println("match between piece " + i + " and piece " + k
												+ " with values j: " + j + " and h: " + h);
										noOfMatches++;
										if (hint) {
											matchingPieces[0] = i;
											matchingPieces[1] = k;
											return;
										} else if (!solve) {
											Group A = (Group) currPiece.getParent();
											Group B = (Group) otherPiece.getParent();

											for (Object subgroup : A.getChildren().toArray()) {
												Piece piece = (Piece) subgroup;

												A.getChildren().remove(piece);
												B.getChildren().add(piece);
											}

											continue;
										}
										// Gets 2 points that match
										Point2D point1 = currPiece.getPointList().get(j + 1);
										Point2D point2 = otherPiece.getPointList().get(h + sideMatch - 1);
										Point2D point3 = currPiece.getPointList().get(j);
										Point2D point4 = otherPiece.getPointList().get((h + sideMatch)%otherPiece.getPointList().size());
										Point2D point5 = currPiece.getPointList().get((j + 2)%currPiece.getPointList().size());
										Point2D point6 = otherPiece.getPointList().get((h + sideMatch-2)%otherPiece.getPointList().size());

										matchingPieces2[i][k] = true;
										matchingPoints[i][k][0] = point1;
										matchingPoints[i][k][1] = point2;
										matchingPoints[i][k][2] = point3;
										matchingPoints[i][k][3] = point4;
										matchingPoints[i][k][4] = point5;
										matchingPoints[i][k][5] = point6;
										indexes[i][k][0] = j;
										indexes[i][k][1] = h;

//										otherPiece.getParent().setTranslateX(
//												otherPiece.getParent().getTranslateX() + point1.getX() - point2.getX());
//
//										otherPiece.getParent().setTranslateY(
//												otherPiece.getParent().getTranslateY() + point1.getY() - point2.getY());
//
//										otherPiece.updatePoints(point1.getX() - point2.getX(),
//												point1.getY() - point2.getY());
//
//										CanvasController.matchPoints(currPiece, otherPiece, sideMatch + 1, 1);
									}
								}
							}
						}
						shift(otherLengths);
					}
				}
				shift(currLenghts);
			}
		}
		String solveable;

		// Matches the first point
		for(int j = 0; j < puzzle.getNoOfPieces(); j++) {
			if(matchingPieces2[0][j]) {
				Piece currPiece = puzzle.getPiece(0);
				Piece otherPiece = puzzle.getPiece(j);
				
				//currPiece and otherPiece points respectively
				Point2D point1 = matchingPoints[0][j][0];
				Point2D point2 = matchingPoints[0][j][1];
				
				currPiece.setFill(Color.RED);
				otherPiece.setFill(Color.BLUE);
				
				otherPiece.getParent().setTranslateX(
						otherPiece.getParent().getTranslateX() + point1.getX() - point2.getX());

				otherPiece.getParent().setTranslateY(
						otherPiece.getParent().getTranslateY() + point1.getY() - point2.getY());

				otherPiece.updatePoints(point1.getX() - point2.getX(),
						point1.getY() - point2.getY());
				
				for(int g = 0; g < 6; g++) {
					Circle circle1 = new Circle(matchingPoints[0][j][g].getX(), matchingPoints[0][j][g].getY(),5);
					canvasController.getPane().getChildren().addAll(circle1);
				}
				
				ArrayList<Point2D> points = new ArrayList<Point2D>();
				for(int i = 0; i < 6; i++) {
					points.add(matchingPoints[0][j][i]);
				}
				
				canvasController.powerMatchPoints(currPiece, otherPiece, points);
				
				
				//System.out.println("j is " + indexes[0][j][0]);
//				System.out.println("h is " + indexes[0][j][1]);
//				System.out.println((indexes[0][j][1] + sideMatch - 1)*2);
				
				
//				Rotate rotate = new Rotate();
//				//4 and 5
//				rotate.setPivotX(otherPiece.getPoints().get((indexes[0][j][1] + sideMatch - 1)*2));
//			    rotate.setPivotY(otherPiece.getPoints().get((indexes[0][j][1] + sideMatch - 1)*2+1));
//			    rotate.setAngle(180);
//				
//				otherPiece.getTransforms().addAll(rotate);
				
//				for(int i = 0; i < otherPiece.getPointList().size(); i++) {
//					Circle circle2 = new Circle(otherPiece.getPointList().get(i).getX(), otherPiece.getPointList().get(i).getY(),5);
//					canvasController.getPane().getChildren().addAll(circle2);
//				}
				
				
				
				
//				boolean matched = false;
//				double counter = 0;
//				//while(!matched) {
//					if(counter == 360) {
//						System.out.println("360");
//						System.out.println("sideMatch " + sideMatch);
//						break;
//					}
//					if(currPiece.getParent().getChildrenUnmodifiable().contains(otherPiece)) {
//						System.out.println("here");
//						matched = true;
//						break;
//					}
//					//System.out.println("here");					
//					CanvasController.matchPoints(currPiece, otherPiece, sideMatch + 1, 1);
//					Rotate rotate = new Rotate();
//					//4 and 5
//					rotate.setPivotX(otherPiece.getPoints().get((indexes[0][j][1] + sideMatch - 1)*2));
//				    rotate.setPivotY(otherPiece.getPoints().get((indexes[0][j][1] + sideMatch - 1)*2+1));
//				    rotate.setAngle(138);
//				    otherPiece.getTransforms().addAll(rotate);
//					
//					counter+=0.25;
//					otherPiece.updateGroupRotate(138, (Group) otherPiece.getParent());
					//System.out.println(rotateDeg);
//				    for(int i = 0; i < otherPiece.getPointList().size(); i++) {
//				    	Circle circle = new Circle(otherPiece.getPointList().get(i).getX(), otherPiece.getPointList().get(i).getY(), 5);
//				    	circle.setFill(Color.ORANGE);
//					    canvasController.getPane().getChildren().addAll(circle);
//				    }
//				    for(int i = 0; i < otherPiece.getPoints().size()-1; i++) {
//				    	Circle circle = new Circle(otherPiece.getPoints().get(i), otherPiece.getPoints().get(i+1), 5);
//				    	circle.setFill(Color.RED);
//					    canvasController.getPane().getChildren().addAll(circle);
//				    }
				}
			
//				for(int i = 0; i < otherPiece.getPointList().size(); i++) {
//			    	Circle circle = new Circle(otherPiece.getPointList().get(i).getX(), otherPiece.getPointList().get(i).getY(), 5);
//				    canvasController.getPane().getChildren().addAll(circle);
//			    }
			//}
		}
		
//		boolean solved = false;
//		while(!solved) {
//			for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
//				Piece pieceGroup = puzzle.getPiece(i);
//				if(pieceGroup.getParent().getChildrenUnmodifiable().size() < 2) {
//					continue;
//				}
//				for(int j = i+1; j < puzzle.getNoOfPieces(); j++) {
//					Piece otherPiece = puzzle.getPiece(j);
//					if(otherPiece.getParent().getChildrenUnmodifiable().size() > 1) {
//						continue;
//					}
//					
//				}
//			}
//		}

		double maxX = 0.0;
		double maxY = 0.0;

		for (int i = 0; i < js.getPuzzle().getNoOfPieces(); i++) {

			double currX = js.getPuzzle().getPiece(i).getTranslateX();
			double currY = js.getPuzzle().getPiece(i).getTranslateY();

			if (Math.abs(currX) > Math.abs(maxX)) {
				maxX = currX;
			}

			if (Math.abs(currY) > Math.abs(maxY)) {
				maxY = currY;
			}
		}

//		puzzle.getPiece(0).getParent().setTranslateX(-maxX / 2);
//		puzzle.getPiece(0).getParent().setTranslateY(-maxY / 2);

//		if(noOfMatches == puzzle.getNoOfPieces()-1) {
//			System.out.println("Puzzle was solveable");
//			return;
//		} 
//		System.out.println("Puzzle was not solveable");
	}

	public void giveHint() {
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getFill() == Color.LIGHTBLUE) {
				return;
			}
		}
		solvePuzzle(true, false);
		for (int i = 0; i < puzzle.getPiece(matchingPieces[0]).getParent().getChildrenUnmodifiable().size(); i++) {
			((javafx.scene.shape.Shape) puzzle.getPiece(matchingPieces[0]).getParent().getChildrenUnmodifiable().get(i))
					.setFill(Color.LIGHTBLUE);
		}
		for (int i = 0; i < puzzle.getPiece(matchingPieces[1]).getParent().getChildrenUnmodifiable().size(); i++) {
			((javafx.scene.shape.Shape) puzzle.getPiece(matchingPieces[1]).getParent().getChildrenUnmodifiable().get(i))
					.setFill(Color.LIGHTBLUE);
		}
	}

	public boolean solveable() {
		boolean returnValue = true;
		solvePuzzle(false, false);
		Object parent = puzzle.getPiece(0).getParent();
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getParent() == parent) {
				continue;
			} else {
				returnValue = false;
				break;
			}
		}

		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getParent() == null) {
				continue;
			}
			((Group) puzzle.getPiece(i).getParent()).getChildren().clear();
		}

		// currGroup.getChildren().clear();
		try {
			canvasController.puzzleSetup(canvasController.getFilename());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnValue;
	}

	private boolean closeEnoughLists(List<Double> list1, List<Double> list2) {
		for (int i = 0; i < list1.size(); i++) {
			if (Piece.closeEnough(list1.get(i), list2.get(i))) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	private ArrayList<Double> shift(ArrayList<Double> a) {
		int n = a.size();
		double temp = a.get(0);
		for (int i = 0; i < n; i++) {
			a.set(i, a.get((i + 1) % n));
		}
		a.set(n - 1, temp);
		return a;
	}

	private boolean checkMiddleAngles(Piece p1, Piece p2, int j, int h) {
		// p1 = curr, p2 = other
		double[] p1Angles = p1.getUnorderedAngles();
		double[] p2Angles = p2.getUnorderedAngles();
		int counter = 0;
		double angle1 = p1Angles[(j + 1) % p1.getPointList().size()];
		double angle2 = p2Angles[(h + sideMatch - 1) % p2.getPointList().size()];

		while (Piece.closeEnough(360, angle1 + angle2)) {
			j++;
			h--;
			counter++;
			angle1 = p1Angles[(j + 1) % p1.getPointList().size()];
			angle2 = p2Angles[(h + sideMatch - 1) % p2.getPointList().size()];
			continue;
		}

		if (counter == sideMatch - 1) {
			return true;
		}

		return false;
	}
}
