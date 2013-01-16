package simulation.cc;

import java.io.File;

import net.unitedfield.cc.PAppletDisplayGeometry;
import simulation.p5.SyncPAppletMaster;
import simulation.p5.SyncPAppletSlave;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.water.SimpleWaterProcessor;


public class SyncPAppletSimulation extends SimpleApplication {
	PAppletDisplayGeometry[] 	displayArray;	
    boolean useHttp = true;    
	
	@Override
	public void simpleInitApp() {		
        Node mainScene = new Node();
		// sky
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));
		// light
        //DirectionalLight dl = new DirectionalLight();
        //dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        //rootNode.addLight(dl);			
        // load house
        File file = new File("wildhouse.zip");
        if (file.exists()) {
            useHttp = false;
        }
        // create the geometry and attach it
        // load the level from zip or http zip
        if (useHttp) {
            assetManager.registerLocator("http://jmonkeyengine.googlecode.com/files/wildhouse.zip", HttpZipLocator.class.getName());
        } else {
            assetManager.registerLocator("wildhouse.zip", ZipLocator.class.getName());
        }
        Spatial scene = assetManager.loadModel("main.scene");
        DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir=new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        scene.addLight(sun);        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        // add lightPos Geometry
        Sphere lite=new Sphere(8, 8, 3.0f);
        Geometry lightSphere=new Geometry("lightsphere", lite);
        lightSphere.setMaterial(mat);
        Vector3f lightPos=lightDir.multLocal(-400);
        lightSphere.setLocalTranslation(lightPos);
        rootNode.attachChild(lightSphere);

        /*
         * making lake
         */
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(mainScene);
        waterProcessor.setDebug(false);
        waterProcessor.setLightPosition(lightPos);
        waterProcessor.setRefractionClippingOffset(1.0f);
        //setting the water plane
        Vector3f waterLocation=new Vector3f(0,-20,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        //WaterUI waterUi=new WaterUI(inputManager, waterProcessor);
        //waterProcessor.setWaterColor(ColorRGBA.Brown);
        waterProcessor.setWaterColor(ColorRGBA.Blue);        
        waterProcessor.setDebug(false);
        //lower render size for higher performance
        //waterProcessor.setRenderSize(128,128);
        //raise depth to see through water
        waterProcessor.setWaterDepth(20);
        //lower the distortion scale if the waves appear too strong
        waterProcessor.setDistortionScale(0.03f);
        //lower the speed of the waves if they are too fast
        waterProcessor.setWaveSpeed(0.03f);
        
        Quad quad = new Quad(400,400);
        //the texture coordinates define the general size of the waves
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));
        Geometry water=new Geometry("water", quad);
        water.setShadowMode(ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.setLocalTranslation(-200, -20, 250);
        
        
		//several synchronizing PApplets
		String message = "0123456789   ";		
		SyncPAppletMaster master = new SyncPAppletMaster(message);
		displayArray = new PAppletDisplayGeometry[message.length()];
		for(int i=0; i<message.length(); i++){
			SyncPAppletSlave slave = new SyncPAppletSlave(i, message);
			master.addSlave(slave);
			//
			displayArray[i] = new PAppletDisplayGeometry("display"+i, assetManager, 5.0f, 5.0f, slave, 200, 200, true);
			displayArray[i].setLocalTranslation(-i*6, -22.f, 40);
			displayArray[i].setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
			mainScene.attachChild(displayArray[i]);
		}		
		this.setPauseOnLostFocus(false); // call this method in order not to pause in clicking on a window of the applet.
		master.start();

        rootNode.attachChild(water);
        viewPort.addProcessor(waterProcessor);
        mainScene.attachChild(scene);
        rootNode.attachChild(mainScene);
        
    	flyCam.setDragToRotate(true);
    	flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(-70.0f, 1.0f, 85.0f));
        cam.setRotation(new Quaternion(0.03f, 0.9f, 0f, 0.4f));
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		Quaternion q = new Quaternion().fromAngleAxis(-0.001f, Vector3f.UNIT_X);	
		for(int i=0; i<displayArray.length; i++)
			displayArray[i].rotate(q);			
	}
	
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
		
	public static void main(String[] args) {
		SimpleApplication app = new SyncPAppletSimulation();
		app.start();
	}
}
