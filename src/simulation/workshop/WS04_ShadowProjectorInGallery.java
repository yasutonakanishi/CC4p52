package simulation.workshop;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import net.unitedfield.cc.util.SpatialInspector;
import processing.core.PApplet;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

// SimpleApplicationクラスを継承した、WS02_CreateGalleryクラスを生成
public class WS04_ShadowProjectorInGallery extends SimpleApplication{

	PAppletProjectorShadowNode ppn;
	
	@Override
	// アプリケーションの初期化
	public void simpleInitApp() {
		setupGallery();
		setupObjects();
		setupSpatialInspector();
	}

	// ギャラリーの環境を生成
	private void setupGallery() {
		// カメラ
		cam.setLocation(new Vector3f(0, 1.5f, -4f));
		cam.lookAt(new Vector3f(0, 1.8f, 5f), Vector3f.UNIT_Y);

		// 照明
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.6f));
		this.rootNode.addLight(al);
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(0, 0.3f, 0.8f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.5f));
		//this.rootNode.addLight(sun);

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
		floorGeom.setShadowMode(ShadowMode.Receive);
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
		screenGeomF.setLocalTranslation(new Vector3f(0f, 4f, 9.5f));
		screenGeomF.setMaterial(wall_mat);
		screenGeomF.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(screenGeomF);
		Geometry screenGeomR = new Geometry("ScreenR", screen);
		screenGeomR.setLocalTranslation(new Vector3f(9.5f, 4f, 0f));
		screenGeomR.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomR.setMaterial(wall_mat);
		screenGeomR.setShadowMode(ShadowMode.CastAndReceive);
		rootNode.attachChild(screenGeomR);
		Geometry screenGeomL = new Geometry("ScreenL", screen);
		screenGeomL.setLocalTranslation(new Vector3f(-9.5f, 4f, 0f));
		screenGeomL.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomL.setMaterial(wall_mat);
		screenGeomL.setShadowMode(ShadowMode.CastAndReceive);
		rootNode.attachChild(screenGeomL);
	}
    
    // オブジェクトを追加
	private void setupObjects(){		
		PApplet applet = new ColorBarsPApplet();
		// アプレットをディスプレイに適用
	    ppn = new PAppletProjectorShadowNode("Projector0",assetManager,viewPort,1024,768,applet,200,200,false); 
		ppn.setLocalTranslation(0, 5.0f, -1f);
	    Quaternion rot = new Quaternion();
	    ppn.rotate(rot.fromAngleAxis(FastMath.PI/2, Vector3f.UNIT_X));
	    rootNode.attachChild(ppn);
		
		// 女性を1人追加
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.setLocalTranslation(0, 0, 0);
		girl.setModelBound(girl.getWorldBound());
		girl.setShadowMode(ShadowMode.CastAndReceive);
		rootNode.attachChild(girl);
	}
    // プロジェクターの位置や方向を調整するGUIを追加
	private void setupSpatialInspector() {
        // GUIを生成
		SpatialInspector spatialInspector = SpatialInspector.getInstance();
        // プロジェクタに適用
		ppn.addControl(spatialInspector);
        // マウスの動きと画面の動きを連動させない
		this.setPauseOnLostFocus(false);
        // GUI表示
		spatialInspector.show();
	}

	@Override
	// 状態の更新(アニメーション)
	public void simpleUpdate(float tpf) {				
		// Quaternion q = new Quaternion();
	    // q.fromAngles(0, 0, tpf/10);// x is pitch, y is yaw, z is roll.
		// ppn.rotate(q);
	}

	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン関数
	public static void main(String[] args){
		// アプリケーションのスタート
		WS04_ShadowProjectorInGallery app = new WS04_ShadowProjectorInGallery();
		app.setPauseOnLostFocus(false); 
		app.start();
	}
}