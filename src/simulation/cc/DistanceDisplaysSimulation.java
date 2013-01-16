package simulation.cc;

import net.unitedfield.cc.DistanceSensor;
import net.unitedfield.cc.DistanceSensorFirmata;
import net.unitedfield.cc.DistanceSensorNode;
import net.unitedfield.cc.PAppletDisplayGeometry;
import net.unitedfield.cc.util.SpatialInspector;
import test.p5.TreeWithDistanceSensor;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
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

public class DistanceDisplaysSimulation extends SimpleApplication {
	Node senseTarget;
	Spatial girl;
	
	public void simpleInitApp() {
		cam.setLocation(new Vector3f(-1f, 1.5f, -3f));
		cam.lookAt(new Vector3f(0, 1.8f, 5f), Vector3f.UNIT_Y);
		flyCam.setDragToRotate(true);

		senseTarget = new Node();
		setupEnvironment();		
		setupDistanceDisplays();
		setupGirl();		
	}
		
	private void setupDistanceDisplays() {
		int NUM = 3;
		DistanceSensor			sensors[]	= new DistanceSensor[NUM];
		TreeWithDistanceSensor	applets[]	= new TreeWithDistanceSensor[NUM];
		PAppletDisplayGeometry	displays[]	= new PAppletDisplayGeometry[NUM];
		
		SpatialInspector spatialInspector = SpatialInspector.getInstance();
		for(int i=0; i<NUM; i++){			
			if(i<NUM-1){
			//if(false){
				DistanceSensorNode sensorVirtual = new DistanceSensorNode("sensor"+i, 5f, assetManager, senseTarget);
				sensorVirtual.setLocalTranslation(new Vector3f(4-4*i, 1.5f, 9.4f));
				sensorVirtual.rotate(0, FastMath.PI, 0);				
				rootNode.attachChild(sensorVirtual);
				sensorVirtual.addControl(spatialInspector);
				sensors[i] = sensorVirtual;
			}else{
				DistanceSensorFirmata sensorReal = new DistanceSensorFirmata(0);
				sensorReal.setup();
				sensors[i] = sensorReal;
			}
			applets[i] = new TreeWithDistanceSensor();
			applets[i].setDistanceSensor(sensors[i]);
			displays[i] = new PAppletDisplayGeometry("display"+i,assetManager,4,3,applets[i],640,360,false);
			displays[i].setLocalTranslation(new Vector3f(4-4*i, 1.5f, 9.4f));
			rootNode.attachChild(displays[i]);
			displays[i].addControl(spatialInspector);
		}		
		spatialInspector.show();
		this.setPauseOnLostFocus(false);
	}
	
	// 環境を設定
	private void setupEnvironment() {
		// ライティング
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(-0.12f, -0.3729129f, 0.74847335f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.25f));
		this.rootNode.addLight(sun);

		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.1f));
		this.rootNode.addLight(al);

		SpotLight spot = new SpotLight();
		spot.setSpotRange(500);
		spot.setColor(ColorRGBA.White.multLocal(2.0f));
		spot.setSpotOuterAngle(30 * FastMath.DEG_TO_RAD);
		spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
		spot.setPosition(new Vector3f(0, 2.2f, -2f));
		spot.setDirection(new Vector3f(0, 0, 1));
		rootNode.addLight(spot);

		// 床面を生成
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
		this.rootNode.attachChild(floorGeom);
		
		// 壁を生成
		Material screen_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md"); 
		screen_mat.setFloat("Shininess", 12);
		screen_mat.setBoolean("UseMaterialColors", true);
		screen_mat.setColor("Ambient",  ColorRGBA.White);
		screen_mat.setColor("Diffuse",  ColorRGBA.White);
		screen_mat.setColor("Specular", ColorRGBA.White);
			
		Box screen = new Box(Vector3f.ZERO, 10f, 4f, 0.01f);
		Geometry screenGeomF = new Geometry("ScreenF", screen);
		screenGeomF.setLocalTranslation(new Vector3f(0f, 4f, 9.5f));
		screenGeomF.setMaterial(screen_mat);
		screenGeomF.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(screenGeomF);

		Geometry screenGeomR = new Geometry("ScreenR", screen);
		screenGeomR.setLocalTranslation(new Vector3f(9.5f, 4f, 0f));
		screenGeomR.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomR.setMaterial(screen_mat);
		screenGeomR.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(screenGeomR);

		Geometry screenGeomL = new Geometry("ScreenL", screen);
		screenGeomL.setLocalTranslation(new Vector3f(-9.5f, 4f, 0f));
		screenGeomL.rotate(0, (float)(Math.PI)*0.5f, 0);
		screenGeomL.setMaterial(screen_mat);
		screenGeomL.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(screenGeomL);
	}

	private	void setupGirl(){
		// 女性を配置して、センサーのターゲットに設定
		girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.setShadowMode(ShadowMode.CastAndReceive);
		girl.setModelBound(girl.getWorldBound());
		senseTarget.attachChild(girl);
		rootNode.attachChild(senseTarget);
		
		// キー入力を設定
		inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("MoveBack", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addListener(analogListener, new String[]{
				"MoveRight", "MoveLeft", "MoveUp", "MoveDown", "MoveForward", "MoveBack"
		});

		// 画面にキー設定を表示
		BitmapText helpText = new BitmapText(guiFont);
		helpText.setLocalTranslation(0, settings.getHeight(), 0);
		helpText.setText("J:Right, K:Left, I:Forward, M:Back");
		guiNode.attachChild(helpText);
	}
	
	// イベントリスナー
	private AnalogListener analogListener = new AnalogListener() {
		// キー入力に対応して、センサーのターゲット(女性)の位置を移動
		public void onAnalog(String name, float value, float tpf) {
			if (name.equals("MoveRight")) {
				girl.move(2 * tpf, 0, 0);
			}
			if (name.equals("MoveLeft")) {
				girl.move(-2 * tpf, 0, 0);
			}
			if (name.equals("MoveUp")) {
				girl.move(0, 2 * tpf, 0);
			}
			if (name.equals("MoveDown")) {
				girl.move(0, -2 * tpf, 0);
			}            
			if (name.equals("MoveForward")) {
				girl.move(0, 0, 2 * tpf);
			}            
			if (name.equals("MoveBack")) {
				girl.move(0, 0, -2 * tpf);
			}            
		}
	};

    public void destroy() {
		super.destroy();
		// we should terminate the thread of PApplet.
		System.exit(0);
	}
		
	public static void main(String[] args) {
		SimpleApplication app = new DistanceDisplaysSimulation();
		app.setPauseOnLostFocus(false);
		app.start();
	}
}
