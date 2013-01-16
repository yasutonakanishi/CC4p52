package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.shadow.PssmShadowRenderer.CompareMode;
import com.jme3.shadow.PssmShadowRenderer.FilterMode;

public class HelloShadow extends SimpleApplication {

	@Override
	public void simpleInitApp() {         
		cam.setLocation(Vector3f.UNIT_XYZ.mult(6.0f)); // camera moves to 6.0, 6.0, 6.0 
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);    // and looks at 0,0,0.        
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);

        /*
         * light and shadow
         */
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(0.7f));
        rootNode.addLight(al);
        DirectionalLight dl1 = new DirectionalLight();
        dl1.setDirection(new Vector3f(0, -1, 0.5f).normalizeLocal());
        dl1.setColor(ColorRGBA.White.mult(1.5f));
        rootNode.addLight(dl1);             
        
        /* 
         * Please see difference between Basic Shadow and Pssm Shadow         
         */
        // basic shadow
//        BasicShadowRenderer bsr = new BasicShadowRenderer(assetManager, 512);
//        bsr.setDirection(dl1.getDirection());
//        viewPort.addProcessor(bsr);
        // pssm shadow
        PssmShadowRenderer pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 1);
        pssmRenderer.setDirection(dl1.getDirection());
        pssmRenderer.setLambda(0.55f);
        pssmRenderer.setShadowIntensity(0.8f);
        pssmRenderer.setCompareMode(CompareMode.Software);
        pssmRenderer.setFilterMode(FilterMode.PCF4);
        //pssmRenderer.displayDebug();     
        viewPort.addProcessor(pssmRenderer);
        
        /*
         * create the geometry and set shadowMode          
         */
        // floor
        Quad q = new Quad(20, 20);
        q.scaleTextureCoordinates(Vector2f.UNIT_XY.mult(10));
        Geometry geom = new Geometry("floor", q);
        Material mat = assetManager.loadMaterial("Textures/Terrain/Pond/Pond.j3m");
        mat.setFloat("Shininess", 0);
        geom.setMaterial(mat);        
        geom.rotate(-FastMath.HALF_PI, 0, 0);
        geom.center();
        geom.setShadowMode(ShadowMode.Receive);        // floor receives shadow
        rootNode.attachChild(geom);
        // tree 
        Spatial tree = assetManager.loadModel("Models/Tree/Tree.mesh.j3o");
        tree.setQueueBucket(Bucket.Transparent);        
        tree.setShadowMode(ShadowMode.CastAndReceive); // tree makes and receives shadow
        rootNode.attachChild(tree);                 
	}

	public static void main(String[] args) {
		SimpleApplication app = new HelloShadow();
		app.start();
	}
}
