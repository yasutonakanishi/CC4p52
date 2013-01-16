package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class HelloMaterial extends SimpleApplication {

	@Override
	public void simpleInitApp() {		
		rootNode.attachChild(SkyFactory.createSky(assetManager, "myAssets/Textures/Sky/Bright/BrightSky.dds", false));

		/*
		 * detailed information is here,
		 * http://jmonkeyengine.org/wiki/doku.php/jme3:intermediate:how_to_use_materials
		 */
		
		/** Geometry consits of mesh data and material data **/
		Material whitemat = assetManager.loadMaterial("Common/Materials/WhiteColor.j3m");
        Material redmat = assetManager.loadMaterial("Common/Materials/RedColor.j3m");
        Sphere sphere =  new Sphere(16, 16, 0.5f);
        Geometry ball = new Geometry("sphere", sphere);
        ball.setMaterial(redmat);
        rootNode.attachChild(ball);
        ball.setLocalTranslation(-1.0f, 1.5f, 1.0f);
                		
		/** Material has color, texture, paramters for shiness etc.**/
		Material mat_tex = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md");        
        Texture tex1 = assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg");
        Texture tex2 = assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg");
        mat_tex.setTexture("ColorMap", tex2);
        
		/** Create a wall (Box with material and texture from test-data) */
		Box box = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);		
		Geometry wall = new Geometry("Box", box );
        wall.setMaterial(mat_tex);
        wall.setLocalTranslation(2.0f, 2.5f,2.0f);
        rootNode.attachChild(wall);
                		
        flyCam.setMoveSpeed(30f);		
	}

	public static void main(String[] args) {
		SimpleApplication app = new HelloMaterial();
		app.start();
	}
}
