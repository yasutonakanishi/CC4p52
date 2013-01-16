package test.cc;

import net.unitedfield.cc.CaptureCameraNode;
import net.unitedfield.cc.PAppletProjectorNode;
import test.p5.CaptureSkewPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.TextureProjectorRenderer;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class HelloCaptureAndProjection extends SimpleApplication {

	@Override
	public void simpleInitApp() {
	    /** You must add a light to make the model visible */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -0.5f).normalizeLocal());
        sun.setColor(ColorRGBA.Red);
        rootNode.addLight(sun);    	
        // sky
        rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/BrightSky.dds", false));		        
        // floor
        rootNode.setShadowMode(ShadowMode.Off);
        Material mat = assetManager.loadMaterial("Common/Materials/WhiteColor.j3m");  
        Box floor = new Box(Vector3f.ZERO, 300, 0.1f, 300);
        Geometry floorGeom = new Geometry("Floor", floor);
        floorGeom.setMaterial(mat);
        floorGeom.setLocalTranslation(0,-0.2f,0);
        rootNode.attachChild(floorGeom);        
        // model   
        Spatial model = assetManager.loadModel("myAssets/Models/TheRedPyramid/TheRedPyramid.obj");		
		rootNode.attachChild(model);
		
		// PApplet
		//CapturePApplet 			applet = new CapturePApplet();
		//CaptureBinarizePApplet	applet = new CaptureBinarizePApplet();	
		CaptureSkewPApplet		applet = new CaptureSkewPApplet();	
		//CaptureBinarizeSkewPApplet	applet = new CaptureBinarizeSkewPApplet();
		
		// CaptureCamera
		CaptureCameraNode captureCameraNode=null;
		if(applet.realDeployment == false){
        	captureCameraNode = new CaptureCameraNode("cameraNode", 400, 300, assetManager, renderManager, renderer, rootNode);
        	applet.setCapture(captureCameraNode.getCapture()); // set Capture to PApplet
        	rootNode.attachChild(captureCameraNode);         
        }		
		
		/*
		 * When you send a virtual camera image to a PApplet and output the processed-image to a projector,
		 * please use same the width and the height among the camera, the PApplet and the projector. 
		 */		
		
		// Projector
        PAppletProjectorNode projectorNode = new PAppletProjectorNode("projector", assetManager, applet, 400, 300, true);        
        rootNode.attachChild(projectorNode);
        rootNode.attachChild(projectorNode.getFrustmModel()); // if you don't want to see frustum, please don't attach it to rootNode. 
        //projector should be added to TextureProjectorRenderer, and TextureProjectorRenderer should be added to ViewPort.
		TextureProjectorRenderer ptr = new TextureProjectorRenderer(assetManager);
		ptr.getTextureProjectors().add(projectorNode.getProjector());
		viewPort.addProcessor(ptr);        
 		
		model.setShadowMode(ShadowMode.CastAndReceive);	
		floorGeom.setShadowMode(ShadowMode.Receive);
		
		projectorNode.setLocalTranslation(Vector3f.UNIT_XYZ.mult(100.0f));
		captureCameraNode.setLocalTranslation(Vector3f.UNIT_XYZ.mult(100.0f));
		projectorNode.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		captureCameraNode.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

		// Camera
		cam.setLocation(Vector3f.UNIT_XYZ.mult(50.0f));  
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);           
		flyCam.setMoveSpeed(10);
		flyCam.setDragToRotate(true);
	}
		
	public void destroy() {
		super.destroy();
		System.exit(0);	// we should terminate the thread of PApplet.		
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloCaptureAndProjection();		
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.start();
	}
}