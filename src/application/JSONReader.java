package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {

	private Puzzle puzzle;
	private int corners = 0;
	private String name;
	private String file;
	private int matches;
	private int maxNoCorners = 0;
	private long noOfPieces;

	// Victor A. + Victor W.
	public JSONReader(String filename) {
		this.file = filename;
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;

			puzzle = new Puzzle();
			String start = file.substring(0, 2);

			if (start.equals("Pu") && !file.contains("PieceList") && !file.contains("checkIdentical")) {
				JSONObject puzzles = (JSONObject) jsonObject.get("puzzle");
				JSONArray formArray = (JSONArray) puzzles.get("form");
				
			}

			noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setNoOfPieces(noOfPieces); 

			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			
			// Goes through all pieces
			
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();

			while (pieceIterator.hasNext()) {
				JSONObject pieces = pieceIterator.next();
				Piece piece = new Piece();
				JSONArray cornerArray = (JSONArray) pieces.get("corners");
				
				// Goes through all corners
				
				Iterator<JSONObject> cornerIterator = cornerArray.iterator();
				int counter = 0;

				while (cornerIterator.hasNext()) {
					counter++;
					corners = corners + 1;
					JSONObject corners = (JSONObject) cornerIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");
					double x = (double) coordinate.get("x") * 100;
					double y = (double) coordinate.get("y") * 100;
					piece.getPoints().addAll(x + 400, y + 200);
				}
				if (counter > maxNoCorners) {
					maxNoCorners = counter;
				}
				puzzle.addPieceToArray(piece);
			}

			// Calculates the required number of matches for the puzzle (assumptions are made here)
			if (start.equals("Pu") && !file.contains("PieceList") && !file.contains("checkIdentical")
					&& !file.contains("Spejlvendt")) {
				if (noOfPieces < 3) {
					matches = (int) Math.ceil(maxNoCorners / 2.0);
				} else if (noOfPieces < 9) {
					matches = maxNoCorners / 2;
				} else {
					matches = (maxNoCorners + 4) / 4;
				}
				puzzle.setSnapable(true);
			} else {
				puzzle.setSnapable(false);
			}
		} catch (IOException | ParseException e) {
		}
	}

	// Victor W.
	public void setFile(String input) {
		if (input == null) {
			file = "Puzzles/Puzzle-1r-2c-0995.json";
		} else {
			file = input;
		}
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public int getMatches() {
		return matches;
	}
}
