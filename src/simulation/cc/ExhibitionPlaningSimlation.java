/*
 * 展覧会のシミュレーション
 * ギャラリー内に複数のディスプレイを配置 
 */

package simulation.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import simulation.p5.GravitySim;
import simulation.p5.ParticleSphere;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;


public class ExhibitionPlaningSimlation extends SimpleApplication {
	@Override
	// アプリケーションの初期化
	public void simpleInitApp() {
		setupEnvironment();
		setupObject();
	}

	public void setupEnvironment(){
		// カメラの位置を設定
		cam.setLocation(new Vector3f(4.0f, 1.5f, 22f));
		cam.lookAtDirection(new Vector3f(10.0f, 0.0f, 0.0f), Vector3f.UNIT_Y);
		flyCam.setDragToRotate(true);

		// 環境光
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.5f));
		this.rootNode.addLight(al);

		// ギャラリーに等間隔にポイントライトを配置
		PointLight light;
		for(int j = 0; j < 5; j++){
			for(int i = 0; i < 5; i++){
				light = new PointLight();
				light.setPosition(new Vector3f(i * 20.0f, 4.0f, j * 20.0f));
				light.setColor(ColorRGBA.White.mult(0.06f));
				rootNode.addLight(light);
			}
		}

		// 壁のマテリアル		
		Material wall_mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		wall_mat.setFloat("Shininess", 1);
		wall_mat.setBoolean("UseMaterialColors", true);
		wall_mat.setColor("Ambient",  ColorRGBA.White);
		wall_mat.setColor("Diffuse",  ColorRGBA.White);
		wall_mat.setColor("Specular", ColorRGBA.White);

		// 配置
		Spatial object = (Spatial) assetManager.loadModel("myAssets/Models/MOT_B2F_plain/MOT_B2F_plain.obj");
		object.setMaterial(wall_mat);
		rootNode.attachChild(object);
	}

	public void setupObject(){
		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display1 = new PAppletDisplayGeometry("display", assetManager, 3.2f, 2.4f, new ColorBarsPApplet(), 400, 300, false);
		display1.setLocalTranslation(10, 2.0f, 16f);
		rootNode.attachChild(display1);
		
		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display2 = new PAppletDisplayGeometry("display", assetManager, 3.2f, 2.4f, new ColorBarsPApplet(), 400, 300, false);
		display2.setLocalTranslation(15, 2.0f, 16f);
		rootNode.attachChild(display2);

		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display3 = new PAppletDisplayGeometry("display", assetManager, 3.2f, 2.4f,  new ColorBarsPApplet(), 400, 300, false);
		display3.setLocalTranslation(20, 2.0f, 16f);
		rootNode.attachChild(display3);

		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display4 = new PAppletDisplayGeometry("display", assetManager, 3.2f, 2.4f,  new ColorBarsPApplet(), 400, 300, false);
		display4.setLocalTranslation(25, 2.0f, 16f);
		rootNode.attachChild(display4);

		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display5 = new PAppletDisplayGeometry("display", assetManager, 8f, 3f,  new GravitySim(), 800, 600, false);
		display5.setLocalTranslation(15f, 2.0f, 30.5f);
		rootNode.attachChild(display5);

		// ディスプレイをギャラリーに配置
		PAppletDisplayGeometry display6 = new PAppletDisplayGeometry("display", assetManager, 4.8f, 3.6f,  new ParticleSphere(), 400, 300, true);
		display6.setLocalTranslation(31f, 2.0f, 22f);
		display6.rotate(0, FastMath.PI * 0.5f, 0);
		rootNode.attachChild(display6);

		// 20人の女性をランダムにシーンに追加
		for(int i = 0; i<20; i++){
			Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
			girl.rotate(0, (float)(Math.random()) * 180f, 0);
			girl.setLocalTranslation((float)(Math.random()) * 30f, 0.1f, (float)(Math.random()) * 12f + 17);
			this.rootNode.attachChild(girl);
		}
	}

	@Override
	// 状態の更新(アニメーション)
	public void simpleUpdate(float tpf) {

	}

	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン関数
	public static void main(String[] args){
		// アプリケーションのスタート
		ExhibitionPlaningSimlation app = new ExhibitionPlaningSimlation();
		app.setPauseOnLostFocus(false); 
		app.start();
	}
}
