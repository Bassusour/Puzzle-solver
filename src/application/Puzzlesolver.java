package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.geom.Shape;

import javafx.scene.shape.*;

public class Puzzlesolver {
	private Puzzle puzzle;
	int sideMatch;

	public Puzzlesolver() {
		JSONReader js = Model.getJSONReader();
		puzzle = js.getPuzzle();
		sideMatch = JSONReader.getMatches() - 1;

		// System.out.println("noOfPieces are " + puzzle.getNoOfPieces());

		// Loop for current piece
		for (int i = 0; i < puzzle.getNoOfPieces() - 1; i++) {
			Piece currPiece = puzzle.getPiece(i);
			ArrayList<Double> currLenghts = currPiece.getLengths();
			// System.out.println("currLenghts from piece " + i + " are: " + currLenghts +
			// "\n");

			// Loop for all sublists for current piece
			for (int j = 0; j < currPiece.getPointList().size(); j++) {
				List<Double> currSublist = currLenghts.subList(0, sideMatch);
				// System.out.println("CurrSublist is " + currSublist);

				// Loop for all other pieces
				for (int k = i + 1; k < puzzle.getNoOfPieces(); k++) {
					Piece otherPiece = puzzle.getPiece(k);
					ArrayList<Double> otherLengths = otherPiece.getLengths();

					// Loop for all sublists for other piece, and match them
					for (int h = 0; h < otherPiece.getPointList().size(); h++) {
						List<Double> otherSublist = otherLengths.subList(0, sideMatch);
						List<Double> otherSublistRev = new ArrayList<Double>(otherSublist);
						Collections.reverse(otherSublistRev);

						if (closeEnoughLists(currSublist, otherSublist)) {
							if (checkMiddleAngles(currPiece, otherPiece, j, h)) {
								System.out.println("match between piece " + i + " and piece " + k + " with values j: "
										+ j + " and h: " + h);
								
								System.out.println(otherPiece.getParent());
								
								// Gets two points that match
								Point2D point1 = currPiece.getPointList().get(j + 1);
								Point2D point2 = otherPiece.getPointList().get(h + sideMatch - 1);
//								double distance = Point2D.distance(point1.getX(), point1.getY(), point2.getX(),
//										point2.getY());
//								Vector vector = new Vector(point1, point2);
//								Vector xaxis = new Vector(point2.getX(), point2.getY(), point2.getX() - 1,
//										point2.getY());
//								double angle = (Vector.angle(vector, xaxis) * Math.PI) / 180;

								otherPiece.getParent().setTranslateX(otherPiece.getParent().getTranslateX()
										+ point1.getX() - point2.getX());

								otherPiece.getParent().setTranslateY(otherPiece.getParent().getTranslateY()
										+ point1.getY() - point2.getY());

								otherPiece.updatePoints(point1.getX() - point2.getX(), point1.getY() - point2.getY());

								// System.out.println(otherPiece.getParent().getTranslateX()+otherPiece.getTranslateX());

								Model.matchPoints(currPiece, otherPiece, sideMatch + 1);

								// currPiece.getParent().setTranslateX(otherPiece.getTranslateX()+point1.getX()-point2.getX());
							}
						}

						if (closeEnoughLists(currSublist, otherSublistRev)) {
							if (checkMiddleAngles(currPiece, otherPiece, j, h)) {
								System.out.println("match between piece " + i + " and piece " + k + " with values j: "
										+ j + " and h: " + h);
								
								System.out.println(otherPiece.getParent());
								
								// Gets two points that match
								Point2D point1 = currPiece.getPointList().get(j + 1);
								Point2D point2 = otherPiece.getPointList().get(h + sideMatch - 1);
//								double distance = Point2D.distance(point1.getX(), point1.getY(), point2.getX(),
//										point2.getY());
//								Vector vector = new Vector(point1, point2);
//								Vector xaxis = new Vector(point2.getX(), point2.getY(), point2.getX() - 1,
//										point2.getY());
//								double angle = (Vector.angle(vector, xaxis) * Math.PI) / 180;

								otherPiece.getParent().setTranslateX(otherPiece.getParent().getTranslateX()
										+ point1.getX() - point2.getX());

								otherPiece.getParent().setTranslateY(otherPiece.getParent().getTranslateY()
										+ point1.getY() - point2.getY());

								otherPiece.updatePoints(point1.getX() - point2.getX(), point1.getY() - point2.getY());

								// System.out.println(otherPiece.getParent().getTranslateX()+otherPiece.getTranslateX());

								Model.matchPoints(currPiece, otherPiece, sideMatch + 1);

								// currPiece.getParent().setTranslateX(otherPiece.getTranslateX()+point1.getX()-point2.getX());

							}
						}
						shift(otherLengths);
					}
				}
				// System.out.println();
				shift(currLenghts);
			}
		}
		
		
//		for(int i = 0; i < puzzle.getNoOfPieces()-1; i++) {
//			if(puzzle.getPiece(i).getParent() == puzzle.getPiece(i+1).getParent()) {
//				continue;
//			} else {
//				System.out.println("No solution");
//			}
//		}
		
		System.out.println(puzzle.getPiece(0).getTranslateX());
		System.out.println(puzzle.getPiece(0).getTranslateY());
		System.out.println(puzzle.getPiece(0).getParent());
		
//		puzzle.getPiece(0).getParent().setTranslateX(100);
//		puzzle.getPiece(0).getParent().setTranslateY(-7000);
		
		System.out.println(puzzle.getPiece(0).getParent().getTranslateX());
		System.out.println(puzzle.getPiece(0).getParent().getTranslateY());
		
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
