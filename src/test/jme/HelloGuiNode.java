package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.system.AppSettings;

public class HelloGuiNode extends SimpleApplication {	
	@Override
	public void simpleInitApp() {
		// you can select showing fps or statview is true or false.
        //setDisplayFps(false);
        //setDisplayStatView(false);

        // Create debug text
        BitmapText helpText = new BitmapText(guiFont);
        helpText.setLocalTranslation(0, settings.getHeight(), 0);
        helpText.setText("Show\n" +
                         "some texts for Help at guiNode");
        guiNode.attachChild(helpText);
        
        BitmapText sampleText = new BitmapText(guiFont);
        sampleText.setLocalTranslation(300, 300, 0);
        sampleText.setText("another sample\n" +
                         "shown at anoter location");
        guiNode.attachChild(sampleText);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloGuiNode();
				
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
