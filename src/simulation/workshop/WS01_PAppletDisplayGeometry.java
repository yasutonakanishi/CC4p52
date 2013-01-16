/*
 * サンプル 01
 * Processingの画面をディスプレイで表示
 */

package simulation.workshop;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import test.p5.ColorBarsPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.util.SkyFactory;

// SimpleApplicationクラスを継承した、WS01_PAppletDisplayGeometryクラスを生成
public class WS01_PAppletDisplayGeometry extends SimpleApplication{

	PAppletDisplayGeometry display;

	@Override
	// アプリケーションの初期化
	public void simpleInitApp() {
		// 背景を表示
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));	
		// アプレットを新規に生成(ColorBarsPApplet)
		PApplet applet = new ColorBarsPApplet();
		// アプレットをディスプレイに適用
		display = new PAppletDisplayGeometry(
				"display",  	// ディスプレイの名前
				assetManager,
				4.0f, 4.0f, 	// シーン内でのサイズ
				applet,     	// 貼り付けるアプレット
				200, 200,   	// アプレットのサイズ
				false       	// コントロール画面を出すか否か
				);
		// ルートノードにディスプレイを追加
		rootNode.attachChild(display);
	}

	@Override
	// 状態の更新(アニメーション)
	public void simpleUpdate(float tpf) {
		// 立方体を回転
		display.rotate(0, 2*tpf, 0);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン関数
	public static void main(String[] args){
		// アプリケーションのスタート
		WS01_PAppletDisplayGeometry app = new WS01_PAppletDisplayGeometry();
		app.start();
	}
}