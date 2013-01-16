/*
 * 人物の周囲をカメラが回転しながら撮影
 * その結果をProcessingで加工
 */

package simulation.cc;

import net.unitedfield.cc.CaptureCameraNode;
import net.unitedfield.cc.PAppletDisplayGeometry;
import simulation.p5.Mirror2PApplet;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;

public class MovingCameraMirror2Simulation extends SimpleApplication {
	Vector3f direction = new Vector3f();
	float	angle;	
	Node	cameraRotationCenterNode;
	CaptureCameraNode	captureCameraNode;
	
    @Override
    // アプリ初期設定
	public void simpleInitApp()
	{
		cameraRotationCenterNode = new Node("CamNode");
		rootNode.attachChild(cameraRotationCenterNode);
		
		setupEnvironment();
		setupCaptureCamereNodeAndDisplay();

		cam.setLocation(new Vector3f(-2f, 1.8f, 5f));
		flyCam.setMoveSpeed(5);  
		flyCam.setDragToRotate(true);
	}

	@Override
    // 更新
	public void simpleUpdate(float tpf)
	{
		angle += 0.002;
		direction.y = angle;

		Quaternion q = cameraRotationCenterNode.getLocalRotation();
		cameraRotationCenterNode.setLocalRotation(q.fromAngles(direction.x, direction.y, direction.z));
	}

	private void setupCaptureCamereNodeAndDisplay(){
		// CaptureCameraNode
		captureCameraNode = new CaptureCameraNode("cameraNode", 640, 480, assetManager, renderManager, renderer, rootNode);		
		captureCameraNode.setLocalTranslation(0, 1.4f, 2.2f);
		cameraRotationCenterNode.attachChild(captureCameraNode);
		
		//CapturePApplet
		//CapturePApplet captureApplet = new CapturePApplet();
		Mirror2PApplet captureApplet = new Mirror2PApplet();
		captureApplet.setCapture(captureCameraNode.getCapture());		
		
        /** In order to process image from camera at PApplet adequately,
        please use same width and height at CaptureCameraNode and PApplet */
	
		// Display
		PAppletDisplayGeometry display = new PAppletDisplayGeometry("display", assetManager, 4, 3, captureApplet, 640, 480, true);					
		rootNode.attachChild(display);
		display.setLocalTranslation(new Vector3f(0,3f,-7f));

		//sun
		DirectionalLight sun1 = new DirectionalLight();
		sun1.setColor(ColorRGBA.White);
		sun1.setDirection(new Vector3f(.5f,.5f,.5f).normalizeLocal());
		rootNode.addLight(sun1);
		DirectionalLight sun2 = new DirectionalLight();
		sun2.setColor(ColorRGBA.White);
		sun2.setDirection(new Vector3f(-.5f,.5f,-.5f).normalizeLocal());
		rootNode.addLight(sun2);

		//girl
		Spatial girl = assetManager.loadModel("myAssets/Models/WalkingGirl/WalkingGirl.obj");
		girl.rotate(0, (float)(Math.PI), 0);
		girl.setLocalTranslation(0, 0, -0.2f);
		this.rootNode.attachChild(girl);
		girl.setShadowMode(ShadowMode.CastAndReceive);
	}

	private void setupEnvironment() {
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/FullskiesBlueClear03.dds", false));

		Material mat = assetManager.loadMaterial("Textures/Terrain/Rocky/Rocky.j3m");
		Spatial scene = assetManager.loadModel("Models/Terrain/Terrain.mesh.xml");
		TangentBinormalGenerator.generate(((Geometry)((Node)scene).getChild(0)).getMesh());
		scene.setMaterial(mat);
		scene.setShadowMode(ShadowMode.CastAndReceive);
		scene.setLocalScale(400);
		scene.setLocalTranslation(0, -0.6f, -120);
		rootNode.attachChild(scene);     
		DirectionalLight sun = new DirectionalLight();
		Vector3f lightDir = new Vector3f(-0.12f, -0.3729129f, 0.74847335f);
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(2));
		scene.addLight(sun);      
	}

	public void putArrow(Vector3f pos, Vector3f dir, ColorRGBA color){
		Arrow arrow = new Arrow(dir);
		arrow.setLineWidth(4);
		putShape(arrow, color).setLocalTranslation(pos);
	}

	public Geometry putShape(Mesh shape, ColorRGBA color){
		Geometry g = new Geometry("shape", shape);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		rootNode.attachChild(g);
		return g;
	}

	@Override
	public void destroy() {
		super.destroy();
		System.exit(0);
	}

	public static void main(String[] args){
		SimpleApplication app = new MovingCameraMirror2Simulation();
		AppSettings s = new AppSettings(true);	
		s.setWidth(1024);
		s.setHeight(768);
		app.setSettings(s);
		app.setShowSettings(false);
		app.start();
	}
}