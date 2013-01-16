#import "Common/ShaderLib/Shadow.glsllib"

uniform SHADOWMAP m_ShadowMap;
uniform sampler2D m_ShadowTex;

varying vec4 projCoord;

void main() {
	vec4 coord = projCoord;
	coord.xyz /= coord.w;

	float shad = Shadow_GetShadow(m_ShadowMap, coord)*0.7+0.3;
	
	if(shad<1.0){
		gl_FragColor = vec4(shad,shad,shad,1.0);
	}else{								
		if(texture2DProj(m_ShadowTex, projCoord).r > 0.0 )
			gl_FragColor = texture2DProj(m_ShadowTex, projCoord);
		else
			gl_FragColor = vec4(1.0);
	}
}

