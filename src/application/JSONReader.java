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
	// private int previousCorners = 0;
	private int corners = 0;
	private String name;

	private String file;

	private int matches;
	private int maxNoCorners = 0;
	private long noOfPieces;

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

				Iterator<JSONObject> formIterator = formArray.iterator();
				while (formIterator.hasNext()) {
					JSONObject corners = (JSONObject) formIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");

					double x = (double) coordinate.get("x") * 100;
					double y = (double) coordinate.get("y") * 100;
					puzzle.getPoints().addAll(x, y);

					name = (String) jsonObject.get("name");
					name = name.substring(0, name.length() - 5);
					puzzle.setName(name);
				}
			}

			noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setNoOfPieces(noOfPieces); // If running piecelist, increment this number by 10

			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();

			while (pieceIterator.hasNext()) {

				JSONObject pieces = pieceIterator.next();
				Piece piece = new Piece();

				JSONArray cornerArray = (JSONArray) pieces.get("corners");
				Iterator<JSONObject> cornerIterator = cornerArray.iterator();
				int counter = 0;
				while (cornerIterator.hasNext()) {
					counter++;
					corners = corners + 1;
					JSONObject corners = (JSONObject) cornerIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");

					double x = (double) coordinate.get("x") * 100;
					double y = (double) coordinate.get("y") * 100;

					piece.getPoints().addAll(x + 400, y + 400);
				}
				if(counter > maxNoCorners) {
					maxNoCorners = counter;
				}
				puzzle.addPieceToArray(piece);
			}

			if (start.equals("Pu") && !file.contains("PieceList") && !file.contains("checkIdentical") && !file.contains("Spejlvendt")) {
				if(noOfPieces < 3) {
					matches = (int) Math.ceil(maxNoCorners / 2.0);
				} else if(noOfPieces < 9) {
					matches = maxNoCorners/2;
				} else {
					matches = (maxNoCorners+4)/4;
				}
				puzzle.setSolveable(true);
				//System.out.println(maxNoCorners);
				//System.out.println("new matches: " + matches);
			} else {
				puzzle.setSolveable(false);
			}
		} catch (IOException | ParseException e) {
		}
	}

	public void setFile(String input) {
		if (input == null) {
			file = "Puzzles/Puzzle-1r-2c-0995.json";
		} else {
			file = "Puzzles/" + input + ".json";
		}
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public int getMatches() {
		return matches;
	}

}
