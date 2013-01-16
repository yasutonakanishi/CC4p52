package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class HelloModel extends SimpleApplication {
	
	/*
	 * detailed information is here,
	 * http://jmonkeyengine.org/wiki/doku.php/jme3:beginner:hello_asset
	 */
	
	@Override
	public void simpleInitApp() {        
		/** Load a girl model **/
		/** Scale, rotate, move **/
        Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");        
        //girl.rotate(0.0f, -3.0f, 0.0f);
        //girl.setLocalTranslation(0.0f, -5.0f, -2.0f);
        rootNode.attachChild(girl);
        
        Spatial bigGirl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
        bigGirl.scale(10f);
        bigGirl.rotate(0f, 1f, 0f);
        bigGirl.setLocalTranslation(0f, -5f, 0f);
        //rootNode.attachChild(bigGirl);        
        
        Spatial model = assetManager.loadModel("myAssets/Models/imazato/imazato.obj");
        rootNode.attachChild(model);
        
        /** You must add a light to make the model visible */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);
	}

	public static void main(String[] args) {
		SimpleApplication app = new HelloModel();
		app.start();
	}
}
