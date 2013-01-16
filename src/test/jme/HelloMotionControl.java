package test.jme;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionTrack;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class HelloMotionControl extends SimpleApplication {
    protected Geometry player;

    @Override
    public void simpleInitApp() {
        DirectionalLight light = new DirectionalLight();
        light.setDirection(new Vector3f(0, -1, 0).normalizeLocal());
        light.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(light);
        
        /** this blue box is our player character */
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        player = new Geometry("blue cube", b);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);
        rootNode.attachChild(player);
        
        initNPC();
    }

    private void initNPC(){ // non player character    
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 1f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient", ColorRGBA.Black);
        mat.setColor("Diffuse", ColorRGBA.DarkGray);
        mat.setColor("Specular", ColorRGBA.White.mult(0.6f));  
        Spatial teapot = assetManager.loadModel("Models/Teapot/Teapot.obj");
        teapot.setName("Teapot");
        teapot.setLocalScale(3);
        teapot.setMaterial(mat);
        rootNode.attachChild(teapot);
          
        MotionPath path = new MotionPath();
        path.setCycle(true);
        path.addWayPoint(new Vector3f(20, 3, 0));
        path.addWayPoint(new Vector3f(0, 3, 20));
        path.addWayPoint(new Vector3f(-20, 3, 0));
        path.addWayPoint(new Vector3f(0, 3, -20));
        path.setCurveTension(0.83f);
        path.enableDebugShape(assetManager, rootNode);
        
        MotionTrack motionControl = new MotionTrack(teapot, path, LoopMode.Loop);
        motionControl.setDirectionType(MotionTrack.Direction.PathAndRotation);
        motionControl.setInitialDuration(10f);
        motionControl.setSpeed(2f);
        motionControl.play();
        // teapot runs on a motionpath, but no need to control in simpleUpdate        
        // you can add more NPC on various motionpath
    }
    
    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        // make the player rotate:
        player.rotate(0, 2*tpf, 0);                
    }
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloMotionControl();
		app.start();
	}
}
