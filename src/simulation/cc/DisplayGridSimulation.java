/*
 * ディスプレイをグリッド状に配置するサンプル
 *
 */

package simulation.cc;

import test.p5.ColorBarsPApplet;
import net.unitedfield.cc.PAppletDisplayGeometry;
import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class DisplayGridSimulation extends SimpleApplication  {
	PAppletDisplayGeometry[] player = new PAppletDisplayGeometry[80];

	@Override
	public void simpleInitApp() {
		// カメラの位置を設定
		cam.setLocation(new Vector3f(0, 1.5f, 12));
		flyCam.setDragToRotate(true);

		// 空を表示
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));

		// ライトを追加
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
		rootNode.addLight(dl);

		// 30人の女性をランダムにシーンに追加
		for(int i = 0; i<30; i++){
			Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
			girl.rotate(0, (float)(Math.random()) * 180f, 0);
			girl.setLocalTranslation((float)(Math.random()) * 20f - 10f, 0, (float)(Math.random()) * 20f - 10f);
			this.rootNode.attachChild(girl);
		}

		// ディスプレイを追加
		for(int i = 0; i<player.length; i++){
			this.player[i] = new PAppletDisplayGeometry("display"+i, assetManager, 0.8f, 0.8f, new ColorBarsPApplet(), 200, 200, false);
			rootNode.attachChild(player[i]);
			player[i].setLocalTranslation((i % 8) - 3f, 3f, (i / 8) - 3f);
			player[i].rotate((float) (Math.PI / 2.0), 0, 0);
		}
	}

	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン
	public static void main(String[] args){
		SimpleApplication app = new DisplayGridSimulation();
		app.start();
	}
}