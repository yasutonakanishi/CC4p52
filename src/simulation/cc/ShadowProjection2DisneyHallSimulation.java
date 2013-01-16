/*
 * ウォルト・ディズニー・コンサートホールにプロジェクションマッピング
 *
 */

package simulation.cc;

import net.unitedfield.cc.PAppletProjectorShadowNode;
import processing.core.PApplet;
import simulation.p5.SimpleGridPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.util.TangentBinormalGenerator;

public class ShadowProjection2DisneyHallSimulation extends SimpleApplication {
    int NUM = 100;
    Spatial[] girl = new Spatial[NUM];
    PApplet applet;
    BasicShadowRenderer bsr;
    PAppletProjectorShadowNode ppn;

    @Override
    // 初期化
    public void simpleInitApp() {
        setupScene();
        setupProjector();
        setupEnvironment();
    }

    // プロジェクターを配置
    private void setupProjector() {
        // アプレット生成
        applet = new SimpleGridPApplet();

        // プロジェクタを配置
        ppn = new PAppletProjectorShadowNode("Projector", assetManager, viewPort, 45, 200, 1024, 768, applet, 600, 400,false);
        ppn.setLocalTranslation(new Vector3f(80f, 5f, 80f));
        ppn.lookAt(new Vector3f(0f, 10f, 0f), Vector3f.UNIT_Y);
        rootNode.attachChild(ppn);
        rootNode.attachChild(ppn.getFrustmModel());
    }

    private void setupEnvironment() {
        // カメラ設定
        cam.setFrustumPerspective(40f, 1f, 1f, 1000f);
        cam.setLocation(new Vector3f(10f, 50f, 140f));
        cam.lookAt(new Vector3f(0f, 20f, 0f), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(5);
        flyCam.setDragToRotate(true);
        
        // ライティング
        SpotLight spot = new SpotLight();
		spot = new SpotLight();
		spot.setSpotRange(2000);
		spot.setColor(ColorRGBA.White.clone().multLocal(5.0f));
		spot.setSpotOuterAngle(30 * FastMath.DEG_TO_RAD);
		spot.setSpotInnerAngle(10 * FastMath.DEG_TO_RAD);
		spot.setPosition(ppn.getLocalTranslation());
		spot.setDirection(new Vector3f(-1.0f,0.2f,-0.8f));
		rootNode.addLight(spot);
		
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(2.0f));
		rootNode.addLight(al);

        // 地形を生成
        Material mat = assetManager.loadMaterial("Textures/Terrain/Rocky/Rocky.j3m");
        Spatial scene = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
        TangentBinormalGenerator.generate(((Geometry)((Node)scene).getChild(0)).getMesh());
        scene.setMaterial(mat);
        scene.setLocalScale(1200);
        scene.setLocalTranslation(0, 0, -120);
        scene.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);
    }

    private void setupScene() {
    	bsr = new BasicShadowRenderer(assetManager, 4096);
		bsr.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		
        // 女性をランダムに配置
        for (int i = 0; i < girl.length; i++) {
            girl[i] = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
            Vector3f girlPos = new Vector3f((float) (Math.random() * 160f - 80f), 1.0f, (float) (Math.random() * 160f - 80f));
            girl[i].rotate(0, (float) (Math.random() * Math.PI / 2.0f), 0);
            girl[i].setLocalTranslation(girlPos);
            girl[i].setShadowMode(ShadowMode.CastAndReceive);
            this.rootNode.attachChild(girl[i]);
        }
        
        // 建築物のマテリアル
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setFloat("Shininess", 12);
		mat.setBoolean("HighQuality", true);
		mat.setColor("Ambient",  ColorRGBA.White.mult(0.5f));
		mat.setColor("Diffuse",  ColorRGBA.White.mult(0.5f));
		mat.setColor("Specular", ColorRGBA.White.mult(0.5f));
		
        /* object */
        Spatial object = assetManager.loadModel("myAssets/Models/WaltDisneyConcertHall/WaltDisneyConcertHall.obj");
        //object.setMaterial(mat);
        rootNode.attachChild(object);
        object.setShadowMode(ShadowMode.Receive);
    }

    @Override
    // 終了処理
    public void destroy() {
        super.destroy();
        System.exit(0);
    }
    
    // メイン
    public static void main(String[] args) {
        SimpleApplication app = new ShadowProjection2DisneyHallSimulation();
        app.setPauseOnLostFocus(false); 
        app.start();
    }
}