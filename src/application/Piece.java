package application;

import javafx.scene.shape.Polygon;

public class Piece extends Polygon {
	
	private long number;
	private int lengths[];
	private int angles[];
	
	public long getNumber() {
		return number;
	}
	
	public void setNumber(long number) {
		this.number = number;
	}
	
	public void updatePoints() {
		lengths = new int[this.getPoints().size()];
		angles = new int[this.getPoints().size()];
		
		
	}
	
}
