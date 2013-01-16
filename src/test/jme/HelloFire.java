package test.jme;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;

public class HelloFire extends SimpleApplication {

	public static void main(String[] args) {
		SimpleApplication app = new HelloFire();
		app.start();
	}

	@Override
	public void simpleInitApp() {
	    // Uses Texture from jme3-test-data library
        ParticleEmitter fire = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        //mat_red.getAdditionalRenderState().setDepthTest(true);
        //mat_red.getAdditionalRenderState().setDepthWrite(true);
        fire.setMaterial(mat_red);
        fire.setImagesX(2); fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.setInitialVelocity(new Vector3f(0, 2, 0));
        fire.setStartSize(0.6f);
        fire.setEndSize(0.1f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(1.5f);
        fire.setVelocityVariation(0.3f);
        fire.setLocalTranslation(1.0f, 0, 1.0f);
        fire.setLocalScale(0.3f);
        fire.setQueueBucket(Bucket.Translucent);
        rootNode.attachChild(fire);		
	}

}
