package simulation.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import test.p5.Bounce;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class P52DisplaySimulation extends SimpleApplication {		
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
        PApplet applet = new Bounce();		  	  	        
		display = new PAppletDisplayGeometry("display",assetManager, 6.4f, 2.0f, applet, 640, 200, true);		
		this.rootNode.attachChild(display);		
	}
		
	@Override
	public void simpleUpdate(float tpf) {
	}
	
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new P52DisplaySimulation();
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.		
		app.start();
	}
}