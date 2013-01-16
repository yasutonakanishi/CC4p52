package test.p5;

import processing.core.PApplet;
import processing.video.Capture;

public class CapturePApplet extends PApplet {	
	public boolean realDeployment = true;
	Capture video = null;	
	
	public void setup() {
		size(400, 300, P2D);			
		
		if(realDeployment){ 
			video = new Capture(this, this.width, this.height);
			video.start();
		}
	}
	
	public	void setCapture(Capture capture){
		this.video = capture;
	}
	
	@Override
	public void draw() {
		if(video == null)
			return;
		if (video.available()){
		  video.read();
		  video.loadPixels();
		  background(0, 0, 0);
		  this.image(video, 0, 0);
		}
	}
}