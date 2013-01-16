package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.util.SkyFactory;

public class HelloSky extends SimpleApplication {

    @Override
	public void simpleInitApp() {
		// sky
		rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/BrightSky.dds", false));
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/FullskiesBlueClear03.dds", false));	
		//rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Night/Night_dxt1.dds", true)); // in using cube-map, last parameter is true
		
		/*
		Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg");
        Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg");
        Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg");
        Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg");
        Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg");
        Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");
        Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
        rootNode.attachChild(sky);
        */
	}
	
	public static void main(String[] args) {		
		SimpleApplication app = new HelloSky();
		app.start();
	}
}
