package net.unitedfield.cc;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;

public class PointLightTextureShadowRenderer extends PointLightShadowRenderer{    

	public PointLightTextureShadowRenderer(AssetManager manager, int width, int height, Texture texture){
    	super(manager, width, height);  
    	setTexture(manager, texture);          
    }
	public PointLightTextureShadowRenderer(AssetManager manager, float fov, float far, int width, int height, Texture texture){
		super(manager, fov, far, width, height);
		setTexture(manager, texture);		
	}
	
	private void setTexture(AssetManager manager, Texture texture){
        postshadowMat = new Material(manager, "myAssets/MatDefs/Shadow/PostTextureLightShadow.j3md");
        postshadowMat.setTexture("ShadowMap", shadowMap);        
        texture.setWrap(Texture.WrapMode.BorderClamp);
        texture.setMagFilter(MagFilter.Bilinear);
        texture.setMinFilter(MinFilter.Trilinear);
        postshadowMat.setTexture("ShadowTex", texture);     
	}
}