package simulation.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import processing.core.PApplet;
import simulation.p5.DynamicParticlesRetained;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.system.AppSettings;


public class ShadowProjection2TokyoStation extends SimpleApplication {
	PAppletProjectorShadowNode ppsn;
	float phase;
	
	@Override
	public void simpleInitApp() {		
		/** model of TokyoStation **/
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/FullskiesBlueClear03.dds", false));		
		Spatial model = assetManager.loadModel("myAssets/Models/TokyoStation/TokyoStation.obj");
		rootNode.attachChild(model);
		model.move(-18,0,26);
		model.rotate(0,0.3f,0);
		model.setShadowMode(ShadowMode.CastAndReceive);
        /** You must add a light to make the model visible */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.1f, -0.7f, -0.1f).normalizeLocal());
        sun.setColor(ColorRGBA.White.clone().multLocal(2));        
        rootNode.addLight(sun);           
        
        /** move camera position and direction **/
        cam.setLocation(new Vector3f(-150.0f,150.0f,-70.0f));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);      // and looks at 0,0,0.                      
        /** cam is a camera for rendering GL,and flyCam is object for moving the camera. **/
        flyCam.setMoveSpeed(30);
		flyCam.setDragToRotate(true);
				   	 	   
        // PApplet
        PApplet applet = new DynamicParticlesRetained();         
        this.setPauseOnLostFocus(false); // call this method in order not to pause in clicking on a window of the applet.		

        // projector in front of the station
        ppsn = new PAppletProjectorShadowNode("Projector", assetManager, viewPort, 30, 200, 960, 240, applet, 640, 480, true);
		rootNode.attachChild(ppsn);	
		ppsn.setLocalTranslation(-150f, 1f, 0);
		ppsn.lookAt(new Vector3f(0, 1,0), Vector3f.UNIT_Y);
		
        setupDebugShapes(); // as you like
	}
	
//	public void simpleUpdate(float tpf){				
//		phase += 0.0001f;
//		Quaternion q = new Quaternion();
//	    q.fromAngles(0, 0, phase);
//	    ppn.rotate(q);	      
//	}
	
	private void setupDebugShapes(){
		putArrow(Vector3f.ZERO, Vector3f.UNIT_X, ColorRGBA.Red);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Y, ColorRGBA.Green);
        putArrow(Vector3f.ZERO, Vector3f.UNIT_Z, ColorRGBA.Blue);	        
        //putGrid(new Vector3f(0, 0, 0), ColorRGBA.White);
	}
    public void putGrid(Vector3f pos, ColorRGBA color){
        putShape(new Grid(200, 200, 1.0f), color).center().move(pos);
    }
	public void putArrow(Vector3f pos, Vector3f dir, ColorRGBA color){
        Arrow arrow = new Arrow(dir);
        arrow.setLineWidth(4); // make arrow thicker
        arrow.setArrowExtent(dir.mult(100));
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
	
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new ShadowProjection2TokyoStation();
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.start();
	}
}
