package application;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {
	public JSONReader() {
		
		JSONParser parser = new JSONParser();
	
		try {
			Object obj = parser.parse(new FileReader("PieceList/PieceList01.json"));
			JSONObject jsonObject = (JSONObject) obj;
			
			Puzzle puzzle = new Puzzle();
			
			/*
			JSONObject puzzles = (JSONObject) jsonObject.get("puzzle"); //Fungerer ikke 
			JSONArray formArray = (JSONArray) puzzles.get("form"); //Fungerer ikke
			Iterator<JSONObject> formIterator = formArray.iterator();
			while (formIterator.hasNext()) {
				JSONObject corners = (JSONObject) formIterator.next();
				JSONObject coordinate = (JSONObject) corners.get("coord");
				
				double x = (double) coordinate.get("x");
				double y = (double) coordinate.get("y");
				puzzle.getPoints().addAll(x, y);
				
			}
			*/
			
			String name = (String) jsonObject.get("name");
			long noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setName(name);
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
					
					double x = (double) coordinate.get("x");
					double y = (double) coordinate.get("y");
					piece.getPoints().addAll(x, y);
					
				}
				
				puzzle.addPieceToArray(piece);
				
			}
		} catch (IOException | ParseException e) {
		}
	}
}
