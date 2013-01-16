package simulation.p5;

import processing.core.PApplet;
import processing.video.Capture;

public class SlitScanPApplet  extends PApplet {
	boolean realDeployment = false;
	Capture video = null;	

	int videoSliceX;
	int drawPositionX;

	public void setup() {
		size(400, 300, P2D);

		// Uses the default video input, see the reference if this causes an error
		if(realDeployment) {
			video = new Capture(this, this.width, this.height);
			video.start();
		}

		videoSliceX = video.width / 2;
		drawPositionX = width - 1;
		frameRate(30);
		background(0);
	}

	public	void setCapture(Capture capture){
		this.video = capture;
	}

	@Override
	public void draw() {
		/*
		if(video != null){
		  video.read();
		  video.loadPixels();
		  this.image(video, 0, 0);
		}
		*/
				
		if(video != null){
			
			video.read();
			video.loadPixels();

			loadPixels();
			for (int y = 0; y < video.height; y++){
				int setPixelIndex = y*width + drawPositionX;
				int getPixelIndex = y*video.width  + videoSliceX;
				pixels[setPixelIndex] = video.pixels[getPixelIndex];
			}
			updatePixels();

			drawPositionX--;

			if (drawPositionX < 0) {
				drawPositionX = width - 1;
			}
		}
		
	}
}
