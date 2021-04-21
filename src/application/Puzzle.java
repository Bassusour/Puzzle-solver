package application;

import javafx.scene.shape.Polygon;

public class Puzzle {
	
	private String name;
	private long noOfPieces;
	private Piece[] pieces;
	//private double width;
	//private double height;
	
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
		pieces[(int) piece.getNumber()] = piece;
	}
	
	public Piece getPiece(int index) {
		return pieces[index];
	}
	/*
	public void setWidth(double width) {
		this.width = width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}*/

}
