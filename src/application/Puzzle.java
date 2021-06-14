package application;

import javafx.scene.shape.Polygon;

public class Puzzle extends Polygon {
	
	private String name;
	private long noOfPieces;
	private Piece[] pieces;
	private int currPieceNo = 0;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Piece[] getPieces() {
		return pieces;
	}
	
	public long getNoOfPieces() {
		return noOfPieces;
	}
	
	public void setNoOfPieces(long noOfPieces) {
		this.noOfPieces = noOfPieces;
		this.pieces = new Piece[(int)noOfPieces];
	}
	
	public void addPieceToArray(Piece piece) {
		pieces[currPieceNo] = piece;
		currPieceNo++;
	}
	
	public Piece getPiece(int index) {
		return pieces[index];
	}

}
