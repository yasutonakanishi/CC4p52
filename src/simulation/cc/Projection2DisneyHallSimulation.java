/*
 * ウォルト・ディズニー・コンサートホールにプロジェクションマッピング
 *
 */

package simulation.cc;

import net.unitedfield.cc.PAppletProjectorNode;
import processing.core.PApplet;
import simulation.p5.SimpleGridPApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.TextureProjectorRenderer;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.TangentBinormalGenerator;

public class Projection2DisneyHallSimulation extends SimpleApplication {
    int NUM = 100;
    Spatial[] girl = new Spatial[NUM];

    @Override
    // 初期化
    public void simpleInitApp() {
        setupScene();
        setupProjector();
        setupEnvironment();
    }

    // プロジェクターを配置
    private void setupProjector() {
        //projector(new!)
        PApplet applet = new SimpleGridPApplet();
        PAppletProjectorNode projectorNode = new PAppletProjectorNode("projector0", assetManager, applet, 400, 300, true);
        projectorNode.setLocalTranslation(new Vector3f(100f, 5f, 100f));
        projectorNode.lookAt(new Vector3f(0, 10f, 0f), Vector3f.UNIT_Y);
        rootNode.attachChild(projectorNode);
        rootNode.attachChild(projectorNode.getFrustmModel()); // if you don't want to see frustum, please don't attach it to rootNode. 
        //projector should be added to TextureProjectorRenderer, and TextureProjectorRenderer should be added to ViewPort.
		TextureProjectorRenderer ptr = new TextureProjectorRenderer(assetManager);
		ptr.getTextureProjectors().add(projectorNode.getProjector());
		viewPort.addProcessor(ptr);
    }

    private void setupEnvironment() {
        // カメラ設定
        cam.setFrustumPerspective(40f, 1f, 1f, 1000f);
        cam.setLocation(new Vector3f(10f, 50f, 140f));
        cam.lookAt(new Vector3f(0f, 20f, 0f), Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(5);
        flyCam.setDragToRotate(true);
        
        // ライティング
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(0.2f));
		rootNode.addLight(al);
		DirectionalLight sun = new DirectionalLight();
        Vector3f lightDir=new Vector3f(-0.37352666f, -0.50444174f, -0.7784704f);
        sun.setDirection(lightDir);
        sun.setColor(ColorRGBA.White.clone().multLocal(0.8f));
        rootNode.addLight(sun); 

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
        SimpleApplication app = new Projection2DisneyHallSimulation();
        app.setPauseOnLostFocus(false); 
        app.start();
    }
}