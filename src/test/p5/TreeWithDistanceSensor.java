package test.p5;

import net.unitedfield.cc.DistanceSensor;
import net.unitedfield.cc.DistanceSensorFirmata;
import processing.core.PApplet;

public class TreeWithDistanceSensor extends PApplet {

/**
 * Recursive Tree
 * by Daniel Shiffman.  
 */
 
float theta;
DistanceSensor	distanceSensor = null;
boolean			realDeployment = false;

	public void setup() {
		size(640, 360);
		if(realDeployment == true){
			distanceSensor = new DistanceSensorFirmata(0);
			((DistanceSensorFirmata)distanceSensor).setup();
		}
	}
	
	public	void setDistanceSensor(DistanceSensor sensor){
			this.distanceSensor = sensor;
	}

	public void draw() {
		background(0);
		frameRate(30);
		stroke(255);
		
		// Let's pick an angle 0 to 90 degrees based on the mouse position or DistanceSensorFirmata
		//float a = (mouseX / (float) width) * 90f;
		float a =0;		
		if(distanceSensor != null)
			a = (distanceSensor.getDistance()/distanceSensor.getSenseMax()) *90f;
		
		theta = radians(a);
		translate(width/2,height);
		line(0,0,0,-120);
		translate(0,-120);
		branch(120);
	}

public void branch(float h) {
  // Each branch will be 2/3rds the size of the previous one
  h *= 0.66f;
  
  // All recursive functions must have an exit condition!!!!
  // Here, ours is when the length of the branch is 2 pixels or less
  if (h > 2) {
    pushMatrix();    // Save the current state of transformation (i.e. where are we now)
    rotate(theta);   // Rotate by theta
    line(0, 0, 0, -h);  // Draw the branch
    translate(0, -h); // Move to the end of the branch
    branch(h);       // Ok, now call myself to draw two new branches!!
    popMatrix();     // Whenever we get back here, we "pop" in order to restore the previous matrix state
    
    // Repeat the same thing, only branch off to the "left" this time!
    pushMatrix();
    rotate(-theta);
    line(0, 0, 0, -h);
    translate(0, -h);
    branch(h);
    popMatrix();
  }
}

}
