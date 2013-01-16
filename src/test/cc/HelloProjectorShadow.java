package test.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import processing.core.PApplet;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class HelloProjectorShadow extends SimpleApplication{

	@Override
	public void simpleInitApp() {               		
		//sun
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		dl.setColor(ColorRGBA.Orange);
		rootNode.addLight(dl);
	    //ambient light
	    AmbientLight al = new AmbientLight();
	    al.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1.0f));
	    rootNode.addLight(al);
	    
		//floor             	
		Material textureMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		textureMat.setTexture("ColorMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));		
		Box floor = new Box(Vector3f.ZERO, 20.0f, 0.01f, 20.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(textureMat);
		rootNode.attachChild(floorGeom);
        
        //object 
        Spatial tree = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
        tree.setQueueBucket(Bucket.Transparent);
        rootNode.attachChild(tree);
        
        //projector
        PApplet applet = new ColorBarsPApplet();

        PAppletProjectorShadowNode ppg = new PAppletProjectorShadowNode("Projector", assetManager, viewPort, 1024, 768, applet, 400,300,false);        
        rootNode.attachChild(ppg);
        ppg.setLocalTranslation(new Vector3f(0,10,0));		 // move the projector,
        ppg.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_X);// and make it to look at where you want.
        
		//projector is a kind of Shadow, and following processes are necessary for Shadow Rendering. 		
		floorGeom.setShadowMode(ShadowMode.Receive);
		tree.setShadowMode(ShadowMode.CastAndReceive); // tree makes and receives shadow
		                
		//cam
		cam.setLocation(Vector3f.UNIT_XYZ.mult(10.0f)); // camera moves to 10, 10, 10 
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);     // and looks at 0,0,0.         
	}
	
	public void destroy() {
		super.destroy();
		// we should terminate the thread of PApplet.
		System.exit(0);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloProjectorShadow();
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.
		app.start();
	}

}
