package simulation.workshop;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import simulation.p5.GravitySim;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

// SimpleApplicationクラスを継承した、WS02_CreateGalleryクラスを生成
public class WS02_DisplayInGallery extends SimpleApplication{
	
	PAppletDisplayGeometry display;
	
	@Override
	// アプリケーションの初期化
	public void simpleInitApp() {
		setupGallery();
		setupObjects();
	}
	
	// ギャラリーの環境を生成
	private void setupGallery() {
		// カメラの位置を設定
		cam.setLocation(new Vector3f(2.0f, 1.5f, -3.0f));
		cam.lookAt(new Vector3f(0, 1.8f, 0), Vector3f.UNIT_Y);
		flyCam.setDragToRotate(true);
		
		// 環境光
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.2f));
		this.rootNode.addLight(al);
		// 平行光
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(-0.3f, -0.5f, 0.7f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.6f));
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
		floorGeom.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(floorGeom);
		
		// 壁のマテリアル
		Material wall_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		wall_mat.setFloat("Shininess", 12);
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
		screenGeomL.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomL.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomL);
		
		Geometry screenGeomB = new Geometry("ScreenB", screen);
		screenGeomB.setLocalTranslation(new Vector3f(0f, 4f, -9f));
		screenGeomB.setMaterial(wall_mat);
		rootNode.attachChild(screenGeomB);
	}
	
	// オブジェクトを追加
	private void setupObjects(){
		PApplet applet = new GravitySim();
		// アプレットをディスプレイに適用
		display = new PAppletDisplayGeometry("display", assetManager, 2f, 1.5f, applet, 800, 600, true);
		this.setPauseOnLostFocus(false); // call this method in order not to pause in clicking on a window of the applet.

		// ルートノードにディスプレイを追加
		display.setLocalTranslation(0, 1.5f, 1.5f);
		rootNode.attachChild(display);
		
		// 女性を1人追加
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.setLocalTranslation(0, 0, 0);
		girl.setModelBound(girl.getWorldBound());
		rootNode.attachChild(girl);
	}
	
	@Override
	// 状態の更新(アニメーション)
	public void simpleUpdate(float tpf) {
	}
	
	@Override
	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
	
	// メイン関数
	public static void main(String[] args){
		// アプリケーションのスタート
		WS02_DisplayInGallery app = new WS02_DisplayInGallery();
		app.start();
	}
}
