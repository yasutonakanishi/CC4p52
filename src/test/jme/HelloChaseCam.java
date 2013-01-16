package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Quad;

public class HelloChaseCam extends  SimpleApplication implements AnalogListener, ActionListener{	

	private Spatial girl;
	private ChaseCamera chaseCam;
	private CameraNode camNode;
	private Node girlCameraNode;
	  
	@Override
	public void simpleInitApp() {
        // sun
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(1f, -1f, 1f).normalizeLocal());
        rootNode.addLight(sun);
	    
        // ground
	    Material mat_ground = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	    mat_ground.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
	    Geometry ground = new Geometry("ground", new Quad(50, 50));
	    ground.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
	    ground.setLocalTranslation(-25, 0, 25);
	    ground.setMaterial(mat_ground);
	    rootNode.attachChild(ground);

	    // object
	    girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
	    girl.rotate(0, FastMath.PI, 0);
	    rootNode.attachChild(girl);
	    

	    // Disable the default first-person cam!
	    flyCam.setEnabled(false);
	    flyCam.setDragToRotate(true);
	    // one implementation a camera follows an object	
	    /*
	     * CameraNode
	     */
	    camNode = new CameraNode("CamNode", cam);
	    camNode.setControlDir(ControlDirection.SpatialToCamera);
	    girlCameraNode = new Node();
	    girlCameraNode.attachChild(girl);
	    girlCameraNode.attachChild(camNode);
	    camNode.setLocalTranslation(new Vector3f(0, 0.8f, -3));
	    rootNode.attachChild(girlCameraNode);
	    
	    // another implementation a camera follows an object	   
	    /*
	     * ChaseCam
	     */
//	    chaseCam = new ChaseCamera(cam, girl, inputManager);
//	    chaseCam.setSmoothMotion(true);
//	    chaseCam.setLookAtOffset(Vector3f.UNIT_Y.mult(3));
	    	    
	    registerInput();
	}
	
	public void registerInput() {
		inputManager.addMapping("moveForward", new KeyTrigger(keyInput.KEY_UP), new KeyTrigger(keyInput.KEY_W));
		inputManager.addMapping("moveBackward", new KeyTrigger(keyInput.KEY_DOWN), new KeyTrigger(keyInput.KEY_S));
		inputManager.addMapping("moveRight", new KeyTrigger(keyInput.KEY_RIGHT), new KeyTrigger(keyInput.KEY_D));
		inputManager.addMapping("moveLeft", new KeyTrigger(keyInput.KEY_LEFT), new KeyTrigger(keyInput.KEY_A));
		inputManager.addMapping("displayPosition", new KeyTrigger(keyInput.KEY_P));
		inputManager.addListener(this, "moveForward", "moveBackward", "moveRight", "moveLeft");
		inputManager.addListener(this, "displayPosition");
	}

	public void onAnalog(String name, float value, float tpf) {
		if (name.equals("moveForward")) {			
			if(girlCameraNode!=null)
				girlCameraNode.move(0, 0, -5 * tpf);
			if(chaseCam !=null)
				girl.move(0, 0, -5 * tpf);
		}
		if (name.equals("moveBackward")) {		    
			if(girlCameraNode!=null)
				girlCameraNode.move(0, 0, 5 * tpf);
			if(chaseCam !=null)
				girl.move(0, 0, 5 * tpf);
		}
		if (name.equals("moveRight")) {
			if(girlCameraNode!=null)
				girlCameraNode.move(5 * tpf, 0, 0);
			if(chaseCam !=null)
				girl.move(5 * tpf, 0, 0);
		}
		if (name.equals("moveLeft")) {
			if(girlCameraNode!=null)
				girlCameraNode.move(-5 * tpf, 0, 0);
			if(chaseCam !=null)
				girl.move(-5 * tpf, 0, 0);
		}
	}

	public void onAction(String name, boolean keyPressed, float tpf) {
		if (name.equals("displayPosition") && keyPressed) {
			if(girlCameraNode!=null)
				girlCameraNode.move(10, 10, 10);
			if(chaseCam !=null)
				girl.move(10, 10, 10);
		}
	}

	public static void main(String[] args) {
		SimpleApplication app = new HelloChaseCam();
		app.start();
	}
}
