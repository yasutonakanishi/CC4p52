/*
 *  画面内でクリックした地表に、ディスプレイを追加する
 *  
 */

package simulation.cc;

import net.unitedfield.cc.PAppletDisplayGeometry;
import processing.core.PApplet;
import simulation.p5.OrangeWavePApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

public class DisplayLandscapeNew extends SimpleApplication {
	TerrainQuad terrain;
	Material mat_terrain;
	Node shootables;
	int displayNum = 0;

	@Override
	// 初期化
	public void simpleInitApp() {
		// クリック可能なノードを生成してルートに追加
		shootables = new Node("Shootables");
		rootNode.attachChild(shootables);

		// シーン初期化
		setupScene();
		createTerrain();
		initCrossHairs();

		// キー入力を初期化
		initKeys();
	}

	private void setupScene(){
		// カメラの位置と移動速度を設定
		cam.setLocation(new Vector3f(-150f, 4.0f, 0f));
		flyCam.setMoveSpeed(10);
		//flyCam.setDragToRotate(true);

		// 空を追加
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));

		// ライティング設定
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		rootNode.addLight(sun);
	}

	// 地形を生成
	public void createTerrain(){
		// 地表のマテリアル
		mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
		mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
		// 草のテクスチャー
		Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		// 草のテクチャーを地形のマテリアルに適用
		mat_terrain.setTexture("Tex1", grass);
		mat_terrain.setFloat("Tex1Scale", 64f);
		// 土のマテリアル
		Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		// 土のテクスチャーを地形のマテリアルに追加
		mat_terrain.setTexture("Tex2", dirt);
		mat_terrain.setFloat("Tex2Scale", 32f);
		// 岩のテクスチャー
		Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		// 岩のテクスチャーを地形のマテリアルに追加
		mat_terrain.setTexture("Tex3", rock);
		mat_terrain.setFloat("Tex3Scale", 256f);
		// 高度マップの生成
		AbstractHeightMap heightmap = null;
		Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
		heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
		heightmap.load();
		// 地形に高度マップを適用
		int patchSize = 65;
		terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
		// 地形のマテリアルを地形の形状データに適用
		terrain.setMaterial(mat_terrain);
		terrain.setLocalTranslation(0, 0, 0);
		terrain.setLocalScale(1f, 0.05f, 1f);

		// 地形をクリック可能なノードに追加
		shootables.attachChild(terrain);
	}

	// 画面中央のターゲットに「+」記号を追加
	protected void initCrossHairs() {
		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+");
		ch.setLocalTranslation(
				settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}

	// キー入力の初期化
	private void initKeys() {
		// マウスの左クリックか、スペースキーで「Shoot」イベントを発生
		inputManager.addMapping("Shoot",
				new KeyTrigger(KeyInput.KEY_SPACE),
				new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		// イベントリスナーに「Shoot」イベントを追加
		inputManager.addListener(actionListener, "Shoot");
	}

	// イベントリスナー
	private ActionListener actionListener = new ActionListener() {
		public void onAction(String name, boolean keyPressed, float tpf) {
			// 検出したイベント名が「Shoot」だったら
			if (name.equals("Shoot") && !keyPressed) {
				// 1. 衝突判定のリストをリセット
				CollisionResults results = new CollisionResults();
				// 2. カメラから射出される光線を定義
				Ray ray = new Ray(cam.getLocation(), cam.getDirection());
				// 3. 光線とクリック可能な領域との衝突判定をして、結果をリストに保存
				shootables.collideWith(ray, results);
				// 4. 結果を使用
				if (results.size() > 0) {
					// 衝突リストの中から、もっとも近い場所を選択
					CollisionResult closest = results.getClosestCollision();
					if(displayNum < 1){
						// もし最初の物体の追加だったら、女性を配置
						putNewGirl(closest.getContactPoint());
					} else {
						// そうでなければ、ディスプレイを配置
						putNewDisplay(closest.getContactPoint());
					}
				} 
			}
		}
	};

	// 女性を追加
	private void putNewGirl(Vector3f pos){
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.rotate(0, (float)(Math.PI), 0);
		girl.setLocalTranslation(pos);
		this.rootNode.attachChild(girl);
		displayNum++;
	}

	// ディスプレイを追加
	private void putNewDisplay(Vector3f pos){
		PApplet applet = new OrangeWavePApplet();
		PAppletDisplayGeometry display = new PAppletDisplayGeometry("display"+displayNum, assetManager, 0.4f, 2f, applet, 20, 100, false);
		rootNode.attachChild(display);
		display.setLocalTranslation(pos.x, pos.y+1f, pos.z);
		displayNum++;
	}

	// 終了処理
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	// メイン
	public static void main(String[] args) {
		SimpleApplication app = new DisplayLandscapeNew();
		app.start();
	}
}