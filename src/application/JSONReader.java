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
	
	public JSONReader() {
		
		JSONParser parser = new JSONParser();
	
		try {
			Object obj = parser.parse(new FileReader("Puzzles/Puzzle-1r-2c-0995.json"));
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
			
			while (pieceIterator.hasNext()) {
				JSONObject pieces = pieceIterator.next();

				Piece piece = new Piece();
				long number = (long) pieces.get("piece");
				piece.setNumber(number);
				
				JSONArray cornerArray = (JSONArray) pieces.get("corners");
				Iterator<JSONObject> cornerIterator = cornerArray.iterator();
				while (cornerIterator.hasNext()) {

					JSONObject corners = (JSONObject) cornerIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");
					
					double x = (double) coordinate.get("x") * 100;
					double y = (double) coordinate.get("y") * 100;
					piece.getPoints().addAll(x, y);
				
				}
				
				puzzle.addPieceToArray(piece);
			}
		} catch (IOException | ParseException e) {
		}
	}
	
	public Puzzle getPuzzle() {
		return puzzle;
	}
	
}
