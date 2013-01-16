package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.LightNode;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;

public class HelloPointLight extends SimpleApplication {
    float	angle;
    Node	movingNode;

    @Override
    public void simpleInitApp() {
        //Torus torus = new Torus(10, 6, 1, 3); //Mesh
        Torus torus = new Torus(50, 30, 1, 3);
        Geometry g = new Geometry("Torus Geom", torus);
        g.rotate(-FastMath.HALF_PI, 0, 0);
        g.center();
        //g.move(0, 1, 0);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 32f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient",  ColorRGBA.Black);
        mat.setColor("Diffuse",  ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
//        mat.setBoolean("VertexLighting", true);
//        mat.setBoolean("LowQuality", true);
        g.setMaterial(mat);
        rootNode.attachChild(g);

        /** Model for light**/
        Geometry lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
                
        /** PointLight and LightNode**/
        PointLight pl = new PointLight();
        pl.setColor(ColorRGBA.Green);
        pl.setRadius(4f);
        rootNode.addLight(pl);      
        LightNode lightNode=new LightNode("pointLight", pl);
        
        /** Node to contain LightNode and model Geometry**/
        movingNode=new Node("lightParentNode");
        movingNode.attachChild(lightMdl);  
        movingNode.attachChild(lightNode);
        rootNode.attachChild(movingNode);        

//        DirectionalLight dl = new DirectionalLight();
//        dl.setColor(ColorRGBA.Red);
//        dl.setDirection(new Vector3f(0, 1, 0));
//        rootNode.addLight(dl);
        
        cam.setLocation(new Vector3f(5.0347548f, 6.6481347f, 3.74853f));
        cam.setRotation(new Quaternion(-0.19183293f, 0.80776674f, -0.37974006f, -0.40805697f));
    }

    @Override
    public void simpleUpdate(float tpf){    	
        angle += tpf;
        angle %= FastMath.TWO_PI;

        movingNode.setLocalTranslation(new Vector3f(FastMath.cos(angle) * 3f, 2, FastMath.sin(angle) * 3f));
    }
    
	public static void main(String[] args) {
		SimpleApplication app = new HelloPointLight();
		app.start();
	}
}
