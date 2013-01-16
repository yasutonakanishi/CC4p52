package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

public class HelloSimpleWater extends SimpleApplication implements ActionListener {

    Material mat;
    Spatial waterPlane;
    Geometry lightSphere;
    SimpleWaterProcessor waterProcessor;
    Node sceneNode;
    boolean useWater = true;
    private Vector3f lightPos =  new Vector3f(33,12,-29);

    @Override
    public void simpleInitApp() {
        initInput();
        initScene();

        //create processor
        waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(sceneNode);
        waterProcessor.setDebug(true);
        viewPort.addProcessor(waterProcessor);

        waterProcessor.setLightPosition(lightPos);

        //create water quad
        //waterPlane = waterProcessor.createWaterGeometry(100, 100);
        waterPlane=(Spatial)  assetManager.loadModel("Models/WaterTest/WaterTest.mesh.xml");
        waterPlane.setMaterial(waterProcessor.getMaterial());
        waterPlane.setLocalScale(40);
        waterPlane.setLocalTranslation(-5, 0, 5);

        rootNode.attachChild(waterPlane);
    }

    private void initScene() {
        //init cam location
        cam.setLocation(new Vector3f(0, 10, 10));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        //init scene
        sceneNode = new Node("Scene");
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        geom.setMaterial(mat);
        sceneNode.attachChild(geom);

        // load sky
        sceneNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        rootNode.attachChild(sceneNode);

        //add lightPos Geometry
        Sphere lite=new Sphere(8, 8, 3.0f);
        lightSphere=new Geometry("lightsphere", lite);
        lightSphere.setMaterial(mat);
        lightSphere.setLocalTranslation(lightPos);
        rootNode.attachChild(lightSphere);
    }

    protected void initInput() {
        flyCam.setMoveSpeed(3);
        //init input
        inputManager.addMapping("use_water", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addListener(this, "use_water");
        inputManager.addMapping("lightup", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addListener(this, "lightup");
        inputManager.addMapping("lightdown", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, "lightdown");
        inputManager.addMapping("lightleft", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(this, "lightleft");
        inputManager.addMapping("lightright", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addListener(this, "lightright");
        inputManager.addMapping("lightforward", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addListener(this, "lightforward");
        inputManager.addMapping("lightback", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addListener(this, "lightback");
    }

    @Override
    public void simpleUpdate(float tpf) {
        fpsText.setText("Light Position: "+lightPos.toString()+" Change Light position with [U], [H], [J], [K] and [T], [G] Turn off water with [O]");
        lightSphere.setLocalTranslation(lightPos);
        waterProcessor.setLightPosition(lightPos);
    }

    public void onAction(String name, boolean value, float tpf) {
        if (name.equals("use_water") && value) {
            if (!useWater) {
                useWater = true;
                waterPlane.setMaterial(waterProcessor.getMaterial());
            } else {
                useWater = false;
                waterPlane.setMaterial(mat);
            }
        } else if (name.equals("lightup") && value) {
            lightPos.y++;
        } else if (name.equals("lightdown") && value) {
            lightPos.y--;
        } else if (name.equals("lightleft") && value) {
            lightPos.x--;
        } else if (name.equals("lightright") && value) {
            lightPos.x++;
        } else if (name.equals("lightforward") && value) {
            lightPos.z--;
        } else if (name.equals("lightback") && value) {
            lightPos.z++;
        }
    }

    public static void main(String[] args) {
        SimpleApplication app = new HelloSimpleWater();
        app.start();
    }
}
