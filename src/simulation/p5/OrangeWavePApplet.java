package simulation.p5;

import processing.core.*; 
//import processing.xml.*; 

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

public class OrangeWavePApplet extends PApplet {
	int WNUM = 2;
	float[] amp = new float[WNUM];
	float[] freq = new float[WNUM];
	float yc;
	float phase;

	int BNUM = 5;
	float [] bx = new float[BNUM];
	float [] by = new float[BNUM];
	float [] bSpeed = new float[BNUM];

	public void setup() 
	{ 
	  size(20, 100);
	  phase = random(2.0f*PI);
	  frameRate(30);
	  
	  yc = height * 0.7f;

	  for (int i = 0; i < WNUM; i++) {
	    amp[i] = random(4.0f, 10.0f);
	    freq[i] = random(0.01f, 0.1f);
	  }
	  
	  for (int i = 0; i < BNUM; i++) {
	    bx[i] = random(width);
	    by[i] = random(height/1.5f);
	    bSpeed[i] = random(-1.5f, -0.1f);
	  }

	  background(255, 200, 31);
	} 

	public void draw() 
	{ 
	  noStroke();	
	  fill(255, 200, 31, 31);
	  rect(0, 0, width, height);

	  fill(255, 255, 255, 25);
	  for (int j = 0; j < WNUM; j++) {
	    beginShape();
	    vertex(0, height);
	    for (int i = 0; i <= width; i++) {
	      float yp = amp[j] * sin(i * freq[j] + millis()/1000.0f + phase);
	      vertex(i, yc + yp);
	    }
	    vertex(width, height);
	    endShape();
	  }
	  
	  fill(255, 255, 255, 31);
	  for (int i = 0; i < BNUM; i++) {
	    rect(bx[i], by[i], 20, 1);
	    bx[i] += bSpeed[i];
	    if(bx[i] < -20){
	      bx[i] = width;
	    }
	  }
	}	
}
