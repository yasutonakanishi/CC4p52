package test.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import processing.core.PApplet;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;



public class HelloMovingProjector extends SimpleApplication {

	PAppletProjectorShadowNode ppg;

	@Override
	public void simpleInitApp() {
		//cam
		cam.setLocation(Vector3f.UNIT_XYZ.mult(20.0f)); // camera moves to 10, 10, 10 
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);     // and looks at 0,0,0.   
		flyCam.setRotationSpeed(10);
        flyCam.setDragToRotate(true);
		
		setupGallery();  
		
        PApplet applet = new ColorBarsPApplet();
        ppg = new PAppletProjectorShadowNode("Projector",assetManager,viewPort,1024,768,applet,400,300,false);        
        rootNode.attachChild(ppg);        
        rootNode.attachChild(ppg.getFrustmModel()); 
		
	    ppg.setLocalTranslation(new Vector3f(0,10,0));		 // move the projector,
	    ppg.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_X);  // and make it to look at where you want.	        
	}

	// ギャラリーの環境を生成
	private void setupGallery() {
		// 環境光
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.1f));
		this.rootNode.addLight(al);
		// 平行光
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(-0.3f, -0.5f, 0.7f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.5f));
		this.rootNode.addLight(sun);

		// 空間内に等間隔にポイントライトを配置
		PointLight light;
		for(int j = -2; j < 2; j++){
			for(int i = -2; i < 2; i++){
				light = new PointLight();
				light.setPosition(new Vector3f(i * 10.0f, 4.0f, j * 10.0f));
				light.setColor(ColorRGBA.White.mult(0.04f));
				rootNode.addLight(light);
			}
		}

		// 床のマテリアル
		Material floor_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		floor_mat.setTexture("DiffuseMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));
		floor_mat.setFloat("Shininess", 12);
		floor_mat.setBoolean("UseMaterialColors", true);
		floor_mat.setColor("Ambient",  ColorRGBA.White);
		floor_mat.setColor("Diffuse",  ColorRGBA.White);
		floor_mat.setColor("Specular", ColorRGBA.White);

		// 床の形状
		Box floor = new Box(Vector3f.ZERO, 10.0f, 0.01f, 10.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(floor_mat);
		floorGeom.setLocalTranslation(0, 0, 0);
		this.rootNode.attachChild(floorGeom);

		// 壁のマテリアル
		Material wall_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		//wall_mat.setFloat("Shininess", 12);
		wall_mat.setBoolean("UseMaterialColors", true);
		wall_mat.setColor("Ambient",  ColorRGBA.White);
		wall_mat.setColor("Diffuse",  ColorRGBA.White);
		wall_mat.setColor("Specular", ColorRGBA.White);

		// 壁の形状
		Box screen = new Box(Vector3f.ZERO, 10f, 4f, 0.01f);
		Geometry screenGeomF = new Geometry("ScreenF", screen);
		screenGeomF.setLocalTranslation(new Vector3f(0f, 4f, 9f));
		screenGeomF.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomF);

		Geometry screenGeomR = new Geometry("ScreenR", screen);
		screenGeomR.setLocalTranslation(new Vector3f(9f, 4f, 0f));
		screenGeomR.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomR.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomR);

		Geometry screenGeomL = new Geometry("ScreenL", screen);
		screenGeomL.setLocalTranslation(new Vector3f(-9f, 4f, 0f));
		screenGeomL.rotate(0, (float)(Math.PI)*-0.5f, 0);
		screenGeomL.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomL);

		Geometry screenGeomB = new Geometry("ScreenB", screen);
		screenGeomB.setLocalTranslation(new Vector3f(0f, 4f, -9f));
		screenGeomB.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomB);
		

        //object 
        Spatial tree = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
        tree.setQueueBucket(Bucket.Transparent);
        rootNode.attachChild(tree);
				
		//projector is a kind of Shadow, and following processes are necessary for Shadow Rendering. 		
		floorGeom.setShadowMode(ShadowMode.Receive);
		screenGeomF.setShadowMode(ShadowMode.Receive);
		screenGeomL.setShadowMode(ShadowMode.Receive);
		screenGeomR.setShadowMode(ShadowMode.Receive);
		screenGeomB.setShadowMode(ShadowMode.Receive);
		tree.setShadowMode(ShadowMode.CastAndReceive); // tree makes and receives shadow
	}

	float phase;
	@Override
	public void simpleUpdate(float tpf) {
		ppg.setLocalTranslation((float)(Math.sin(phase)) * 5f, 10f, (float)(Math.cos(phase)) * 5f);
		ppg.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_X);
		phase += 0.005f;
	}
	
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		SimpleApplication app = new HelloMovingProjector();
		app.start();
	}
}
