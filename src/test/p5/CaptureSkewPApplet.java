package test.p5;

import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

import processing.video.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

/*
 * based on kougaku-navi, http://d.hatena.ne.jp/kougaku-navi/20120109/p1
 */

public class CaptureSkewPApplet extends PApplet {
	public boolean realDeployment = false;	
	Capture video;
	int   selected = -1;
	int   pos[][] = {{0,0},{400,0},{400,300},{0,300}};

	public void setup(){
		size(400,300, P2D);
		if(realDeployment){ 
			video = new Capture(this, width, height);
			video.start();
		}
	}
	
	public	void setCapture(Capture capture){
		this.video = capture;
	}
	
	public void draw(){
		if(video == null)
			return;	
		if (video.available()) {
			video.read();
			video.loadPixels();
			
			background(0);    
    
			beginShape();
				texture(video);  
				vertex(pos[0][0], pos[0][1], 0, 0);
				vertex(pos[1][0], pos[1][1], video.width, 0);
				vertex(pos[2][0], pos[2][1], video.width, video.height);
				vertex(pos[3][0], pos[3][1], 0, video.height);
			endShape(CLOSE);
    
			if ( mousePressed && selected >= 0 ) {
				pos[selected][0] = mouseX;
				pos[selected][1] = mouseY;
			}
			else {
				float min_d = 20;
				selected = -1;
				for (int i=0; i<4; i++) {
					float d = dist( mouseX, mouseY, pos[i][0], pos[i][1] );
					if ( d < min_d ) {
						min_d = d;
						selected = i;
					}      
				}
			}
			if ( selected >= 0 ) {
				ellipse( mouseX, mouseY, 20, 20 );
			}
		}
	}
	
	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "CaptureSkew" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}	
}