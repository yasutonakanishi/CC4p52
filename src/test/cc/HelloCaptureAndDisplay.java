package test.cc;

import net.unitedfield.cc.CaptureCameraNode;
import net.unitedfield.cc.PAppletDisplayGeometry;
import test.p5.CaptureBinarizePApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.util.SkyFactory;

public class HelloCaptureAndDisplay extends SimpleApplication {
	private CaptureCameraNode captureCameraNode;	
	@Override
	public void simpleInitApp() {		
		//sky
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));  
				
		// PApplet
		CaptureBinarizePApplet applet = new CaptureBinarizePApplet(); // for simulation, please set realDeployment = false;
		
		// CaptureCamera
		captureCameraNode=null;
		if(applet.realDeployment == false){
        	captureCameraNode = new CaptureCameraNode("cameraNode", 400, 300, assetManager, renderManager, renderer, rootNode);
        	rootNode.attachChild(captureCameraNode);
        	applet.setCapture(captureCameraNode.getCapture()); // set Capture to PApplet
        	captureCameraNode.rotate(0, (float)Math.PI/2, 0);    		
        }
		
		// Display
		PAppletDisplayGeometry display = new PAppletDisplayGeometry("display", assetManager, 4, 3, applet, 400, 300, true);			
		rootNode.attachChild(display);
		display.setLocalTranslation(0, 0, -3);		
	}

	public void simpleUpdate(float tpf) {
        // make the player rotate:
		if(captureCameraNode != null)
			captureCameraNode.rotate(0f, 0.2f*tpf, 0f); 
    }
	
	public void destroy() {
		super.destroy();
		// we should terminate the thread of PApplet.
		System.exit(0);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloCaptureAndDisplay();
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.
		app.start();
	}
}
