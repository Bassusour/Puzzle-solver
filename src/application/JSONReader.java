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
	private int previousCorners = 0;
	private int corners = 0;
	private String name;
	
	private String file = "Puzzles/Puzzle-1r-2c-0995.json";
	
	private int matches;
	
	public JSONReader(String filename) {
		
		this.file = filename;
		
		JSONParser parser = new JSONParser();
		System.out.println(file);
	
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
					
					name = (String) jsonObject.get("name");
					name = name.substring(0, name.length() - 5);
					puzzle.setName(name);
					
				}
			}
			
			
			long noOfPieces = (long) jsonObject.get("no. of pieces");
			puzzle.setNoOfPieces(noOfPieces);
			
			JSONArray pieceArray = (JSONArray) jsonObject.get("pieces");
			Iterator<JSONObject> pieceIterator = pieceArray.iterator();
			
			
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
//				List<Double> list = new ArrayList<Double>();
				while (cornerIterator.hasNext()) {
					
					corners = corners + 1;

					JSONObject corners = (JSONObject) cornerIterator.next();
					JSONObject coordinate = (JSONObject) corners.get("coord");
					
//					double x = (double) coordinate.get("x");
//					double y = (double) coordinate.get("y");
//					
//					list.add(x);
//					list.add(y);
					
					double xs = (double) coordinate.get("x") * 100;
					double ys = (double) coordinate.get("y") * 100;
					
					piece.getPoints().addAll(xs+600,ys+200);
				
				}
				
//				System.out.println("size: " + list.size());
//				
//				double xMax = list.get(0);
//				double xMin = list.get(0);
//				double yMax = list.get(0);
//				double yMin = list.get(0);
//				
//				for (int i = 0; i < list.size(); i++) {
//					if (i % 2 == 0) {
//						if (list.get(i) > xMax) {
//							xMax = list.get(i);
//						}
//						if (list.get(i) < xMin) {
//							xMin = list.get(i);
//						}
//					} else {
//						if (list.get(i) > yMax) {
//							yMax = list.get(i);
//						}
//						if (list.get(i) < yMin) {
//							yMin = list.get(i);
//						}
//					}
//				}
//				
//				double centerX = (xMax - xMin) / 2;
//				double centerY = (yMax - yMin) / 2;
//				
//				for (int i = 0; i < list.size(); i++) {
//					double x = 0;
//					double y = 0;
//					if (i % 2 == 0) {
//						x = (list.get(i) - centerX) * 100;
//						piece.getPoints().add(x + 400);
//					} else {
//						y = (list.get(i) - centerY) * 100;
//						piece.getPoints().add(y + 200);
//					}
////					piece.getPoints().addAll(x, y);
//				}
				
//				System.out.println(piece.getPoints().toString());
				
				puzzle.addPieceToArray(piece);
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
		
		System.out.println(matches);
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
