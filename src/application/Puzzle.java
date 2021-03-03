package application;

import javafx.scene.shape.Polygon;

public class Puzzle extends Polygon {
	
	private String name;
	private long noOfPieces;
	private Piece[] pieces;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getNoOfPieces() {
		return noOfPieces;
	}
	
	public void setNoOfPieces(long noOfPieces) {
		this.noOfPieces = noOfPieces;
		this.pieces = new Piece[(int)noOfPieces];
	}
	
	public void addPieceToArray(Piece piece) {
		pieces[(int) piece.getNumber() - 1] = piece;
	}

}
