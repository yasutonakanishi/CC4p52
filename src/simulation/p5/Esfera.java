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

public class Esfera extends PApplet {

/**
 * Esfera
 * by David Pena.  
 * 
 * Distribucion aleatoria uniforme sobre la superficie de una esfera. 
 */

int cuantos = 16000;
pelo[] lista ;
float[] z = new float[cuantos]; 
float[] phi = new float[cuantos]; 
float[] largos = new float[cuantos]; 
float radio = 200;
float rx = 0;
float ry =0;

public void setup() {
  size(400, 400, P3D);
  noSmooth();
  frameRate(120);  
  radio = height/3.5f;
  
  lista = new pelo[cuantos];
  for (int i=0; i<cuantos; i++){
    lista[i] = new pelo();
  }
  noiseDetail(3);

}

public void draw() {
  background(0);
  translate(width/2,height/2);

  float rxp = ((mouseX-(width/2))*0.005f);
  float ryp = ((mouseY-(height/2))*0.005f);
  rx = (rx*0.9f)+(rxp*0.1f);
  ry = (ry*0.9f)+(ryp*0.1f);
  rotateY(rx);
  rotateX(ry);
  fill(0);
  noStroke();
  sphere(radio);

  for (int i=0;i<cuantos;i++){
    lista[i].dibujar();

  }
  if (frameCount % 10 == 0) {
    //println(frameRate);
  }  
}


class pelo
{
  float z = random(-radio,radio);
  float phi = random(TWO_PI);
  float largo = random(1.15f,1.2f);
  float theta = asin(z/radio);

    public void dibujar(){

    float off = (noise(millis() * 0.0005f,sin(phi))-0.5f) * 0.3f;
    float offb = (noise(millis() * 0.0007f,sin(z) * 0.01f)-0.5f) * 0.3f;

    float thetaff = theta+off;
    float phff = phi+offb;
    float x = radio * cos(theta) * cos(phi);
    float y = radio * cos(theta) * sin(phi);
    float z = radio * sin(theta);

    float xo = radio * cos(thetaff) * cos(phff);
    float yo = radio * cos(thetaff) * sin(phff);
    float zo = radio * sin(thetaff);

    float xb = xo * largo;
    float yb = yo * largo;
    float zb = zo * largo;
    
    strokeWeight(1);
    beginShape(LINES);
    stroke(0);
    vertex(x,y,z);
    stroke(200,150);
    vertex(xb,yb,zb);
    endShape();
  }
}
//  static public void main(String[] passedArgs) {
//    String[] appletArgs = new String[] { "Esfera" };
//    if (passedArgs != null) {
//      PApplet.main(concat(appletArgs, passedArgs));
//    } else {
//      PApplet.main(appletArgs);
//    }
//  }
}
