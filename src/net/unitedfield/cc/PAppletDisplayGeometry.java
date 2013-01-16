package net.unitedfield.cc;

import java.awt.BorderLayout;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

import processing.core.PApplet;

import com.jme3.asset.AssetManager;
import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

public class PAppletDisplayGeometry extends Geometry {
	private	PApplet	applet;
	private	Image	appletImage;
	private	ByteBuffer abgrPixelData;
	private	JFrame	frame;
	boolean frameVisible = true;
	
	PAppletDisplayGeometry(String name, float width, float height, Material material, Texture texture) {
		super(name);
		Mesh box = new Box(Vector3f.ZERO, width/2, height/2, 0.01f);
		this.setMesh(box);		
		material.setTexture("ColorMap", texture);
		this.setMaterial(material);
	}
	
	PAppletDisplayGeometry(String name, Mesh mesh, Material material, Texture texture) {
		super(name);
		this.setMesh(mesh);
		material.setTexture("ColorMap", texture);
		this.setMaterial(material);		
	}

	public PAppletDisplayGeometry(String name, AssetManager assetmanager, float width, float height,   
			PApplet applet, int appletWidth, int appletHeight, boolean frameVisible){
		this(name, width, height, new Material(assetmanager,"Common/MatDefs/Misc/Unshaded.j3md"), assetmanager.loadTexture("Interface/Logo/Monkey.jpg"));
		setupWithPApplet(applet, appletWidth, appletHeight, frameVisible);
	}
		
	public PAppletDisplayGeometry(String name, Mesh mesh, AssetManager assetmanager,PApplet applet, int appletFrameWidth, int appletFrameHeight, boolean frameVisible){
		this(name, mesh, new Material(assetmanager,"Common/MatDefs/Misc/Unshaded.j3md"), assetmanager.loadTexture("Interface/Logo/Monkey.jpg"));
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
		MatParamTexture matParamTexture = this.material.getTextureParam("ColorMap");
		if(matParamTexture != null && this.abgrPixelData != null){			
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
					int abgrColor = (a << 24) | (b << 16) | (g << 8) | r;
					this.abgrPixelData.asIntBuffer().put(c, abgrColor);
					c++;
				}
			}			
			this.appletImage.setData(this.abgrPixelData);
			matParamTexture.getTextureValue().setImage(this.appletImage);
		}		
	}
	
	public PApplet	getApplet(){
		return this.applet;
	}
}