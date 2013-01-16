package simulation.cc;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class ControlSinMotion extends AbstractControl {

	//Vector3f initLoc;
	float phase;
	float delay;
	float speed;
	
	ControlSinMotion(float _delay) {
		//initLoc = geom.getLocalTranslation();
		phase = 0;
		delay = _delay;
		speed = 0.006f;
	}
	
	public Control cloneForSpatial(Spatial spatial) {
		return null;
	}

	@Override
	protected void controlUpdate(float tpf) {
		spatial.setLocalTranslation(spatial.getLocalTranslation().x, (float)(Math.sin(phase + delay)) * 0.49f, spatial.getLocalTranslation().z);
		phase += speed;
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		//TODO Auto-generated method stub		
	}

}
