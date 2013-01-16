/*
 * キネティックな物体を配置して、プロジェクション
 *
 */

package simulation.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer;

public class KineticProjectionSimulation extends SimpleApplication {

	Geometry [] geoms = new Geometry[100];
	Node geomsNode;
	PointLight pl;
	private BasicShadowRenderer bsr;
	PssmShadowRenderer pssmRenderer;

	@Override
	// アプリ初期設定
	public void simpleInitApp()	{

		setupProjector();
		setupEnvironment();
		setupObjects();

		bsr = new BasicShadowRenderer(assetManager, 4096);
		bsr.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal()); // light direction
		viewPort.addProcessor(bsr);
	}

	private void setupEnvironment() {
		// カメラ設定
		cam.setLocation(new Vector3f(6f, 2.2f, -8f));
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

		// ライティング
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(-0.12f, -0.3729129f, 0.74847335f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.5f));
		this.rootNode.addLight(sun);

		SpotLight spot = new SpotLight(); 
		spot = new SpotLight(); 
		spot.setSpotRange(2000); 
		spot.setColor(ColorRGBA.White.clone().multLocal(4));
		spot.setSpotOuterAngle(55 * FastMath.DEG_TO_RAD); 
		spot.setSpotInnerAngle(20 * FastMath.DEG_TO_RAD); 
		spot.setPosition(new Vector3f(0f, 5f, 0f));
		rootNode.addLight(spot);

		// 床面を設定
		Material floor_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"); 
		floor_mat.setTexture("DiffuseMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));
		floor_mat.setFloat("Shininess", 12);
		floor_mat.setBoolean("UseMaterialColors", true);
		floor_mat.setColor("Ambient",  ColorRGBA.White);
		floor_mat.setColor("Diffuse",  ColorRGBA.White);
		floor_mat.setColor("Specular", ColorRGBA.White);

		Box floor = new Box(Vector3f.ZERO, 10.0f, 0.01f, 10.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(floor_mat);
		floorGeom.setLocalTranslation(0, 0, 0);
		floorGeom.setShadowMode(ShadowMode.CastAndReceive);
		rootNode.attachChild(floorGeom);

		// 女性を配置
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.rotate(0, (float)(Math.PI)*-0.3f, 0);
		girl.setLocalTranslation(5f, 0, -6f);
		girl.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(girl);
	}

	// プロジェクターを配置
	private void setupProjector() {
		PAppletProjectorShadowNode ppn = new PAppletProjectorShadowNode("projector", assetManager,viewPort,1024,768, new ColorBarsPApplet(), 200,200,false);
		ppn.rotate((float)(Math.PI)*0.2f, 0, 0);
		rootNode.attachChild(ppn);
		rootNode.attachChild(ppn.getFrustmModel());   
		ppn.setLocalTranslation(new Vector3f(0, 4f, -12f));
	}

	// キネティックな物体を追加
	private void setupObjects() {
		// マテリアル設定
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setFloat("Shininess", 24);
		mat.setBoolean("UseMaterialColors", true);
		mat.setBoolean("VertexLighting", true);
		mat.setBoolean("HighQuality", true);
		mat.setColor("Ambient",  ColorRGBA.DarkGray);
		mat.setColor("Diffuse",  ColorRGBA.LightGray);
		mat.setColor("Specular", ColorRGBA.LightGray);

		// ノードを設定
		geomsNode = new Node("DisplayNode");
		geomsNode.setLocalTranslation(-4.5f, 0f, -4.5f);
		rootNode.attachChild(geomsNode);

		// ボックスをマトリクス状に配置
		for(int j = 0; j < 10; j++) {
			for(int i = 0; i < 10; i++) {
				Box b = new Box(Vector3f.ZERO, 0.4f, 0.51f, 0.4f);
				this.geoms[i] = new Geometry("Box", b);
				this.geomsNode.attachChild(this.geoms[i]);
				this.geoms[i].move(1f * i, 1f, 1f * j);
				this.geoms[i].setMaterial(mat);
				this.geoms[i].setShadowMode(ShadowMode.CastAndReceive);
				// コントロールを追加、sin関数の動きを追加
				this.geoms[i].addControl(new ControlSinMotion((float)(i * Math.PI / 3f + j * Math.PI / 4f)));
			}
		}
	}

	@Override
	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン
	public static void main(String[] args) {		
		SimpleApplication app = new KineticProjectionSimulation();				
		app.start();
	}
}
