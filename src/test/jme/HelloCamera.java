package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

public class HelloCamera extends SimpleApplication {

	@Override
	public void simpleInitApp() {
		/** model of TokyoStation **/
		rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/FullskiesBlueClear03.dds", false));		
//		Spatial model = assetManager.loadModel("myAssets/Models/TokyoStation/TokyoStation.obj");
//		rootNode.attachChild(model);
        /** You must add a light to make the model visible */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
		        
        /** move camera position and direction **/
        cam.setLocation(Vector3f.UNIT_XYZ.mult(100.0f)); // camera moves to 100.0, 100.0, 100.0 
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);      // and looks at 0,0,0.                      
        /** cam is a camera for rendering GL,and flyCam is object for moving the camera. **/
        flyCam.setMoveSpeed(30);
		flyCam.setDragToRotate(true);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloCamera();
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.start();
	}
}
