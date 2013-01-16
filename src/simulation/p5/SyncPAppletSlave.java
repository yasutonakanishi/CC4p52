package simulation.p5;

import processing.core.PApplet;
import processing.core.PFont;


public class SyncPAppletSlave extends PApplet{
	int masterStatus;
	int slaveid;
	String message;
	char displayChar;
	static int charSize = 192;
	PFont font;

	public SyncPAppletSlave(){
		this.slaveid = 0;
		this.message = "Hello";
		displayChar = message.charAt(slaveid);		
	}
	public SyncPAppletSlave(int id, String message){
		this.slaveid = id;
		this.message = message;
		displayChar = message.charAt(id);
		noLoop();		
	}
	
	public void setup(){
		size(200,200);
		smooth();
		
		textAlign(CENTER);
		textSize(charSize);
		
		font = loadFont("BankGothic-Medium-48.vlw");
		textFont(font, charSize);
		fill(255,32,0);
	}
	
	public void draw(){
		background(0);						
		text(displayChar,width/2, height/2+charSize/4);
	}
		
	public void updateStatus(int masterStatus){
		displayChar = this.message.charAt((masterStatus+slaveid) % this.message.length());
		repaint();
	}
}
