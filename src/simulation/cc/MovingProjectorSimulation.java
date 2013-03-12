/*
 * ギャラリー内に配置された球を移動しながら照らすプロジェクター
 *
 */

package simulation.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

public class MovingProjectorSimulation extends SimpleApplication  {
	PAppletProjectorShadowNode ppn;
	float phase;

	@Override
	// 初期設定
	public void simpleInitApp() {
		setupEnvironment();
		setupProjector();
	}

	// 環境設定
	public void setupEnvironment(){
		// カメラ
		cam.setLocation(new Vector3f(0, 1.7f, 6));
		flyCam.setDragToRotate(true);

		// ライティング
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);

		// 床面
		Material textureMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		textureMat.setTexture("ColorMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));
		Box floor = new Box(Vector3f.ZERO, 5.0f, 0.01f, 5.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(textureMat);
		rootNode.attachChild(floorGeom);

		// 球を配置
		Material whitemat = assetManager.loadMaterial("Common/Materials/WhiteColor.j3m");
		Sphere sp = new Sphere(64, 64, 1.0f);
		Geometry sphereGeom = new Geometry("Sphere", sp);
		sphereGeom.updateModelBound();
		sphereGeom.setMaterial(whitemat);
		sphereGeom.rotate((float)(Math.sin(phase)) * 0.3f, 0, (float)(Math.sin(phase)) * 1.2f);
		sphereGeom.setLocalTranslation(0, 1.3f, 0);
		rootNode.attachChild(sphereGeom);

		// 女性
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.rotate(0, (float)(Math.PI)*1.3f, 0);
		girl.setLocalTranslation(1f, 0, 1f);
		this.rootNode.attachChild(girl);

		// プロジェクターの映像と陰を設定
		girl.setShadowMode(ShadowMode.CastAndReceive);
		floorGeom.setShadowMode(ShadowMode.CastAndReceive);
		sphereGeom.setShadowMode(ShadowMode.CastAndReceive);
	}

	// プロジェクタ設定
	public void setupProjector(){
		ppn = new PAppletProjectorShadowNode("Projector0", assetManager, viewPort, 1024, 1024, new ColorBarsPApplet(), 200, 200, true);
		rootNode.attachChild(ppn);
		rootNode.attachChild(ppn.getFrustmModel());   
		ppn.rotate((float)(Math.PI)*0.51f, 0, 0);
		phase = 0.0f;
		
	}

	@Override
	// 更新
	public void simpleUpdate(float tpf) {
		// プロジェクターを移動
		ppn.setLocalTranslation((float)(Math.sin(phase)) * 1.5f, 6f, (float)(Math.cos(phase) * 2f) * 1.5f);
		ppn.rotate(0, 0, tpf/20);
		phase += 0.001f;
		ppn.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
	}

	@Override
	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン
	public static void main(String[] args){
		SimpleApplication app = new MovingProjectorSimulation();
		app.start();
	}
}