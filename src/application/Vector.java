package application;

import java.awt.geom.Point2D;

public class Vector {
	private double x;
	private double y;
	private double magnitude;
	
	public Vector(Point2D p1, Point2D p2) {
		x = p2.getX() - p1.getX();
		y = p2.getY() - p1.getY();
		magnitude = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public Vector(double x1, double y1, double x2, double y2) {
		x = x1-x2;
		y = y1-y2;
		magnitude = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public static double determinant(Vector v1, Vector v2) {
		return (v1.getX()*v2.getY() - v1.getY() * v2.getX());
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return v1.getX()*v2.getX()+v1.getY()*v2.getY();
	}
	
	public static double angle(Vector v1, Vector v2) {
		return 180*Math.acos(Vector.dotProduct(v1, v2)/(v1.getMagnitude()*v2.getMagnitude()))/Math.PI;
	}
	
}
