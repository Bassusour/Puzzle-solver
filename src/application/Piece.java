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
		for (int i = 0; i < this.getPoints().size(); i += 2) {
			avg += this.getPoints().get(i) + this.getTranslateX();
		}
		avg = avg / (this.getPoints().size() / 2);
		return avg;
	}

	public double getCenterY() {
		double avg = 0;
		for (int i = 1; i < this.getPoints().size(); i += 2) {
			avg += this.getPoints().get(i) + this.getTranslateY();
		}
		avg = avg / (this.getPoints().size() / 2);
		return avg;
	}

	public void setPoints(ObservableList<Double> list) {
		for (int i = 0; i < list.size(); i += 2) {
			this.points.add(new Point2D.Double(list.get(i), list.get(i + 1)));
		}
	}

	public ArrayList<Point2D> getPointList() {
		return this.points;
	}

	public void setPoint(int index, Point2D element) {
		points.set(index, element);
	}

	public void updatePoints(double translatex, double translatey) {
		for (int i = 0; i < this.getPointList().size(); i++) {
			this.points.get(i).setLocation(this.getPointList().get(i).getX() + translatex,
					this.getPointList().get(i).getY() + translatey);
		}
	}

	public void updatePointsRotate(double degrees) {
		double sin;
		double cos;
		if (degrees % 180.0 == 0 && degrees != 0 && degrees % 360 != 0) {
			sin = 0.0;
			cos = -1.0;
		} else {
			sin = Math.sin(Math.toRadians(degrees));
			cos = Math.cos(Math.toRadians(degrees));
		}
		for(int i = 0; i < this.getPoints().size(); i += 2) {
            double oldX = (this.getPoints().get(i) + this.getTranslateX()) - (this.getLayoutBounds().getCenterX()+this.getTranslateX());
            double oldY = (this.getPoints().get(i+1) + this.getTranslateY()) - (this.getLayoutBounds().getCenterY()+this.getTranslateY());

            double newX = oldX * cos - oldY * sin;
            double newY = oldX * sin + oldY * cos;

            newX += this.getLayoutBounds().getCenterX()+this.getTranslateX();
            newY += this.getLayoutBounds().getCenterY()+this.getTranslateY();
            this.points.get(i/2).setLocation(newX, newY);
        }
	}

}
