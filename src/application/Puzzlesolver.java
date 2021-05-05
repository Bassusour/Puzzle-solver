package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzlesolver {
	private Puzzle puzzle;
	int corners;
	
	public Puzzlesolver() {
		JSONReader js = Model.getJSONReader();
		puzzle = js.getPuzzle();
		corners = JSONReader.getMatches();
		
		//System.out.println("noOfPieces are " + puzzle.getNoOfPieces());
		
		//Loop for current piece
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece currPiece = puzzle.getPiece(i);
			ArrayList<Double> currLenghts = currPiece.getLengths();
			
			//Loop for all other pieces
			for(int k = i+1; k < (puzzle.getNoOfPieces()); k++) {
				Piece otherPiece = puzzle.getPiece(k);
				ArrayList<Double> otherLengths = otherPiece.getLengths();
				
				//Loop for all sublists for currrent piece
				for(int j = 0; j < currPiece.getPointList().size(); j++) {
					List currSublist = currLenghts.subList(j, corners + j);
					System.out.println("CurrSublist is " + currSublist);
					//Loop for all sublists for other piece, and match them
					for(int h = 0; h < otherPiece.getPointList().size(); h++) {
						List otherSublist = otherLengths.subList(h, corners + h);
						System.out.println("otherSublist is " + otherSublist);
						if(currSublist.equals(otherSublist)) {
							if(checkMiddleAngles(currPiece, otherPiece, h)) {
								System.out.println("Piece" + currPiece.getNumber() + " can match with piece" + otherPiece.getNumber());
							} else {
								continue;
							}
						} 
						Collections.reverse(otherSublist);
						if(currSublist.equals(otherSublist)) {
							if(checkMiddleAngles(currPiece, otherPiece, h)) {
								System.out.println("Piece" + currPiece.getNumber() + " can match with piece" + otherPiece.getNumber());
							} else {
								continue;
							}
						}
						System.out.println();
					}
				}
			}
		}
	}
	
	private boolean checkMiddleAngles(Piece p1, Piece p2, int h) {
		double[] p1Angles = p1.getUnorderedAngles();
		double[] p2Angles = p2.getUnorderedAngles();
		
		for(int i = h; i < corners + h; i++) {
			if(p1Angles[i] != p2Angles[i]) {
				return false;
			}
		}
		return true;
	}
}
