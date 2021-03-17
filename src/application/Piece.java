package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;

public class Piece extends Polygon {
	
	private long number;
	private ArrayList<Point2D> points = new ArrayList<Point2D>();
	private double OriginalCenterX;
	private double OriginalCenterY;
	
	public Piece() {
	
	}
	
	public long getNumber() {
		return number;
	}
	
	public void setNumber(long number) {
		this.number = number;
	}
	
	public double getOriginalCenterX() {
		return OriginalCenterX;
	}
	
	public double getOriginalCenterY() {
		return OriginalCenterY;
	}
	
	public void setOriginalCenterX(double centerX) {
		this.OriginalCenterX = centerX;
	}
	
	public void setOriginalCenterY(double centerY) {
		this.OriginalCenterY = centerY;
	}
	
	public double getCenterX() {
		double avg = 0;
		for (int i = 0; i < this.points.size(); i++) {
			avg += this.points.get(i).getX();
		}
		avg = avg/this.points.size();
		return avg;
	}
	
	public double getCenterY() {
		double avg = 0;
		for (int i = 0; i < this.points.size(); i++) {
			avg += this.points.get(i).getY();
		}
		avg = avg/this.points.size();
		return avg;
	}
	
	
	public void setPoints(ObservableList<Double> list) {
		for (int i = 0; i < list.size(); i += 2) {
			this.points.add(new Point2D.Double(list.get(i), list.get(i+1)));
		}
	}
	
	public ArrayList<Point2D> getPointList() {
		return this.points;
	}
	
	public void setPoint(int index, Point2D element) {
		points.set(index, element);
	}
	
	public void updatePoints( double translatex, double translatey ) {
		for (int i = 0; i < this.getPoints().size(); i += 2) {
			this.points.get(i/2).setLocation(this.getPoints().get(i)+translatex, this.getPoints().get(i+1)+translatey);
		}
	}

}
