package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzlesolver {
	private Puzzle puzzle;
	
	public Puzzlesolver() {
		JSONReader js = new JSONReader();
		puzzle = js.getPuzzle();
		int corners = JSONReader.getMatches();
		
		//Loop for current piece
		for(int i = 0; i < puzzle.getNoOfPieces(); i++) {
			Piece currPiece = puzzle.getPiece(i);
			ArrayList<Double> currLenghts = currPiece.getLengths();
			
			//Loop for all other pieces
			for(int k = i+1; k < puzzle.getNoOfPieces(); k++) {
				Piece otherPiece = puzzle.getPiece(k);
				ArrayList<Double> otherLengths = otherPiece.getLengths();
				
				//Loop for all sublists for currrent piece
				for(int j = 0; j < currPiece.getPointList().size(); j++) {
					List currSublist = currLenghts.subList(j, corners + j);
					
					//Loop for all sublists for other piece, and match them
					for(int h = 0; h < otherPiece.getPointList().size(); h++) {
						List otherSublist = otherLengths.subList(h, corners + h);
						if(currSublist.equals(otherSublist)) {
							//check angles
						} 
						Collections.reverse(otherSublist);
						if(currSublist.equals(otherSublist)) {
							//check angles
						} 
					}
				}
			}
		}
	}
	
	private boolean checkMiddleAngles(Piece p1, Piece p2, int h) {
		
		
		return false;
	}
}
