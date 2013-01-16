package net.unitedfield.cc;

import processing.video.Capture;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;

public class CaptureCameraNode extends CameraNode {
	CaptureJME3toP5 captureJME3toP5;
	/*
	 * Y is up, and camera orientation is along Z axis.
	 */

	public CaptureCameraNode(String name, int width, int height, AssetManager assetManager, RenderManager renderManager, Renderer renderer, Node rootNode){
		super(name, new CameraControl());
		this.captureJME3toP5 = new CaptureJME3toP5(null, width, height, renderManager, renderer, rootNode); 
		this.setName(name);		
		this.setCamera(this.captureJME3toP5.getCamera());
		this.setControlDir(ControlDirection.SpatialToCamera);
		
		Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
		Geometry outerCaseL = new Geometry();
		Mesh boxL = new Box(0.05f,0.05f,0.15f);		
		outerCaseL.setMesh(boxL);
		outerCaseL.setMaterial(mat);
		outerCaseL.setLocalTranslation(0f, 0f, -0.05f);
		Geometry outerCaseS = new Geometry();
		Mesh boxS = new Box(0.025f, 0.025f, 0.025f);
		outerCaseS.setMesh(boxS);
		outerCaseS.setMaterial(mat);
		outerCaseS.setLocalTranslation(0f, 0f, 0.125f);
		this.attachChild(outerCaseS);
		this.attachChild(outerCaseL);
		
		this.lookAt(outerCaseL.getLocalTranslation(), Vector3f.UNIT_Y);
	}

	public Capture getCapture() {		
		return captureJME3toP5;
	}
}