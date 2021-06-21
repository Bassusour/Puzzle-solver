package application;

import javafx.scene.Group;

public class PowerGroup extends Group {
	
	private double powerRotate;
	
	public PowerGroup() {
		
	}
	
	public double getPowerRotate() {
		return powerRotate;
	}
	
	public void setPowerRotate(double degrees) {
		powerRotate = degrees;
	}

}
