/*
 * カメラ入力をスリットスキャン
 *
 */

package simulation.cc;

import net.unitedfield.cc.CaptureCameraNode;
import net.unitedfield.cc.PAppletDisplayGeometry;
import simulation.p5.SlitScanPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class SlitScanSimulation extends SimpleApplication {
	SlitScanPApplet captureApplet;
	CaptureCameraNode captureCameraNode;
	float phase;
	int NUM = 40;
	Spatial[] 	girl = new Spatial[NUM];
	Vector3f[]	girlPos = new Vector3f[NUM];
	float[] 	girlSpeed = new float[NUM];
	Geometry[] 	wallGeom = new Geometry[6];

	@Override
    // アプリ初期設定
	public void simpleInitApp() {
        setupEnvironments();
		setupCapture();
		setupVirtualCameraDisplay();
	}
    
	private void setupEnvironments() {
		// カメラ設定
        cam.setFrustumPerspective(80f, 1f, 1f, 1000f);
		cam.setLocation(new Vector3f(0, 1.5f, 10));
		//cam.lookAt(new Vector3f(0, 1.6f, 0), Vector3f.UNIT_Y);
		flyCam.setDragToRotate(true);
        
		// ライティング
		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White.mult(3.0f));
		dl.setDirection(Vector3f.UNIT_XYZ.negate());
		rootNode.addLight(dl);
		
        // マテリアル設定
		Material textureMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		textureMat.setTexture("ColorMap", assetManager.loadTexture("myAssets/Textures/woodFloor.jpg"));
		Material whitemat = assetManager.loadMaterial("Common/Materials/WhiteColor.j3m");
        
		// 床面
		Box floor = new Box(Vector3f.ZERO, 20.0f, 0.01f, 20.0f);
		Geometry floorGeom = new Geometry("Floor", floor);
		floorGeom.setMaterial(textureMat);
		floorGeom.setLocalTranslation(0, 0, 0);
		rootNode.attachChild(floorGeom);
		
		// 壁面
		wallGeom[0] = new Geometry("Wall1", new Box(Vector3f.ZERO, 20f, 4f, 0.1f));
		wallGeom[1] = new Geometry("Wall2", new Box(Vector3f.ZERO, 20f, 4f, 0.1f));
		wallGeom[2] = new Geometry("Wall3", new Box(Vector3f.ZERO, 20f, 4f, 0.1f));
		wallGeom[0].setMaterial(whitemat);
		wallGeom[1].setMaterial(whitemat);
		wallGeom[2].setMaterial(whitemat);
		wallGeom[0].setLocalTranslation(0, 4, -2);
		wallGeom[1].setLocalTranslation(-20, 4, 0);
		wallGeom[2].setLocalTranslation(20, 4, 0);
		wallGeom[1].rotate(0, (float)(Math.PI/2f), 0);
		wallGeom[2].rotate(0, (float)(Math.PI/2f), 0);
		rootNode.attachChild(wallGeom[0]);
		rootNode.attachChild(wallGeom[1]);
		rootNode.attachChild(wallGeom[2]);
        
        // 女性をランダムに追加
        for(int i = 0; i < girl.length; i++) {
			girl[i] = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
			girlPos[i] = new Vector3f((float)(Math.random() * 16.0f - 8f), 0, (float)(Math.random() * 8f));
            // 移動スピードをランダムに
			girlSpeed[i] = (float)(Math.random() * -0.02f) + 0.01f;
			if(girlSpeed[i] < 0){
				girl[i].rotate(0, (float)(-Math.PI/2.0f), 0);
			} else {
				girl[i].rotate(0, (float)(Math.PI/2.0f), 0);
			}
			girl[i].setLocalTranslation(girlPos[i]);
			this.rootNode.attachChild(girl[i]);
		}
	}

    // カメラからのキャプチャー設定
	private void setupCapture(){
		captureApplet = new SlitScanPApplet();
		captureCameraNode = new CaptureCameraNode("", 400, 300, assetManager, renderManager, renderer, rootNode);
		captureCameraNode.setLocalTranslation(0, 1.5f, 0);
		captureApplet.setCapture(captureCameraNode.getCapture());
	}

    // ディスプレイを配置
	private void setupVirtualCameraDisplay(){      
		PAppletDisplayGeometry display = new PAppletDisplayGeometry("display", assetManager, 20f, 4f, captureApplet, 400,300,true);		
		rootNode.attachChild(display);
		display.setLocalTranslation(new Vector3f(0,4f,-1.85f));
	}

	@Override
    // 更新
	public void simpleUpdate(float tpf) {
		for(int i = 0; i < girl.length; i++) {
			girlPos[i].x += girlSpeed[i];
			if(girlPos[i].x < -8.0f){
				girlPos[i].x = 8.0f;
			}
			girl[i].setLocalTranslation(girlPos[i]);
		}
		phase += 0.0001f;
	}
    
    @Override
    // 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}
    
    // メイン
	public static void main(String[] args){
		SimpleApplication app = new SlitScanSimulation();
        app.setPauseOnLostFocus(false);
		app.start();
	}
}
