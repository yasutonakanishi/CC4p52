package test.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import test.p5.ColorBarsPApplet;
import test.p5.JpgFlipPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

public class HelloDisplay extends SimpleApplication {
	
	@Override
	public void simpleInitApp() {
		//sun
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -0.1f).normalizeLocal());
		rootNode.addLight(dl);
		
		//sky
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));
			
		PApplet applet = new ColorBarsPApplet();
		PAppletDisplayGeometry display0 = new PAppletDisplayGeometry(
				"display0",		// name  				
				assetManager,   //
				2.0f, 3.6f,		// size of box in jME
				applet,			// PApplet instance name 
				100, 180,       // size of PApplet
				true);		    // if another window for PApplet is shown. P2D or P3D PApplet needs to show a window.
		rootNode.attachChild(display0);		
		display0.rotate((float)Math.PI/2, 0, 0);
		display0.setLocalTranslation(0, 0, 5);		
		
		PApplet applet1 = new ColorBarsPApplet();
		Mesh sphere = new Sphere(20, 20, 0.8f);
		//Default display is a box, and various shapes are used as display geometry.
		PAppletDisplayGeometry display1 = new PAppletDisplayGeometry(
				"display1",
				sphere,	// set a Mesh instance instead of box width and height 
				assetManager,
				applet1,
				100,180,
				true);
		rootNode.attachChild(display1);
		display1.move(0, 2,0);
		display1.rotate(-(float)Math.PI/2, (float)Math.PI/2, 0);
		
		//PApplet applet2 = new LoopMoviePApplet(); // only as a PApplet it works, but in jME, it fails to load a mov.file...orz
		// instead of showing a movie, once export a movie to jpg files, and load them.
		PApplet applet2 = new JpgFlipPApplet("transitimages",750, 640, 360);
		PAppletDisplayGeometry display2 = new PAppletDisplayGeometry(
				"display2",		// name  
				assetManager,   // 
				6.4f, 3.6f,		// size of box in jME
				applet2,		// PApplet instance name 
				640, 360,       // size of PApplet
				true);		    // if another window for PApplet is shown. P2D or P3D PApplet needs to show a window.
		rootNode.attachChild(display2);
		display2.setLocalTranslation(0, 0, -10);				
	}

	public void destroy() {
		super.destroy();
		// we should terminate the thread of PApplet.
		System.exit(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleApplication app = new HelloDisplay();
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.
		app.start();
	}
}
