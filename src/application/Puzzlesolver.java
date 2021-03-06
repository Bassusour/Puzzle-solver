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
	private boolean solved;
	private Puzzle puzzle;
	private int sideMatch;
	private int[] twoMatchingPieces;
	private boolean[][] allMatchingPieces;
	private Point2D[][][] matchingPoints;
	private JSONReader js;
	private GameController canvasController;
	private boolean start = true;
	private ArrayList<Integer> matchNumberQueue = new ArrayList<Integer>();

	// Bastian P. + Victor A.
	public Puzzlesolver(GameController canvasController) {
		js = GameController.getReader();
		puzzle = js.getPuzzle();
		// Number of sides each piece has to match with
		sideMatch = js.getMatches() - 1;
		// Array to keep track of all matching pair of pieces
		allMatchingPieces = new boolean[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()];
		// Array to keep track of 6 matching points for all pairs of matching pieces
		matchingPoints = new Point2D[(int) puzzle.getNoOfPieces()][(int) puzzle.getNoOfPieces()][6];
		this.canvasController = canvasController;
		// Queue to keep track of what piece to match, starting with piece 0
		matchNumberQueue.add(0);
	}

	// Bastian P. + Victor A.
	public void solvePuzzle(boolean hint, boolean solve) throws IllegalArgumentException {
		// Checks if puzzle information has already been calculated
		if (!solved) {
			// Loop for current piece
			for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
				Piece currPiece = puzzle.getPiece(i);
				ArrayList<Double> currLenghts = new ArrayList<Double>(currPiece.getLengths());

				// Loop for all sublists for current piece
				for (int j = 0; j < currPiece.getPointList().size(); j++) {
					List<Double> currSublist = currLenghts.subList(0, sideMatch);

					// Loop for all other pieces
					for (int k = 0; k < puzzle.getNoOfPieces(); k++) {
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
												|| !otherPiece.getParent().getChildrenUnmodifiable()
														.contains(currPiece)) {
											// If only check if solvable, do not spend time to rotate pieces
											if (!solve) {
												Group A = (Group) currPiece.getParent();
												Group B = (Group) otherPiece.getParent();

												for (Object subgroup : A.getChildren().toArray()) {
													Piece piece = (Piece) subgroup;

													A.getChildren().remove(piece);
													B.getChildren().add(piece);
												}
											}

											// Save 6 matching points for 2 matching pieces
											Point2D point1 = currPiece.getPointList().get(j + 1);
											Point2D point2 = otherPiece.getPointList().get(h + sideMatch - 1);
											Point2D point3 = currPiece.getPointList().get(j);
											Point2D point4 = otherPiece.getPointList()
													.get((h + sideMatch) % otherPiece.getPointList().size());
											Point2D point5 = currPiece.getPointList()
													.get((j + 2) % currPiece.getPointList().size());
											Point2D point6 = otherPiece.getPointList()
													.get((h + sideMatch - 2) % otherPiece.getPointList().size());
											matchingPoints[i][k][0] = point1;
											matchingPoints[i][k][1] = point2;
											matchingPoints[i][k][2] = point3;
											matchingPoints[i][k][3] = point4;
											matchingPoints[i][k][4] = point5;
											matchingPoints[i][k][5] = point6;
											//save that these two pieces are matching
											allMatchingPieces[i][k] = true;
										}
									}
								}
							}
							// Shifts all elements 1 to the left
							shift(otherLengths);
						}
					}
					// Shifts all emenets 1 to the left
					shift(currLenghts);
				}
			}
			solved = true;
		}

		if (solve) {
			if(matchNumberQueue.isEmpty()) {
				return;
			}
			// Matches all single pieces to group
			for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
				int currNum = matchNumberQueue.get(0);
				for (int j = 0; j < puzzle.getNoOfPieces(); j++) {
					if (allMatchingPieces[currNum][j]) {
						if (!puzzle.getPiece(0).getParent().getChildrenUnmodifiable().contains(puzzle.getPiece(j))
								|| start) {
							start = false;

							// Two pieces to be matched
							Piece currPiece = puzzle.getPiece(currNum);
							Piece otherPiece = puzzle.getPiece(j);
							
							// Adds to queue
							matchNumberQueue.add(j);
							
							// If hint, only get first pair of matching pieces, color the group, and return
							if (hint) {
								System.out.println("currPiece is " + currPiece.getNumber() + " and otherPiece is " + otherPiece.getNumber());
								for (int k = 0; k < currPiece.getParent().getChildrenUnmodifiable().size(); k++) {
									((javafx.scene.shape.Shape) currPiece.getParent()
											.getChildrenUnmodifiable().get(k)).setFill(Color.LIGHTBLUE);
								}
								for (int k = 0; k < otherPiece.getParent().getChildrenUnmodifiable().size(); k++) {
									((javafx.scene.shape.Shape) otherPiece.getParent()
											.getChildrenUnmodifiable().get(k)).setFill(Color.LIGHTBLUE);
								}
								return;
							}
							
							ArrayList<Point2D> points = new ArrayList<Point2D>();
							for (int h = 0; h < 6; h++) {
								points.add(matchingPoints[currNum][j][h]);
							}
							// Matches points visually
							canvasController.powerMatchPoints(otherPiece, currPiece, points);
						}
					}
				}
				// Removes piece from queue
				if (matchNumberQueue.size() > 0) {
					matchNumberQueue.remove(0);
				}
			}

			// Centers puzzle after solved
			puzzle.getPiece(0).getParent()
					.setTranslateX(500 - puzzle.getPiece(0).getParent().getLayoutBounds().getCenterX());
			puzzle.getPiece(0).getParent()
					.setTranslateY(250 - puzzle.getPiece(0).getParent().getLayoutBounds().getCenterY());
		}

	}

	// Bastian P.
	public void giveHint() {
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getFill() == Color.LIGHTBLUE) {
				return;
			}
		}
		solvePuzzle(true, true);
	}

	// Bastian P.
	public boolean solveable() throws IllegalArgumentException {
		boolean returnValue = true;
		solvePuzzle(false, false);
		Object parent = puzzle.getPiece(0).getParent();
		for (int i = 1; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getParent() == parent) {
				continue;
			} else {
				returnValue = false;
				break;
			}
		}

		//Restets puzzle after check if solvable
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getParent() == null) {
				continue;
			}
			((Group) puzzle.getPiece(i).getParent()).getChildren().clear();
		}
		try {
			canvasController.puzzleSetup(canvasController.getFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	// Bastian P. + Victor A.
	// Checks if lenghts for two pieces
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

	// Bastian P. + Victor A.
	private ArrayList<Double> shift(ArrayList<Double> a) {
		int n = a.size();
		double temp = a.get(0);
		for (int i = 0; i < n; i++) {
			a.set(i, a.get((i + 1) % n));
		}
		a.set(n - 1, temp);
		return a;
	}

	// Bastian P. + Victor A.
	private boolean checkMiddleAngles(Piece p1, Piece p2, int j, int h) {
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
