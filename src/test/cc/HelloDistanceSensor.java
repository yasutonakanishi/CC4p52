package test.cc;

import net.unitedfield.cc.DistanceSensorNode;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class HelloDistanceSensor extends SimpleApplication {
	BitmapText hintText;
	Spatial walkingGirl;
	Spatial	targetGolem;
	Node 	senseTarget;
	DistanceSensorNode distanceSensor;
	
	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(10);
		
		setupEnvironment();
		setupDebugShapes();
		setupKeys();
		loadHintText();
		              		
		walkingGirl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		walkingGirl.setLocalTranslation(1,0,3);
        targetGolem = assetManager.loadModel("myAssets/Models/Oto/Oto.mesh.xml");
        targetGolem.scale(0.5f);
        targetGolem.setLocalTranslation(-1.0f, 0f, -0.6f);
        // We must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        targetGolem.addLight(sun);
        
        senseTarget = new Node();
        senseTarget.attachChild(walkingGirl);
        senseTarget.attachChild(targetGolem);
        rootNode.attachChild(senseTarget);
        
        distanceSensor = new DistanceSensorNode("sensor", 5f, assetManager, senseTarget);	// sense at simpleUpdate
        distanceSensor.move(3, 1, -.5f);   
		rootNode.attachChild(distanceSensor);
	}
	
	public void simpleUpdate(float tpf) {	
		System.out.println("closest distance: " + distanceSensor.getDistance());		
	}
	
	private void setupEnvironment() {		
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);		
			
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));									
	}
	
	private void setupDebugShapes(){
		putArrow(Vector3f.ZERO, Vector3f.UNIT_X, ColorRGBA.Red);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Y, ColorRGBA.Green);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Z, ColorRGBA.Blue);	        
        putGrid(new Vector3f(0, 0, 0), ColorRGBA.White);
	}
    public void putGrid(Vector3f pos, ColorRGBA color){
        putShape(new Grid(200, 200, 1.0f), color).center().move(pos);
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

	public void loadHintText() {
		hintText = new BitmapText(guiFont, false);
		hintText.setSize(guiFont.getCharSet().getRenderedSize());
		hintText.setLocalTranslation(0, getCamera().getHeight(), 0);
		hintText.setText("Hit U/H/J/N/Y/B to move the sensor, \nand W/A/S/Z to move the camera.");		
		guiNode.attachChild(hintText);
	}
	
	private void setupKeys() {		
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_N));
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_Y));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_B));
		
		inputManager.addListener(analogListener, new String[]{
        		"forward", "left", "right", "back", "up", "down"
             });
	}

	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {	
			if (name.equals("forward")) {
				distanceSensor.move(0,0,2*tpf); 
			}else if (name.equals("left") ) {
				distanceSensor.move(-2*tpf,0,0);
            }else if (name.equals("right")) {
            	distanceSensor.move(2*tpf,0,0); 
            }else if (name.equals("back") ) {
            	distanceSensor.move(0,0,-2*tpf); 
            }else if (name.equals("up") ) {
            	distanceSensor.move(0,2*tpf,0); 
            }else if (name.equals("down") ) {
            	distanceSensor.move(0,-2*tpf,0); 
            }	
		}
	};
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloDistanceSensor();
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.		
		app.start();
	}
}
