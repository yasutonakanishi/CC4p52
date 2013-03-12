package simulation.p5;

import processing.core.PApplet;
import processing.video.Capture;

public class Mirror2PApplet extends PApplet {

/**
 * Mirror 2 
 * by Daniel Shiffman. 
 *
 * Each pixel from the video source is drawn as a rectangle with size based on brightness.  
 */
// Size of each cell in the grid
int cellSize = 15;
// Number of columns and rows in our system
int cols, rows;
// Variable for capture device
Capture video;

/**
 * little modified for simulation in CC
 * by naka
 * please see the small difference between Mirror2(oritinal java src exported from pde) and this Mirror2PApplet.
 */

public boolean realDeployment = true;
//public boolean realDeployment = false;

public void setup() {
  size(640, 480, P2D);
	
  //set up columns and rows
  cols = width / cellSize;
  rows = height / cellSize;
  colorMode(RGB, 255, 255, 255, 100);
  rectMode(CENTER);

  // Uses the default video input, see the reference if this causes an error
  if(realDeployment) { // added for CC
	  video = new Capture(this, width, height);
	  video.start();
  }

  background(0);
}

public	void setCapture(Capture capture){ // added for CC
	this.video = capture;
}

public void draw() { 
  // added for CC
  if(video == null)
		return;
  
  if (video.available()) {
    video.read();
    video.loadPixels();
    
    background(0, 0, 255);

    // Begin loop for columns
    for (int i = 0; i < cols;i++) {
      // Begin loop for rows
      for (int j = 0; j < rows;j++) {
        // Where are we, pixel-wise?
        int x = i * cellSize;
        int y = j * cellSize;
        int loc = (video.width - x - 1) + y*video.width; // Reversing x to mirror the image

        // Each rect is colored white with a size determined by brightness
        int c = video.pixels[loc];
        float sz = (brightness(c) / 255.0f) * cellSize;
        fill(255);
        noStroke();
        rect(x + cellSize/2, y + cellSize/2, sz, sz);
      }
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Mirror2PApplet" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}