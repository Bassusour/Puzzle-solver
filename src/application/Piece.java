package application;



import java.util.ArrayList;

import javafx.collections.ObservableList;

import javafx.scene.shape.Polygon;

import java.awt.geom.Point2D;
import java.lang.Object;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Piece extends Polygon implements Comparable<Piece> {
	
	private long number;
	private double angles[];
	private double sumOfAngles = 0;
	private double sumOfLengths = 0;
	private ArrayList<Point2D> points = new ArrayList<Point2D>();
	private ArrayList<Double> lengths = new ArrayList<Double>();
	
	public long getNumber() {
		return number;
	}
	
	public void setNumber(long number2) {
		this.number = number2;
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
		calculateValues(list);
	}
	
	private void calculateValues(ObservableList<Double> list) {
		int noOfLines = list.size()/2;
		int expectedSumOfAngles = 180*(noOfLines-2);
		boolean reverseOrder = false;
		angles = new double[noOfLines];
		
		//Calculate and save the lengths between points
		for (int i = 0; i < noOfLines; i++) {
			double length = points.get(i).distance(points.get((i+1)%noOfLines));
			lengths.add(length);
			sumOfLengths += length;
		}
		System.out.println(this.number);
		System.out.println("angles");
		
		while(!closeEnough(sumOfAngles, expectedSumOfAngles)) {
			for (int i = 0; i < noOfLines; i++) {
				//If wrong order, start over with correct order
				if(sumOfAngles > expectedSumOfAngles) {
					reverseOrder = true;
					sumOfAngles = 0;
					System.out.println("Reverse");
					break;
				}
				
				int negMod = Math.floorMod(i-1, noOfLines);
				Vector v1 = new Vector(points.get(i), points.get((i+1)%noOfLines));
				Vector v2 = new Vector(points.get(i), points.get(negMod));
				double det;
				if(reverseOrder) {
					det = Vector.determinant(v2, v1);
				} else {
					det = Vector.determinant(v1, v2);
				}
				double angle = Vector.angle(v1, v2);
				if(det < 0) {
					angle = 360 - angle;
				}
				
				//If angle is 180 degrees, remove the point, and update the length
				if(closeEnough(angle, 180)) {
					//Decrease number of lines, and remove the point
					noOfLines--;
					expectedSumOfAngles = 180*(noOfLines-2);
					points.remove(i);
					
					//Remove and correct length caused by the point
					negMod = Math.floorMod(i-1, lengths.size()-1);
					lengths.remove(i);
					lengths.remove(negMod);
					lengths.add(negMod, points.get(negMod).distance(points.get(i)));
					//Start again from the next point, with no incorrect point
					i-=1;
					continue;
				}
				sumOfAngles += angle;
				angles[i] = angle;
				System.out.println(angles[i]);
			}
		}
		
		System.out.println(sumOfAngles);
		System.out.println();
		System.out.println("lengths");
		System.out.println(lengths);
		System.out.println();
		
		//Find index of smallest angle 
		double smallestAngle = angles[0];
		int indexOfSV = 0;
		for(int i = 1; i < angles.length; i++) {
			if(closeEnough(smallestAngle, angles[i])) {
				continue;
			}
			if(smallestAngle > angles[i]) {
				smallestAngle = angles[i];
				indexOfSV = i;
			}
		}
		
		System.out.println("angles reordered");
		//Re-order arrays, so the first element corresponds to the smallest value of the angle
		double[] tmpAngles = angles.clone();
		//ArrayList<Double> tmpLengths = new ArrayList<Double>(lengths);
		if(!reverseOrder) {
			for(int i = 0; i < noOfLines; i++) {
				angles[i] = tmpAngles[(indexOfSV+i)%noOfLines];
				//lengths.set(i, tmpLengths.get((indexOfSV+i)%noOfLines));
			}
		} else {
			for(int i = 0; i < noOfLines; i++) {
				int negMod = Math.floorMod(-i, noOfLines);
				angles[negMod] = tmpAngles[(indexOfSV+i)%noOfLines];
			}
		}
		
		
		for(int i = 0; i < noOfLines; i++) {
			System.out.println(angles[i]);
		}
		
		/*System.out.println();
		System.out.println("lengths reordered");
		System.out.println(lengths);*/
		System.out.println();
		System.out.println("Sum of lengths: " + sumOfLengths);
		System.out.println();
	}
	
	private boolean closeEnough(double v1, double v2) {
	    if(Math.abs(v1 - v2) <= 1e-3) {
	    	return true;
	    } 
	    return false;
	}
	
	public double[] getAngles() {
		return angles;
	}
	
	public ArrayList<Double> getLengths() {
		return lengths;
	}
	
	public ArrayList<Point2D> getPointList() {
		return this.points;
	}
	
	public void updatePoints( double translatex, double translatey ) {
		for (int i = 0; i < this.getPoints().size(); i += 2) {
			this.points.get(i/2).setLocation(this.getPoints().get(i)+translatex, this.getPoints().get(i+1)+translatey);
		}
	}
	
	public void updatePointsRotate(double degrees) {
		for(int i = 0; i < this.getPoints().size(); i += 2) {
			double newX = (this.getPoints().get(i) + this.getTranslateX() - this.getCenterX()) * Math.cos(Math.toRadians(degrees)) - (this.getPoints().get(i+1) + this.getTranslateY() - this.getCenterY()) * Math.sin(Math.toRadians(degrees)) + this.getCenterX();
			double newY = (this.getPoints().get(i) + this.getTranslateX() - this.getCenterX()) * Math.sin(Math.toRadians(degrees)) + (this.getPoints().get(i+1) + this.getTranslateY() - this.getCenterY()) * Math.cos(Math.toRadians(degrees)) + this.getCenterY();
			this.points.get(i/2).setLocation(newX, newY);
		}
	}

	@Override
	public int compareTo(Piece p) {
		boolean match = false;
		if(closeEnough(p.sumOfLengths, this.sumOfLengths) && closeEnough(p.sumOfAngles, this.sumOfAngles)) {
			if(p.points.size() == this.points.size()) {
				//Checks angles in clockwise order
				for(int i = 0; i < p.points.size(); i++) {
					if(closeEnough(p.angles[i], this.angles[i])) {
						if(i == (p.points.size()-1)) {
							//match
							return 0;
						}
					} else {
						//no match
						return -1;
					}
				}
				//Chekcs angles in reverse order
				/*for(int i = 0; i < p.points.size(); i++) {
					int negMod = Math.floorMod(-i, p.points.size());
					if(closeEnough(p.angles[i], this.angles[negMod])) {
						if(i == (p.points.size()-1)) {
							match = true;
						}
						continue;
					} else {
						break;
					}
				}*/
			}
		}
		return match ? 0 : -1;
	}
}
