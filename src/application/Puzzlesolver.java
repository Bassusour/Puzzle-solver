package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.javafx.geom.Shape;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Puzzlesolver {
	private Puzzle puzzle;
	private int sideMatch;
	private int noOfMatches = 0;
	private int[] matchingPieces;
	private JSONReader js;
	//Group groups = new Group();

	public Puzzlesolver() {
		js = CanvasController.getReader();
		puzzle = js.getPuzzle();
		sideMatch = js.getMatches() - 1;
		matchingPieces = new int[2];

//		System.out.println(CanvasController.groups.getChildren().size());
//		System.out.println();
//		
//		for(int i = 0; i < CanvasController.groups.getChildren().size(); i++) {
//			System.out.println(((Piece) ((Group) CanvasController.groups.getChildren().get(i)).getChildren().get(0)).getNumber());
//			Group group = new Group();
//			Piece piece = new Piece();
//			piece.getPoints().addAll(puzzle.getPiece(i).getPoints());
//			group.getChildren().add(piece);
//			groups.getChildren().add(group);
//			
//		}
//		System.out.println();
//		for(int i = 0; i < groups.getChildren().size(); i++) {
//			System.out.println(((Piece) ((Group) groups.getChildren().get(i)).getChildren().get(0)).getNumber());
//		}
		// System.out.println();
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
							if (closeEnoughLists(currSublist, otherSublist)
									|| closeEnoughLists(currSublist, otherSublistRev)) {
								if (checkMiddleAngles(currPiece, otherPiece, j, h)) {
//									if (!currPiece.getParent().getChildrenUnmodifiable().contains(otherPiece)
//											|| !otherPiece.getParent().getChildrenUnmodifiable().contains(currPiece)) {
										System.out.println("match between piece " + i + " and piece " + k
												+ " with values j: " + j + " and h: " + h);

										if (hint) {
											matchingPieces[0] = i;
											matchingPieces[1] = k;
											return;
										} else if (!solve) {
//											noOfMatches++;
											
											Group A = (Group) currPiece.getParent();
											Group B = (Group) otherPiece.getParent();
											
											for(int l = 0; l < CanvasController.groups.getChildren().size(); l++) {
												System.out.println("Canvas1 " + ((Piece) ((Group) CanvasController.groups.getChildren().get(l)).getChildren().get(0)).getNumber());
											}
											
											for (Object subgroup : A.getChildren().toArray()) {
												Piece piece = (Piece) subgroup;
												
												for(int l = 0; l < A.getChildren().size(); l++) {
													System.out.println("A " + ((Piece) A.getChildren().get(l)).getNumber());
												}
												for(int l = 0; l < B.getChildren().size(); l++) {
													System.out.println("B " + ((Piece) B.getChildren().get(l)).getNumber());
												}
												for(int l = 0; l < CanvasController.groups.getChildren().size(); l++) {
													System.out.println("Canvas2 " + ((Piece) ((Group) CanvasController.groups.getChildren().get(l)).getChildren().get(0)).getNumber());
												}
												
												A.getChildren().remove(piece);
												B.getChildren().add(piece);
											}
											
											continue;
										}

										// Gets two points that match
										Point2D point1 = currPiece.getPointList().get(j + 1);
										Point2D point2 = otherPiece.getPointList().get(h + sideMatch - 1);

										otherPiece.getParent().setTranslateX(
												otherPiece.getParent().getTranslateX() + point1.getX() - point2.getX());

										otherPiece.getParent().setTranslateY(
												otherPiece.getParent().getTranslateY() + point1.getY() - point2.getY());

										otherPiece.updatePoints(point1.getX() - point2.getX(),
												point1.getY() - point2.getY());

										CanvasController.matchPoints(currPiece, otherPiece, sideMatch + 1, 1);
									//}
								}
							}
						}
						shift(otherLengths);
					}
				}
				shift(currLenghts);
			}
		}
//		for(int i = 0; i < puzzle.getNoOfPieces()-1; i++) {
//		if(puzzle.getPiece(i).getParent() == puzzle.getPiece(i+1).getParent()) {
//			continue;
//		} else {
//			System.out.println("No solution");
//		}
//	}

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

		puzzle.getPiece(0).getParent().setTranslateX(-maxX / 2);
		puzzle.getPiece(0).getParent().setTranslateY(-maxY / 2);

//		for (int i = 0; i < noOfGroups; i++) {
//			// Group group = new Group();
//
//		}
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
//		puzzle.getPiece(matchingPieces[0]).setFill(Color.LIGHTBLUE);
//		puzzle.getPiece(matchingPieces[1]).setFill(Color.LIGHTBLUE);
	}

	public boolean solveable() {
		boolean returnValue = true;
		solvePuzzle(false, false);
		Object parent = puzzle.getPiece(0).getParent();
		for (int i = 0; i < puzzle.getNoOfPieces(); i++) {
			if (puzzle.getPiece(i).getParent() == parent) {
				continue;
			} else {
				returnValue = false;;
			}
		}
		
		// Split up all subgroups
		Group groups = new Group();
		for (int j = 0; j < puzzle.getNoOfPieces(); j++) {
			Group currGroup = (Group) puzzle.getPiece(j).getParent();
			for (Object element : currGroup.getChildren().toArray()) {
				if(groups.getChildren().size() == puzzle.getNoOfPieces()){
					break;
				}
				Group group = new Group();
				Piece piece = (Piece) element;
				currGroup.getChildren().remove(piece);
				group.getChildren().add(piece);
				groups.getChildren().add(group);
				// CanvasController.groups.getChildren().add(group);
				// canvasController.puzzleSetup(file);
				System.out.println("Removed piece " + piece.getNumber() + " with j as " + j);
				for(int k = 0; k < groups.getChildren().size(); k++) {
					System.out.println("group: " + ((Piece) ((Group) groups.getChildren().get(k)).getChildren().get(0)).getNumber());
				}
				
			}
			System.out.println("size: " + groups.getChildren().size());
		}
		
		//CanvasController.setGroups(groups);
		
		return returnValue;

//		solvePuzzle(false, false);
//		System.out.println(noOfMatches + " " + puzzle.getNoOfPieces());
//		if (noOfMatches == puzzle.getNoOfPieces()-1) {
//			return true;
//		} else {
//			return false;
//		}
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
