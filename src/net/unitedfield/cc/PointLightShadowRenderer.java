package net.unitedfield.cc;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;

/**
 * @author naka
 */
public class PointLightShadowRenderer implements SceneProcessor {
	protected RenderManager renderManager;
	protected ViewPort		viewPort;

	protected Camera		shadowCam;
	float fov	= 45;	// default shadowCam fov:field of view angle
	float near	= 1;	
	float far	= 100;
	protected Vector3f[] points = new Vector3f[8];
	
	protected FrameBuffer	shadowFB;
	protected Texture2D		shadowMap;	
	protected Material preshadowMat;
	protected Material postshadowMat;
	protected boolean noOccluders = false;
					
	public PointLightShadowRenderer(AssetManager manager, int width, int height) {
		shadowCam = new Camera(width, height);
		shadowCam.setFrustumPerspective(fov, (float)width/height, near, far);

		shadowFB = new FrameBuffer(width, height, 1); 
		shadowMap = new Texture2D(width, height, Format.Depth);
		shadowFB.setDepthTexture(shadowMap);

		preshadowMat  = new Material(manager, "Common/MatDefs/Shadow/PreShadow.j3md");
		postshadowMat = new Material(manager, "Common/MatDefs/Shadow/PostShadow.j3md");
		postshadowMat.setTexture("ShadowMap", shadowMap);
		
		for (int i = 0; i < points.length; i++) {
			points[i] = new Vector3f();
		}
	}
	public PointLightShadowRenderer(AssetManager manager, float fov, float far, int width, int height) {		
		this(manager, width, height);
		
		setFov(fov);
		setFar(far);
		shadowCam.setFrustumPerspective(fov, (float)width/height, near, far);
	}
	
	public void initialize(RenderManager rm, ViewPort vp) {
		renderManager = rm;
		viewPort = vp;
		reshape(vp, vp.getCamera().getWidth(), vp.getCamera().getHeight());
	}

	public boolean isInitialized() {
		return viewPort != null;
	}

	public	Quaternion getRotation(){
		return this.shadowCam.getRotation();
	}
	public void setDirection(Quaternion direction) {
		this.shadowCam.setRotation(direction);		
	}

	public void setLocation(Vector3f location) {
		this.shadowCam.setLocation(location);
	}
	public void setLocation(float x, float y, float z){
		this.setLocation(new Vector3f(x,y,z));
	}
	public void setLocalTranslation(Vector3f translation) {
		this.shadowCam.getLocation().addLocal(translation);
	}
	public void setLocalTranslation(float x, float y, float z){
		this.setLocalTranslation(new Vector3f(x,y,z));
	}	
	public Vector3f getLocation() {
		return shadowCam.getLocation();
	}

	public Vector3f[] getPoints() {
		return points;
	}
	
	public Camera getShadowCamera() {
		return shadowCam;
	}
	
	public Material getPostShadowMat(){
		return postshadowMat;
	}
	
	public void setFov(float fov){
		this.fov = fov;
	}
	public float getFov(){
		return this.fov;
	}
	public void setFar(float far) {
		this.far = far;
	}
	public float getFar(){
		return this.far;
	}

	/*
	 * 
	 */
	
	/**
	 * Called before the a frame is rendered.
	 * @see SceneProcessor
	 */  
	@Override
	public void preFrame(float tpf) { }
	  
	/**
	 * Called before the render queue is flushed.
	 * @see SceneProcessor
	 */  
	@Override
	public void postQueue(RenderQueue rq) { }	

	public void postFrame(FrameBuffer out) { //drawing shadow
		if (!noOccluders) {			
			postshadowMat.setMatrix4("LightViewProjectionMatrix", shadowCam.getViewProjectionMatrix());			
			renderManager.setForcedMaterial(postshadowMat);	
			viewPort.getQueue().renderShadowQueue(ShadowMode.Receive, renderManager,viewPort.getCamera(), true);
						
			Renderer r = renderManager.getRenderer();
			renderManager.setCamera(shadowCam, false);
			renderManager.setForcedMaterial(preshadowMat);			
			r.setFrameBuffer(shadowFB);
			r.clearBuffers(false, true, false);
			viewPort.getQueue().renderShadowQueue(ShadowMode.Cast, renderManager, shadowCam, true);
			r.setFrameBuffer(viewPort.getOutputFrameBuffer());
			
			renderManager.setForcedMaterial(null);
		}
	}

	public void cleanup() {}
	public void reshape(ViewPort vp, int w, int h) {}
	
}