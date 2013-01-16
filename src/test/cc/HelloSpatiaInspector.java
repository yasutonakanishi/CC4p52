package test.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import net.unitedfield.cc.PAppletProjectorShadowNode;
import net.unitedfield.cc.util.SpatialInspector;
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
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;

public class HelloSpatiaInspector extends SimpleApplication {
	
	@Override
	public void simpleInitApp() {
		//cam
		cam.setLocation(Vector3f.UNIT_XYZ.mult(15.0f));   // camera moves to 15, 15, 15 
		cam.lookAt(new Vector3f(0,5,0), Vector3f.UNIT_Y); // and looks at 0,0,0. 
		flyCam.setMoveSpeed(10);
		flyCam.setDragToRotate(true);
		        
		// light
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		dl.setColor(ColorRGBA.Orange);
		rootNode.addLight(dl);
		// floor             	
		Material textureMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		textureMat.setTexture("ColorMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));		
		Box floor = new Box(Vector3f.ZERO, 20.0f, 0.01f, 20.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(textureMat);
		rootNode.attachChild(floorGeom);        
        // tree 
        Spatial tree = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
        tree.setQueueBucket(Bucket.Transparent);
        rootNode.attachChild(tree);
        		       
        // ProjectorShadowNode
		PAppletProjectorShadowNode ppg = new PAppletProjectorShadowNode("Projector",assetManager,viewPort, 200,200, (Texture2D)assetManager.loadTexture("Interface/Logo/Monkey.png"));		
        rootNode.attachChild(ppg);
        rootNode.attachChild(ppg.getFrustmModel());       
        ppg.setLocalTranslation(new Vector3f(0,10,0));		 
        ppg.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Y);
		//projector is a kind of Shadow, and following processes are necessary for Shadow Rendering. 		
		floorGeom.setShadowMode(ShadowMode.Receive);
		tree.setShadowMode(ShadowMode.CastAndReceive); // tree makes and receives shadow
		
		// PApplet and PAppletDisplayGeometry
		// flat display
		PApplet applet0 = new ColorBarsPApplet();
		PAppletDisplayGeometry flatDisplay = new PAppletDisplayGeometry("FlatDisplay", assetManager, 4, 4, applet0, 200, 200, false);
		rootNode.attachChild(flatDisplay);		
		flatDisplay.setLocalTranslation(-10, 4, 0);
		flatDisplay.rotate(0, (float)Math.PI/2, 0);		
		// sphere display
		PApplet applet1 = new ColorBarsPApplet();
		Mesh sphere = new Sphere(20, 20, 0.8f);
		PAppletDisplayGeometry sphereDisplay = new PAppletDisplayGeometry("SphereDisplay", sphere, assetManager, applet1, 200, 200, false);
		rootNode.attachChild(sphereDisplay);
		sphereDisplay.setLocalTranslation(8, 5, 0);		
		
		/*
		 * Get a instance of SpatialInspector, and add it to each object as control.
		 */
		SpatialInspector spatialInspector = SpatialInspector.getInstance();
		ppg.addControl(spatialInspector);
		tree.addControl(spatialInspector);
		flatDisplay.addControl(spatialInspector);
		sphereDisplay.addControl(spatialInspector);
		this.setPauseOnLostFocus(false);
		spatialInspector.show();		
	}
	
	public void destroy() {
		super.destroy();
		System.exit(0);	// we should terminate the thread of PApplet.
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloSpatiaInspector();		
		app.start();
	}
}