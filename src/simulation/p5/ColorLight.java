package simulation.p5;

import processing.core.PApplet;

public class ColorLight extends PApplet {

	int r, g, b;
	int counter;
	int delay = 10;
	
	public ColorLight(){		
	}
	
	public ColorLight(int _delay) {
		delay = _delay;
	}

	public void setup() {
		size(200, 200);
		noStroke();
		counter += delay;
	}

	public void draw(){
		background(counter, counter / 2, 255 - counter);
		counter++;
		counter = (counter) % 255;
		
	}
}