package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class HelloGuiCameraPos extends SimpleApplication {	
	
	BitmapText helpText;
	@Override
	public void simpleInitApp() {
		// you can select showing fps or statview is true or false.
        //setDisplayFps(false);
        //setDisplayStatView(false);

        // Create debug text
        helpText = new BitmapText(guiFont);
        helpText.setLocalTranslation(0, settings.getHeight(), 0);
//        helpText.setText("Show\n" +
//                         "some texts for Help at guiNode");
        guiNode.attachChild(helpText);
        
        BitmapText sampleText = new BitmapText(guiFont);
        sampleText.setLocalTranslation(300, 300, 0);
        sampleText.setText("another sample\n" +
                         "shown at anoter location");
        guiNode.attachChild(sampleText);
        
        
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
        rootNode.attachChild(bigGirl);                
        
        /** You must add a light to make the model visible */
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);        
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.5f));
		rootNode.addLight(al);
	}
	
    public void simpleUpdate(float tpf) {
    	Vector3f cameraLoc = this.cam.getLocation();    	
    	helpText.setText("cam x: "+cameraLoc.getX() + " y: " + cameraLoc.getY() + " z: " + cameraLoc.getZ());
    }
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloGuiCameraPos();
				
		AppSettings s = new AppSettings(true);		
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);			
		app.setPauseOnLostFocus(false); // call this method in order not to pause when click a window for applet.	
		
		app.setDisplayStatView(false);
		app.setDisplayFps(false);
		
		app.start();
	}
}
