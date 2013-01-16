package simulation.p5;

import processing.core.PApplet;

public class PendulumPApplet extends PApplet {
	float	gravityS= 0.098f;
	double	appletAngle;// counter clockwise is plus.	
	
	public void setup(){
		size(300, 300);
		smooth();
		background(0, 0, 0);
		stroke(200, 200, 200);		
		appletAngle = 0;//Math.PI/4;	
	}
	
	public void setAppletAngle(double angle){
		this.appletAngle = angle;
	}
	
	public void draw(){
		background(0, 0, 0);

		// Pendulumn, that shows gravity orientaion
		fill(255,255,255);
		ellipse(width/2, height/2, 20,20);
		stroke(255, 255, 255);
		line(width/2,height/2,width/2  + 100* (float)Math.sin(-appletAngle),height/2 + 100* (float)Math.cos(-appletAngle));			
	}	
	
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == UP) {
				this.appletAngle += 0.1;
			} else if (keyCode == DOWN) {
				this.appletAngle -= 0.1;
			}			
		} 		  
	}
}
