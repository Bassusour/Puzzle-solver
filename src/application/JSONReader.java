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
	private String name;
	
	private String file = "Puzzles/Puzzle-2r-2c-1430.json";
//	private String file = "PieceList/PieceList01.json";
	
	private static int matches;
	
	public JSONReader() {
		
		JSONParser parser = new JSONParser();
	
		try {
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;
			
			puzzle = new Puzzle();
			
			String start = file.substring(0,2);
			
			if (start.equals("Pu")) {
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
					
					name = (String) jsonObject.get("name");
					name = name.substring(0, name.length() - 5);
					puzzle.setName(name);
					
				}
			}
			
			
			long noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setNoOfPieces(noOfPieces+15);
			
			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();
			
			// Used for hard coded initial position for each piece
			int i = 0;
			int bufferX = 25;
			int bufferY = 25;
			
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
					
					piece.getPoints().addAll(x + bufferX, y + bufferY);
				
				}
				
				if (bufferX > 1000 - 325) {
					bufferX = 25;
					bufferY = bufferY + 150;
				} else {
					bufferX = bufferX + 150;
				}
				
				puzzle.addPieceToArray(piece);
				
				i++;
			}
			
			if (start.equals("Pu")) {
				int first = name.indexOf('-');
				int second = name.indexOf('-', first + 1);
				int third = name.indexOf('-', second + 1);
				
				String rows = name.substring(first + 1, second - 1);
				String columns = name.substring(second + 1, third - 1);
				
				int fourpiece = (Integer.parseInt(rows) - 1) * (Integer.parseInt(columns) - 1);
				int twopiece = (corners - 4 - fourpiece) / 2;
				int sides = (Integer.parseInt(rows) - 1) * Integer.parseInt(columns) +
							(Integer.parseInt(columns) - 1) * Integer.parseInt(rows);
				
				matches = (twopiece + (fourpiece * 4)) / sides;
							
				previousCorners = 2 + (previousCorners  - 4) % 4;
			}
			
		} catch (IOException | ParseException e) {
		}
	}
	
	public Puzzle getPuzzle() {
		return puzzle;
	}
	
	public static int getMatches() {
		return matches;
	}
	
}
