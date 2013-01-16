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

public class CaptureBinarizePApplet extends PApplet {
	Capture video;
	PImage binaryImage;

	public boolean realDeployment = true;	
	
	public	void setCapture(Capture capture){
		this.video = capture;
	}
	
	public void setup(){
		size(400,300,P2D);
  
		if(realDeployment){
			video = new Capture(this, width, height);
			video.start();
		}
	}

	//  gray = 0.114*b + 0.587*g + 0.299*r; 
	float r_ratio = 0.299f;
	float g_ratio = 0.587f;
	float b_ratio = 0.114f;
	int threshold = 128;

	public void draw(){
		if(video == null)
			return;
		
		if (video.available()) {
			video.read();
			video.loadPixels();  			
			//this.image(video, 0, 0);
			
			binaryImage = new PImage(width, height);                          
			for(int i = 0; i < width * height; i++){
				int pix = video.pixels[i];
				float gray = (int)(r_ratio*red(pix) + g_ratio*green(pix) + b_ratio*blue(pix));
				if(gray > threshold){
					binaryImage.pixels[i] = color(255,255,255); //color(255);
				}else{
					binaryImage.pixels[i] = color(0,0,0); //color(0);
				}
			}
			image(binaryImage, 0, 0);		
		}	
	}
}
