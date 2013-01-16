package net.unitedfield.cc;

import java.awt.BorderLayout;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

import processing.core.PApplet;

import com.jme3.post.SimpleTextureProjector;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;

public class PAppletProjector extends SimpleTextureProjector {
	private	PApplet		applet;
	private	Image		appletImage;
	private	ByteBuffer 	abgrPixelData;
	private	JFrame		frame;
	private	boolean 	frameVisible= true;
	private	int			appletAlpha	= 192; //default value.
	
	public PAppletProjector(Texture2D projectiveTextureMap) {
		super(projectiveTextureMap);		
	}
	
	public PAppletProjector(Texture2D projectiveTextureMap, PApplet applet, int appletFrameWidth, int appletFrameHeight, boolean frameVisible) {
		super(projectiveTextureMap);
		setupWithPApplet(applet, appletFrameWidth, appletFrameHeight, frameVisible);
	}	
	
	private	void setupWithPApplet(PApplet applet, int appletFrameWidth, int appletFrameHeight, boolean frameVisible){
		this.applet = applet;
		this.frameVisible = frameVisible;
		
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(appletFrameWidth, appletFrameHeight);        
        if(frameVisible)
        	frame.setVisible(true);
        frame.add(applet, BorderLayout.CENTER);
        frame.setResizable(false);
                
		applet.registerMethod("pre", this);
		applet.registerMethod("post", this);
		
		applet.init();
	}
	
	public void pre() {		
		this.applet.unregisterMethod("pre", this);
		//* after unregistaration, this pre() is not called from the PApplet. Therefore, following methods are called only once.
		this.abgrPixelData = ByteBuffer.allocateDirect(this.applet.width*this.applet.height*4);
		this.appletImage = new Image(Format.ABGR8, this.applet.width, this.applet.height, this.abgrPixelData);
	}	
		
	public void post(){		
		this.applet.loadPixels();
		Texture2D texture2d = this.getProjectiveTexture();
		if(texture2d != null && this.abgrPixelData != null){
			this.abgrPixelData.clear();
			int c = 0;
			int w = this.applet.width;
			int len = this.applet.height * this.applet.width;
			//* if we simply put color to the buffer as follows,
			//*	for(int i=0; i <this.applet.width*this.applet.height; i++){
			//*		int color = this.applet.pixels[i];	
			//* the image is flipped, therefore, updated as follows
			for(int i=0; i <len ; i+=w){
				for (int j = w-1; j >= 0; j--) {
					int color = this.applet.pixels[len-1-(i+j)];				
					int a = (color >> 24) & 0xff;
					int r = (color >> 16) & 0xff;
					int g = (color >> 8) & 0xff;
					int b = color & 0xff;
					if(appletAlpha > 0)
						a = appletAlpha;
					int abgrColor = (a << 24) | (b << 16) | (g << 8) | r;
					this.abgrPixelData.asIntBuffer().put(c, abgrColor);
					c++;
				}
			}			
			this.appletImage.setData(this.abgrPixelData);
			texture2d.setImage(appletImage);
		}
	}
	
	public PApplet	getApplet(){
		return this.applet;
	}	
	public	void setAppletAlpha(int appletAlpha){
		this.appletAlpha = appletAlpha;
	}
}
