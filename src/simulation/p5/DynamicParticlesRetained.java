package simulation.p5;

import processing.core.*; 
import processing.data.*; 
import processing.opengl.*; 

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

public class DynamicParticlesRetained extends PApplet {

PShape particles;
PImage sprite;  

int npartTotal = 10000;
int npartPerFrame = 25;
float speed = 1.0f;
float gravity = 0.05f;
float partSize = 20;

int partLifetime;
PVector velocities[];
int lifetimes[];

int fcount, lastm;
float frate;
int fint = 3;

public void setup() {
  size(640, 480, P3D);
  //size(105, 1050, P3D);
  frameRate(120);

  particles = createShape(PShape.GROUP);
  sprite = loadImage("sprite.png");

  for (int n = 0; n < npartTotal; n++) {
    PShape part = createShape(QUAD);
    part.noStroke();
    part.texture(sprite);
    part.normal(0, 0, 1);
    part.vertex(-partSize/2, -partSize/2, 0, 0);
    part.vertex(+partSize/2, -partSize/2, sprite.width, 0);
    part.vertex(+partSize/2, +partSize/2, sprite.width, sprite.height);
    part.vertex(-partSize/2, +partSize/2, 0, sprite.height);
    part.end();    
    particles.addChild(part);
  }

  partLifetime = npartTotal / npartPerFrame;
  initVelocities();
  initLifetimes(); 

  // Writing to the depth buffer is disabled to avoid rendering
  // artifacts due to the fact that the particles are semi-transparent
  // but not z-sorted.
  hint(DISABLE_DEPTH_MASK);
} 

public void draw () {
  //background(0);
	background(256,0,0);

	
  for (int n = 0; n < particles.getChildCount(); n++) {
    PShape part = particles.getChild(n);

    lifetimes[n]++;
    if (lifetimes[n] == partLifetime) {
      lifetimes[n] = 0;
    }      

    if (0 <= lifetimes[n]) {
      float opacity = 1.0f - PApplet.parseFloat(lifetimes[n]) / partLifetime;
      part.tint(255, opacity * 255);
      
      if (lifetimes[n] == 0) {
        // Re-spawn dead particle
        part.resetMatrix();
        part.translate(mouseX, mouseY);
        float angle = random(0, TWO_PI);
        float s = random(0.5f * speed, 0.5f * speed);
        velocities[n].x = s * cos(angle);
        velocities[n].y = s * sin(angle);
      } else {
        part.translate(velocities[n].x, velocities[n].y);
        velocities[n].y += gravity;
      }
    } else {
      part.tint(0, 0);
    }
  }

  shape(particles);
  
  fcount += 1;
  int m = millis();
  if (m - lastm > 1000 * fint) {
    frate = PApplet.parseFloat(fcount) / fint;
    fcount = 0;
    lastm = m;
    println("fps: " + frate); 
  } 
}

public void initVelocities() {
  velocities = new PVector[npartTotal];
  for (int n = 0; n < velocities.length; n++) {
    velocities[n] = new PVector();
  }
}

public void initLifetimes() {
  // Initializing particles with negative lifetimes so they are added
  // progressively into the screen during the first frames of the sketch   
  lifetimes = new int[npartTotal];
  int t = -1;
  for (int n = 0; n < lifetimes.length; n++) {    
    if (n % npartPerFrame == 0) {
      t++;
    }
    lifetimes[n] = -t;
  }
} 
//  static public void main(String[] passedArgs) {
//    String[] appletArgs = new String[] { "DynamicParticlesRetained" };
//    if (passedArgs != null) {
//      PApplet.main(concat(appletArgs, passedArgs));
//    } else {
//      PApplet.main(appletArgs);
//    }
//  }
}
