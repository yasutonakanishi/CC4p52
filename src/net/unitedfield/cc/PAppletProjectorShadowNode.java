package net.unitedfield.cc;

import java.awt.BorderLayout;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

import processing.core.PApplet;

import com.jme3.asset.AssetManager;
import com.jme3.material.MatParamTexture;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireFrustum;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;

/**
 * @author naka 
 **/
public class PAppletProjectorShadowNode extends Node{
	private	PointLightTextureShadowRenderer ptsr;
	private Geometry	geom;	
	private	WireFrustum frustum;
	private	Vector3f[] 	frustumPoints;
	private	Geometry 	frustumMdl;
	
	private	PApplet		applet;
	private	Image		appletImage;
	private	ByteBuffer	abgrPixelData;
	
	private	boolean frameVisible = true;	
	private	JFrame	frame;
	
	public PAppletProjectorShadowNode(String name, AssetManager assetManager){
		super(name);
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
		Mesh box = new Box(Vector3f.ZERO, 0.2f, 0.1f, 0.2f);
		geom = new Geometry();
		geom.setMaterial(mat);
		geom.setMesh(box);
		Mesh cylinder = new Cylinder(10, 10, 0.05f, 0.1f);
		Geometry cylinderg = new Geometry();
		cylinderg.setMaterial(mat);
		cylinderg.setMesh(cylinder);		
		cylinderg.setLocalTranslation(0, 0, 0.2f);
		this.attachChild(cylinderg);
		this.attachChild(geom);		
	}
	
	public PAppletProjectorShadowNode(String name, AssetManager assetManager, ViewPort viewPort, int projectionWidth, int projectionHeight, Texture texture){
		this(name, assetManager);					
		this.ptsr = new PointLightTextureShadowRenderer(assetManager, projectionWidth, projectionHeight, texture);		
		viewPort.addProcessor(this.ptsr);		
		setFrustmModel(assetManager);
	}
	
	public PAppletProjectorShadowNode(String name, AssetManager assetManager, ViewPort viewPort, float fov, float far, int projectionWidth, int projectionHeight, Texture texture){
		this(name, assetManager);
		this.ptsr = new PointLightTextureShadowRenderer(assetManager, fov, far, projectionWidth, projectionHeight, texture);		
		viewPort.addProcessor(this.ptsr);		
		setFrustmModel(assetManager);
	}

	public PAppletProjectorShadowNode(String name, AssetManager assetManager, ViewPort viewPort, int projectionWidth, int projectionHeight, 
			PApplet applet,int appletFrameWidth, int appletFrameHeight, boolean frameVisible){		
		this(name, assetManager, viewPort, projectionWidth, projectionHeight, assetManager.loadTexture("Interface/Logo/Monkey.jpg")); 	
		setApplet(applet, appletFrameWidth, appletFrameHeight, frameVisible);
	}

	public PAppletProjectorShadowNode(String name, AssetManager assetManager, ViewPort viewPort, float fov, float far, int projectionWidth, int projectionHeight, 
			PApplet applet,int appletFrameWidth, int appletFrameHeight, boolean frameVisible){				
		this(name, assetManager, viewPort, fov, far, projectionWidth, projectionHeight, assetManager.loadTexture("Interface/Logo/Monkey.jpg")); 	
		setApplet(applet, appletFrameWidth, appletFrameHeight, frameVisible);
	}

	private void setFrustmModel(AssetManager assetManager){
	    this.frustumPoints = new Vector3f[8];
	    for (int i = 0; i < 8; i++)
	    {
	      this.frustumPoints[i] = new Vector3f();
	    }	    
	    this.frustum = new WireFrustum(this.frustumPoints);
	    frustumMdl = new Geometry("f", this.frustum);
	    frustumMdl.setCullHint(Spatial.CullHint.Never);
	    frustumMdl.setShadowMode(ShadowMode.Off);
	    frustumMdl.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
	    frustumMdl.getMaterial().setColor("m_Color", ColorRGBA.White);
	    //this.attachChild(frustumMdl);	    
	}
	
	private void setApplet(PApplet applet,int appletFrameWidth, int appletFrameHeight, boolean frameVisible){
		this.frameVisible = frameVisible;
		this.frame = new JFrame();
		this.frame.setLayout(new BorderLayout());
		this.frame.setSize(appletFrameWidth, appletFrameHeight);        
        if(this.frameVisible)
        	this.frame.setVisible(true);
        this.frame.add(applet, BorderLayout.CENTER);
        this.frame.setResizable(false);
        
		this.applet = applet;
		this.applet.registerMethod("pre", this);
		this.applet.registerMethod("post", this);		
		
		this.applet.init();
	}

	public PointLightTextureShadowRenderer getProjector(){
		return this.ptsr;
	}
	public Geometry getFrustmModel(){
		return this.frustumMdl;
	}

	public void pre() {
		this.applet.unregisterMethod("pre", this);
		//* after unregistaration, this pre() is not called from the PApplet. Therefore, following methods are called only once.
		this.abgrPixelData = ByteBuffer.allocateDirect(this.applet.width*this.applet.height*4);
		this.appletImage = new Image(Format.ABGR8, this.applet.width, this.applet.height, this.abgrPixelData);
	}	
	public void post(){	
		this.applet.loadPixels();
	    Material material = this.ptsr.getPostShadowMat();
		MatParamTexture matParamTexture = material.getTextureParam("ShadowTex");
		if(matParamTexture != null && this.abgrPixelData != null){
			this.abgrPixelData.clear();
			int c = 0;
			int w = this.applet.width;
			int len = this.applet.height * this.applet.width;
			
			//* if we simply put color to the buffer as follows,
			//for(int i=0; i <this.applet.width*this.applet.height; i++){
			//	int color = this.applet.pixels[i];
			//}
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
	
	public void addToViewPort(ViewPort viewport){
		viewport.addProcessor(ptsr);
	}
		
    public void updateWorldTransforms(){
    	super.updateWorldTransforms();
        ptsr.setLocation(worldTransform.getTranslation());
    	ptsr.setDirection(worldTransform.getRotation()); 	
    	
    	this.updateFrustumPoints(this.frustumPoints);
	    this.frustum.update(this.frustumPoints);
    }
 
    public void updateFrustumPoints(Vector3f[] points) {
	    int w = ptsr.getShadowCamera().getWidth();
	    int h = ptsr.getShadowCamera().getHeight();
	    final float n = 0;
	    final float f = 1f;
	    
	    points[0].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(0, 0), n));
	    points[1].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(0, h), n));
	    points[2].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(w, h), n));
	    points[3].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(w, 0), n));

	    points[4].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(0, 0), f));
	    points[5].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(0, h), f));
	    points[6].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(w, h), f));
	    points[7].set(ptsr.getShadowCamera().getWorldCoordinates(new Vector2f(w, 0), f));
	}   
}
