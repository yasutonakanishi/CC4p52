package test.p5;

import processing.core.PApplet;
import processing.core.PImage;

public class JpgFlipPApplet extends PApplet{
	int frame = 0;
	int 	imagesNum;  // The number of frames in the animation
	String	imagesDir;	
	PImage[] images; 	
	int	imageWidth, imageHeight;
	
	public JpgFlipPApplet(){
		// as an example
		this("transitimages", 750, 640,360);
	}
	public JpgFlipPApplet(String imageDirName,int numFrames, int width, int height){
		this.imagesNum	= numFrames;
		this.images 	= new PImage[numFrames];
		this.imagesDir	= imageDirName;		
		this.imageWidth = width;
		this.imageHeight= height;
	}
	
	public void setup(){
		size(imageWidth, imageHeight);
		frameRate(30);
		for(int i=0; i<imagesNum; i++)
			this.images[i] = loadImage(imagesDir + "/"+i+".jpg");		 
	}

	public void draw() { 
		background(0);
		frame = (frame+1) % imagesNum;
		image(images[frame], 0, 0);		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
