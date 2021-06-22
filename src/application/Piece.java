package application;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;

public class Piece extends Polygon implements Comparable<Piece>{

	private long number;
	private double angles[];
	private double unorderedAngles[];
	private double sumOfAngles = 0;
	private double sumOfLengths = 0;
	private ArrayList<Double> staticPoints = new ArrayList<Double>();
	private ArrayList<Point2D> points = new ArrayList<Point2D>();
	private ArrayList<Double> lengths = new ArrayList<Double>();
	private ArrayList<Point2D> matchingPoints = new ArrayList<Point2D>();
	private double OriginalCenterX;
	private double OriginalCenterY;
	
	public ArrayList<Point2D> getMatchingPoints() {
		return matchingPoints;
	}
	
	public void setMatchingPoints(ArrayList<Point2D> points) {
		this.matchingPoints = points;
	}
	
	public ArrayList<Double> getStaticPoints() {
		return staticPoints;
	}
	
	public void setStaticPoints() {
		for (Double value : this.getPoints()) {
			this.staticPoints.add(value);
		}
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
		for (int i = 0; i < this.getStaticPoints().size(); i += 2) {
			avg += this.getStaticPoints().get(i) + this.getTranslateX();
		}
		avg = avg / (this.getStaticPoints().size() / 2);
		return avg;
	}

	public double getCenterY() {
		double avg = 0;
		for (int i = 1; i < this.getStaticPoints().size(); i += 2) {
			avg += this.getStaticPoints().get(i) + this.getTranslateY();
		}
		avg = avg / (this.getStaticPoints().size() / 2);
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
		setLengths(noOfLines);
		while(!closeEnough(sumOfAngles, expectedSumOfAngles)) {
			for (int i = 0; i < noOfLines; i++) {
				//If wrong order, start over with correct order
				if(sumOfAngles > expectedSumOfAngles) {
					reverseOrder = true;
					sumOfAngles = 0;
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
			}
		}
		
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
		
		//Re-order array, so the first element corresponds to the smallest value of the angle
		unorderedAngles = angles.clone();
		if(!reverseOrder) {
			for(int i = 0; i < noOfLines; i++) {
				angles[i] = unorderedAngles[(indexOfSV+i)%noOfLines];
			}
		} else {
			for(int i = 0; i < noOfLines; i++) {
				int negMod = Math.floorMod(-i, noOfLines);
				angles[negMod] = unorderedAngles[(indexOfSV+i)%noOfLines];
			}
		}
	}
	
	public static boolean closeEnough(double v1, double v2) {
	    if(Math.abs(v1 - v2) <= 1e-5) {
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
	
	public double[] getUnorderedAngles() {
		return unorderedAngles;
	}

	@Override
	public int compareTo(Piece p) {
		if(closeEnough(p.sumOfLengths, this.sumOfLengths) && closeEnough(p.sumOfAngles, this.sumOfAngles)) {
			if(p.points.size() == this.points.size()) {
				//Checks angles in counter-clockwise order
				for(int i = 0; i < p.points.size(); i++) {
					if(closeEnough(p.angles[i], this.angles[i])) {
						if(i == (p.points.size()-1)) {
							//match
							return 0;
						}
					} else {
						break;
					}
				}
			}
		}
		//no match
		return -1;
	}
	
	private void setLengths(int noOfLines) {
		lengths.clear();
		sumOfLengths = 0;
		for (int i = 0; i < noOfLines; i++) {			
			double length = points.get(i).distance(points.get((i+1)%noOfLines));
			lengths.add(length);
			sumOfLengths += length;
		}
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
	
	public void updateGroupRotate(double degrees, Group group) {
		double sin;
		double cos;

		if (degrees % 180.0 == 0 && degrees != 0 && degrees % 360 != 0) {
			sin = 0.0;
			cos = -1.0;
		} else {
			sin = Math.sin(Math.toRadians(degrees));
			cos = Math.cos(Math.toRadians(degrees));
		}
		

		for(int i = 0; i < this.getStaticPoints().size(); i += 2) {
            
            double oldX = (this.getStaticPoints().get(i) + group.getTranslateX() + this.getTranslateX()) - (group.getLayoutBounds().getCenterX()+group.getTranslateX());
            double oldY = (this.getStaticPoints().get(i+1) + group.getTranslateY() + this.getTranslateY()) - (group.getLayoutBounds().getCenterY()+group.getTranslateY());

            double newX = oldX * cos - oldY * sin;
            double newY = oldX * sin + oldY * cos;

            newX += group.getLayoutBounds().getCenterX()+group.getTranslateX();
            newY += group.getLayoutBounds().getCenterY()+group.getTranslateY();
            
            this.points.get(i/2).setLocation(newX, newY);
        }
	}
	
	public void updateGroupRotate2(double degrees, Group group) {
		double sin;
		double cos;

		if (degrees % 180.0 == 0 && degrees != 0 && degrees % 360 != 0) {
			sin = 0.0;
			cos = -1.0;
		} else {
			sin = Math.sin(Math.toRadians(degrees));
			cos = Math.cos(Math.toRadians(degrees));
		}
		for(int i = 0; i < this.getStaticPoints().size(); i += 2) {
            
            double oldX = this.getStaticPoints().get(i) - this.getLayoutBounds().getCenterX();
            double oldY = this.getStaticPoints().get(i+1) - this.getLayoutBounds().getCenterY();

            double newX = (oldX * cos) - (oldY * sin);
            double newY = (oldX * sin) + (oldY * cos);

            newX += this.getLayoutBounds().getCenterX();
            newY += this.getLayoutBounds().getCenterY();
            
            this.points.get(i/2).setLocation(newX, newY);
        }
	}
	
	public void updatePieceRotate(double degrees) {
		double sin;
		double cos;

		if (degrees % 180.0 == 0 && degrees != 0 && degrees % 360 != 0) {
			sin = 0.0;
			cos = -1.0;
		} else {
			sin = Math.sin(Math.toRadians(degrees));
			cos = Math.cos(Math.toRadians(degrees));
		}
		for(int i = 0; i < this.getStaticPoints().size(); i+=2) {
            
            double oldX = (this.getStaticPoints().get(i)) - (this.getLayoutBounds().getCenterX());
            double oldY = (this.getStaticPoints().get(i + 1)) - (this.getLayoutBounds().getCenterY());
            
            double newX = (oldX * cos) - (oldY * sin);
            double newY = (oldX * sin) + (oldY * cos);

            newX += (this.getLayoutBounds().getCenterX());
            newY += (this.getLayoutBounds().getCenterY());         
            
            this.getStaticPoints().set(i, newX);
            this.getStaticPoints().set(i + 1, newY);
        }
	}
}
