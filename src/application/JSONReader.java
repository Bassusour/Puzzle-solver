package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {
	
	private Puzzle puzzle;
	private int previousCorners = 0;
	private int corners = 0;
	
	public JSONReader() {
		
		JSONParser parser = new JSONParser();
	
		try {
			Object obj = parser.parse(new FileReader("Puzzles/Puzzle-2r-2c-1430.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			puzzle = new Puzzle();
			
			JSONObject puzzles = (JSONObject) jsonObject.get("puzzle");
			JSONArray formArray = (JSONArray) puzzles.get("form");
			Iterator<JSONObject> formIterator = formArray.iterator();
			while (formIterator.hasNext()) {
				JSONObject corners = (JSONObject) formIterator.next();
				JSONObject coordinate = (JSONObject) corners.get("coord");
				
				double x = (double) coordinate.get("x") * 100;
				double y = (double) coordinate.get("y") * 100;
				puzzle.getPoints().addAll(x, y);
				//puzzle.setWidth(puzzle.getWidth() + x);
				//puzzle.setHeight(puzzle.getHeight() + y);
				
			}
			
			String name = (String) jsonObject.get("name");
			name = name.substring(0, name.length() - 5);
			puzzle.setName(name);
			
			long noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setNoOfPieces(noOfPieces);
			
			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();
			
			// Used for hard coded initial position for each piece
			int i = 0;
			
			while (pieceIterator.hasNext()) {
				
				if (corners > previousCorners) {
					previousCorners = corners;
				}
				
				JSONObject pieces = pieceIterator.next();

				Piece piece = new Piece();
				long number = (long) pieces.get("piece");
				piece.setNumber(number);
				
				JSONArray cornerArray = (JSONArray) pieces.get("corners");
				Iterator<JSONObject> cornerIterator = cornerArray.iterator();
				while (cornerIterator.hasNext()) {
					
					corners = corners + 1;

					JSONObject corners = (JSONObject) cornerIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");
					
					double x = (double) coordinate.get("x") * 100;
					double y = (double) coordinate.get("y") * 100;
					
					if (i == 0) {
						piece.getPoints().addAll(x + 50, y + 50);
					} else {
						piece.getPoints().addAll(x + 300, y + 50);
					}
					
					// piece.getPoints().addAll(x, y);
				
				}
				piece.setPoints(piece.getPoints());
				puzzle.addPieceToArray(piece);
				i++;
			}
			previousCorners = 2 + (previousCorners  - 4) % 4;
			System.out.println("Corner no. " + previousCorners);
		} catch (IOException | ParseException e) {
		}
	}
	
	public Puzzle getPuzzle() {
		return puzzle;
	}
	
}