package simulation.cc;

import java.io.File;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import simulation.p5.Esfera;

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
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.water.SimpleWaterProcessor;

public class LakeDisplaySimulation extends SimpleApplication {
    boolean useHttp = true;
    Node mainScene;
    PAppletDisplayGeometry display;
    float phase;
    
    public void simpleInitApp() {
    	/*
    	 * making scene
    	 */
        mainScene=new Node();
        // load sky
        //mainScene.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
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
        //add lightPos Geometry
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
        waterProcessor.setDistortionScale(0.01f);
        //lower the speed of the waves if they are too fast
        waterProcessor.setWaveSpeed(0.01f);

        Quad quad = new Quad(400,400);
        //the texture coordinates define the general size of the waves
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));
        Geometry water=new Geometry("water", quad);
        water.setShadowMode(ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.setLocalTranslation(-200, -20, 250);
        
        rootNode.attachChild(water);
        viewPort.addProcessor(waterProcessor);
        mainScene.attachChild(scene);
        rootNode.attachChild(mainScene);
        
    	/*
    	 * making a sphere display
    	 */
    	Mesh sphere = new Sphere(20,20,10f);
    	PApplet applet = new Esfera();
    	display = new PAppletDisplayGeometry("display0", sphere, assetManager, applet, 400, 400, true);
    	this.setPauseOnLostFocus(false); // call this method in order not to pause in clicking on a window of the applet.    	
    	mainScene.attachChild(display); // in order to reflect on the water
    	display.rotate(0,0,(float)Math.PI/2);
    	display.setLocalTranslation(-10,-10,50);
    	
    	flyCam.setDragToRotate(true);
    	flyCam.setMoveSpeed(10);
        cam.setLocation(new Vector3f(-70.0f, 1.0f, 85.0f));
        cam.setRotation(new Quaternion(0.03f, 0.9f, 0f, 0.4f));
    }
    
	public void simpleUpdate(float tpf){				
		phase += 0.0000001f;
		Quaternion q = new Quaternion();
		q.fromAngles(phase, phase, phase);
		display.rotate(q);	      
	}
    
    public void destroy() {
		super.destroy();
		// we should terminate the thread of PApplet.
		System.exit(0);
	}
      
	public static void main(String[] args) {
		SimpleApplication app = new LakeDisplaySimulation();
		app.start();
	}
}
