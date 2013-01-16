/*
 * 東京ビッグサイトにプロジェクションマッピング
 *
 */

package simulation.cc;
import net.unitedfield.cc.PAppletProjectorShadowNode;
import processing.core.PApplet;
import simulation.p5.SimpleGridPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class ShadowProjection2BigsightSimulation extends SimpleApplication {
	int NUM = 100;
	Spatial[] girl = new Spatial[NUM];
	PApplet applet;
	PAppletProjectorShadowNode ppn;

	@Override
    // 初期設定
	public void simpleInitApp() {
		setupEnvironment();
		setupProjector();
	}

    // 環境の設定
	private void setupEnvironment(){
		// カメラ設定
		cam.setLocation(new Vector3f(0, 10, 150));
		cam.lookAt(new Vector3f(10, 60, 0), Vector3f.UNIT_Y);
		flyCam.setMoveSpeed(20);
		flyCam.setDragToRotate(true);
        
		// ライティング
		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White.mult(4.0f));
		sun.setDirection(new Vector3f(0f, 1.0f, -0.75f));
		rootNode.addLight(sun);
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(8.0f));
		rootNode.addLight(al);

		// 空を設定
		Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg");
		Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg");
		Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg");
		Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg");
		Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg");
		Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");
		Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
		rootNode.attachChild(sky);

        // マテリアル
		Material textureMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		textureMat.setTexture("ColorMap", assetManager.loadTexture("myAssets/Textures/metalTexture.jpg"));
		
        // 地面を設定
		Box floor = new Box(Vector3f.ZERO, 1500f, 0.01f, 1500f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(textureMat);
		rootNode.attachChild(floorGeom);

		// ランダムに100人の女性を配置
		for(int i = 0; i < girl.length; i++) {
			girl[i] = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
			Vector3f girlPos = new Vector3f((float)(Math.random() * 160f - 80f), 0, (float)(Math.random() * 160f - 80f));
			girl[i].rotate(0, (float)(Math.random() * Math.PI/2.0f), 0);
			girl[i].setLocalTranslation(girlPos);
			this.rootNode.attachChild(girl[i]);
		}
        
		// ビッグサイトを配置
		Spatial object = (Spatial) assetManager.loadModel("myAssets/Models/TokyoBigSite/TokyoBigSite.obj");
		rootNode.attachChild(object);
        
        // プロジェクタの映像を影を表示
		object.setShadowMode(ShadowMode.CastAndReceive);
		floorGeom.setShadowMode(ShadowMode.CastAndReceive);
	}

    // プロジェクタ設定
	private void setupProjector(){
		applet = new SimpleGridPApplet();
		ppn = new PAppletProjectorShadowNode("Projector", assetManager, viewPort, 45, 200, 1024, 768, applet, 400,300, false);
		rootNode.attachChild(ppn);
		rootNode.attachChild(ppn.getFrustmModel());
		ppn.setLocalTranslation(new Vector3f(0, 4f, 200));
		//ppn.rotate(FastMath.PI * 0.25f, FastMath.PI * -0.25f, 0);
		ppn.lookAt(new Vector3f(0, 120f, 0f), Vector3f.UNIT_Y);
	}

    @Override
    // 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
    // メイン
	public static void main(String[] args){
		SimpleApplication app = new ShadowProjection2BigsightSimulation();
		app.setPauseOnLostFocus(false); 
		app.start();
	}
}