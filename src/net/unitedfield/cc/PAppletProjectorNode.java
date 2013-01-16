package net.unitedfield.cc;

import processing.core.PApplet;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.SimpleTextureProjector;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireFrustum;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;


public class PAppletProjectorNode extends Node{
	SimpleTextureProjector projector;
	WireFrustum 	frustum;
	Vector3f[] 		frustumPoints;
	Geometry 		frustumMdl;
		
	public PAppletProjectorNode(String name, AssetManager assetManager, PApplet applet, int appletWidth, int appletHeight, boolean frameVisible)
	{	
		super(name);
		Geometry projectorGeometry = new Geometry();		
		Box box = new Box(0.1f, 0.05f, 0.15f);
		projectorGeometry.setMesh(box);
		projectorGeometry.setMaterial( new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md"));
		this.attachChild(projectorGeometry);
		
		Texture2D projectiveTextureMap = (Texture2D)assetManager.loadTexture("Interface/Logo/Monkey.jpg");
		projectiveTextureMap.setMinFilter(Texture.MinFilter.Trilinear);
		projectiveTextureMap.setMagFilter(Texture.MagFilter.Bilinear);
		projectiveTextureMap.setAnisotropicFilter(16);
	    //projectiveTextureMap.setWrap(Texture.WrapMode.EdgeClamp);
		projectiveTextureMap.setWrap(Texture.WrapMode.BorderClamp);	    
//	    int textureWidth  = projectiveTextureMap.getImage().getWidth();
//	    int textureHeight = projectiveTextureMap.getImage().getHeight();
//	    float textureAspectRatio = ((float) textureWidth) / ((float) textureHeight);
		float textureAspectRatio = ((float) appletWidth) / ((float) appletHeight);
		
		this.projector = new PAppletProjector(projectiveTextureMap, applet, appletWidth, appletHeight, frameVisible);
	    Camera projectorCamera = this.projector.getProjectorCamera();
	    projectorCamera.setFrustumPerspective(30, textureAspectRatio, 0.5f, 3f);
	    projectorCamera.setParallelProjection(false);
	        
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
	}
		
	public PAppletProjectorNode(String name, AssetManager assetManager,Texture2D texture) {
		super(name);
		
		Geometry projectorGeometry = new Geometry();
		Box box = new Box(0.1f, 0.05f, 0.15f);
		projectorGeometry.setMesh(box);
		projectorGeometry.setMaterial( new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md"));
		this.attachChild(projectorGeometry);
		
		this.projector = new SimpleTextureProjector(texture);
		
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
	}
	
    public void updateWorldTransforms(){
    	super.updateWorldTransforms();
    	
    	this.projector.getProjectorCamera().setRotation(worldTransform.getRotation());
        this.projector.getProjectorCamera().setLocation(worldTransform.getTranslation());          
        this.projector.updateFrustumPoints(this.frustumPoints);
	    this.frustum.update(this.frustumPoints);
    }
	
	public SimpleTextureProjector getProjector(){
		return this.projector;
	}
	
	public Geometry getFrustmModel(){
		return this.frustumMdl;
	}
}