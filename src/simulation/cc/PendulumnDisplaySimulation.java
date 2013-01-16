package simulation.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import simulation.p5.PendulumPApplet;
import simulation.p5.PendulumnAndBallSystemPApplet;
import test.jmetest.bullet.PhysicsTestHelper;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;

public class PendulumnDisplaySimulation extends SimpleApplication implements AnalogListener {

	BulletAppState bulletAppState;
	PApplet applet;
	Vector3f appletVel, appletAcc;
	RigidBodyControl displayRigidBodyControl;
	private HingeJoint joint;

    private void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Swing", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left", "Right", "Swing");
    }
    public void onAnalog(String binding, float value, float tpf) {
    	if(binding.equals("Left")){
            joint.enableMotor(true, 1, .1f);
        }
        else if(binding.equals("Right")){
            joint.enableMotor(true, -1, .1f);
        }
        else if(binding.equals("Swing")){
            joint.enableMotor(false, 0, 0);
        }
    }
    
	@Override
	public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);        
        
        setupEnvironment();
    	
        //setupJoint();
        //setupTexturePendlumn();        
        setupPAppletDisplayPendulum();
        appletVel	= new Vector3f();
        
        setupKeys();         
	}

    private PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
    
    public void simpleUpdate(float tpf) {    	
    	if(applet != null)	{
    		double appletAngle = 2*Math.PI - this.joint.getHingeAngle();
    		((PendulumPApplet)applet).setAppletAngle(appletAngle);        		
    	}
    }
 
    public void setupJoint() {
    	Node holderNode=PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f( .1f, .1f, .1f)),0);
        holderNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f,0,0f));
        rootNode.attachChild(holderNode);
        getPhysicsSpace().add(holderNode);

        Node hammerNode=PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f( .3f, .3f, .3f)),1);
        hammerNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f,1,0f));
        rootNode.attachChild(hammerNode);
        getPhysicsSpace().add(hammerNode);

        joint=new HingeJoint(holderNode.getControl(RigidBodyControl.class), hammerNode.getControl(RigidBodyControl.class), 
        		Vector3f.ZERO, new Vector3f(0f,-1,0f), Vector3f.UNIT_Z, Vector3f.UNIT_Z);
        getPhysicsSpace().add(joint);
	}
    	
    public void setupTexturePendlumn(){
    	Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture("assets/Interface/Logo/Monkey.jpg"));
        
    	Node holderNode=PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f( .1f, .1f, .1f)),0);
        holderNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f,0,0f));
        rootNode.attachChild(holderNode);
        getPhysicsSpace().add(holderNode);
    
        Box box = new Box(0.25f, 0.25f, 0.25f);
        Geometry boxGeometry = new Geometry("Box", box);
        boxGeometry.setMaterial(material);        
        boxGeometry.addControl(new RigidBodyControl(1));
        boxGeometry.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f,1,0f));
        rootNode.attachChild(boxGeometry);
        getPhysicsSpace().add(boxGeometry);
        
        joint=new HingeJoint(holderNode.getControl(RigidBodyControl.class), boxGeometry.getControl(RigidBodyControl.class), 
        		Vector3f.ZERO, new Vector3f(0f,-1,0f), Vector3f.UNIT_Z, Vector3f.UNIT_Z);        
        getPhysicsSpace().add(joint);
    }
    
	private void setupPAppletDisplayPendulum() {

		Node holderNode=PhysicsTestHelper.createPhysicsTestNode(assetManager, new BoxCollisionShape(new Vector3f( .1f, .1f, .1f)),0);
        holderNode.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(0f,2f,0f));
        rootNode.attachChild(holderNode);
        getPhysicsSpace().add(holderNode);
        	
		applet = new PendulumnAndBallSystemPApplet();		
		PAppletDisplayGeometry display = new PAppletDisplayGeometry("display0", assetManager, 1.0f, 1.0f, applet, 300, 300, false);
		this.rootNode.attachChild(display);
		
		this.displayRigidBodyControl = new RigidBodyControl(1); // RigidBodyControl(float mass)
		display.addControl(this.displayRigidBodyControl);		
		display.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(1.41421356f,1.41421356f,0f)); // 45degree
		getPhysicsSpace().add(display);
		
		joint=new HingeJoint(holderNode.getControl(RigidBodyControl.class), display.getControl(RigidBodyControl.class), 
				Vector3f.ZERO, new Vector3f(0f, 2, 0f), Vector3f.UNIT_Z, Vector3f.UNIT_Z);  
        getPhysicsSpace().add(joint);          
	}
	
	private void setupEnvironment() {		
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);		
			
		// How to add a Sky to your Scene
		// http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:sky	
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));
		
		Spatial walkingGirl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		rootNode.attachChild(walkingGirl);
	}

	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		PendulumnDisplaySimulation app = new PendulumnDisplaySimulation();
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.		
		app.start();
	}
}
