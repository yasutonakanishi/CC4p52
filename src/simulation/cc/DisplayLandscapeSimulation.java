/*
 * 地表に複数のディスプレイを配置
 *
 */

package simulation.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import simulation.p5.OrangeWavePApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public class DisplayLandscapeSimulation extends SimpleApplication  {

	// ディスプレイを追加する座標記録した配列
	PAppletDisplayGeometry[] player = new PAppletDisplayGeometry[50];
	float[] posX = {-30.4f, -26.8f, -27.9f, -29.2f, -25.7f, -22.2f, -19.8f, -15.1f, -23.1f, -15.700001f, -9.200001f, -9.5f, -10.9f, -2.7000008f, 2.0f, -2.0f, 2.5999985f, 10.599998f, 6.0999985f, 1.2999992f, 8.299999f, 12.900002f, 16.7f, 16.900002f, 23.8f};
	float[] posZ = {-33.3f, -32.3f, -28.9f, -25.0f, -25.0f, -20.7f, -20.7f, -15.4f, -12.700001f, -12.700001f, -11.9f, -5.799999f, 1.2999992f, 1.0999985f, 2.0999985f, 8.299999f, 11.400002f, 10.099998f, 17.099998f, 16.599998f, 22.7f, 22.099998f, 21.7f, 24.599998f, 24.699997f};


	@Override
	// 初期化
	public void simpleInitApp() {
		setupScene();
		setupDisplays();
	}

	// シーンを定義
	private void setupScene(){
		// カメラの位置、向き、移動速度を設定
		cam.setLocation(new Vector3f(0.0f, 1.6f, 24.0f));
		cam.lookAt(new Vector3f(0f, 1.6f, 0f), Vector3f.UNIT_Y);
		flyCam.setMoveSpeed(5);
		flyCam.setDragToRotate(true);

		// 空を追加
		Texture west = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_west.jpg");
		Texture east = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_east.jpg");
		Texture north = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_north.jpg");
		Texture south = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_south.jpg");
		Texture up = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_up.jpg");
		Texture down = assetManager.loadTexture("Textures/Sky/Lagoon/lagoon_down.jpg");
		Spatial sky = SkyFactory.createSky(assetManager, west, east, north, south, up, down);
		rootNode.attachChild(sky);

		// 床面を生成
		Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		floor_mat.setColor("Color", ColorRGBA.Gray); 
		Box floor = new Box(Vector3f.ZERO, 1500f, 0.01f, 1500f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(floor_mat);
		rootNode.attachChild(floorGeom);

		// 照明
		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White.mult(3.0f));
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		rootNode.addLight(sun);

		// 女性を追加
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.rotate(0, (float)(Math.PI), 0);
		girl.setLocalTranslation(0f, 0f, 20f);
		this.rootNode.attachChild(girl);
	}

	// ディスプレイを追加
	private void setupDisplays() {
		// 配列で定義した場所に、ディスプレイを追加していく
		for(int i = 0; i<posX.length; i++){
			this.player[i] = new PAppletDisplayGeometry("display"+i, assetManager, 0.3f, 1.5f,  
					new OrangeWavePApplet(), 20, 100, false);
			rootNode.attachChild(player[i]);
			player[i].setLocalTranslation(posX[i], 1.5f, posZ[i]);
		}
	}

	@Override
	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン
	public static void main(String[] args){
		SimpleApplication app = new DisplayLandscapeSimulation();
		app.start(); // start the game
	}
}