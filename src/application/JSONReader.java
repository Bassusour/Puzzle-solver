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
			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();
			while (pieceIterator.hasNext()) {
				JSONObject piece = pieceIterator.next();
				System.out.println("Piece: " + (long) piece.get("piece"));
				JSONArray cornerArray = (JSONArray) piece.get("corners");
				Iterator<JSONObject> cornerIterator = cornerArray.iterator();
				while (cornerIterator.hasNext()) {
					JSONObject coords = (JSONObject) cornerIterator.next();
					JSONObject coord = (JSONObject) coords.get("coord");
					System.out.println("X:" + (double) coord.get("x"));
					System.out.println("Y:" + (double) coord.get("y"));
				}
			}
		} catch (IOException | ParseException e) {
		}
	}
}
