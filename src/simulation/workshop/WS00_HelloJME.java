/*
 * サンプル 00
 * 物体を表示
 */

package simulation.workshop;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.util.SkyFactory;

// SimpleApplicationクラスを継承した、WS00_HelloJMEクラスを生成
public class WS00_HelloJME extends SimpleApplication{

	Geometry geom;

	@Override
	// アプリケーションの初期化
	public void simpleInitApp() {
		// 背景を表示
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));

		// 物体のマテリアル(材質)を設定
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

		// ライトを設定
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(1,0,-2).normalizeLocal());
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

		// 立方体を生成して表示
		Box b = new Box(Vector3f.ZERO, 1, 1, 1);    // 立方体の形状(Shape)を生成
		geom = new Geometry("Box", b);              // 立方体の形状(Shape)からジオメトリ(Geometry)を生成
		geom.setMaterial(mat);                      // 立方体にマテリアルを適用
		rootNode.attachChild(geom);                 // シーンのルートに立方体を追加
	}

	@Override
	// 状態の更新(アニメーション)
	public void simpleUpdate(float tpf) {
		// 立方体を回転
		geom.rotate(1*tpf, 2*tpf, 0);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン関数
	public static void main(String[] args){
		// アプリケーションのスタート
		WS00_HelloJME app = new WS00_HelloJME();
		app.start();
	}
}