package simulation.cc;

import net.unitedfield.cc.CaptureCameraNode;
import net.unitedfield.cc.PAppletDisplayGeometry;
import test.p5.Mirror2PApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class CaptureAppletSimulation extends SimpleApplication {
	CaptureCameraNode captureCameraNode;
	float angle;	
	
	@Override
	public void simpleInitApp() {		
        //init scene
        Node sceneNode = new Node("Scene");
        // load sky
        sceneNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));        
        rootNode.attachChild(sceneNode);        
        //add light 
        DirectionalLight dl1 = new DirectionalLight();
	    dl1.setDirection(new Vector3f(1f, -1f, -1).normalizeLocal());
	    rootNode.addLight(dl1);
    
	    //add display that shows a PApplet
        PAppletDisplayGeometry display;
        Mirror2PApplet applet = new Mirror2PApplet();
        if(applet.realDeployment == false){
        	captureCameraNode = new CaptureCameraNode("cameraNode", 640, 480, assetManager, renderManager, renderer, rootNode);
        	rootNode.attachChild(captureCameraNode);
        	applet.setCapture(captureCameraNode.getCapture());
        }
        
        /** In order to process image from camera at PApplet adequately,
         please use same width and height at CaptureCameraNode and PApplet */
                
		display = new PAppletDisplayGeometry("display", assetManager, 6.4f, 4.8f, applet, 640, 480, true);		
		this.rootNode.attachChild(display);		
		display.setLocalTranslation(0, 0, -3);
		
		flyCam.setMoveSpeed(20);		
	}
	
	public void simpleUpdate(float tpf) {
    	angle += 0.01;    	
    	if(captureCameraNode!=null){
    		Quaternion q = captureCameraNode.getLocalRotation();
    		//captureCameraNode.setLocalRotation(q.fromAngles(0, angle, 0));    	
    		captureCameraNode.rotate(0, 0.01f, 0f);
    	}
    }
	
	public void putArrow(Vector3f pos, Vector3f dir, ColorRGBA color){
        Arrow arrow = new Arrow(dir);
        arrow.setLineWidth(4); // make arrow thicker
        putShape(arrow, color).setLocalTranslation(pos);
    }
    public Geometry putShape(Mesh shape, ColorRGBA color){
        Geometry g = new Geometry("shape", shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
    
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		CaptureAppletSimulation app = new CaptureAppletSimulation();
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.				
		app.start();
	}
}
