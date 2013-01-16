attribute vec3 inPosition;
attribute vec3 inNormal;

uniform mat4 g_WorldViewProjectionMatrix;
uniform mat4 g_WorldMatrix;

#if NUM_PROJECTORS > 0
  uniform mat4 m_ProjectorViewProjectionMatrix0;

  #ifdef IS_PARALLEL_PROJECTION0
    uniform vec3 m_ProjectorDirection0;
  #else
    uniform vec3 m_ProjectorLocation0;
  #endif

  varying vec4 projCoord0;
  varying float cosAngle0;
#endif

#if NUM_PROJECTORS > 1
  uniform mat4 m_ProjectorViewProjectionMatrix1;

  #ifdef IS_PARALLEL_PROJECTION1
    uniform vec3 m_ProjectorDirection1;
  #else
    uniform vec3 m_ProjectorLocation1;
  #endif

  varying vec4 projCoord1;
  varying float cosAngle1;
#endif

#if NUM_PROJECTORS > 2
  uniform mat4 m_ProjectorViewProjectionMatrix2;

  #ifdef IS_PARALLEL_PROJECTION2
    uniform vec3 m_ProjectorDirection2;
  #else
    uniform vec3 m_ProjectorLocation2;
  #endif

  varying vec4 projCoord2;
  varying float cosAngle2;
#endif

#if NUM_PROJECTORS > 3
  uniform mat4 m_ProjectorViewProjectionMatrix3;

  #ifdef IS_PARALLEL_PROJECTION3
    uniform vec3 m_ProjectorDirection3;
  #else
    uniform vec3 m_ProjectorLocation3;
  #endif

  varying vec4 projCoord3;
  varying float cosAngle3;
#endif

#if NUM_PROJECTORS > 4
  uniform mat4 m_ProjectorViewProjectionMatrix4;

  #ifdef IS_PARALLEL_PROJECTION4
    uniform vec3 m_ProjectorDirection4;
  #else
    uniform vec3 m_ProjectorLocation4;
  #endif

  varying vec4 projCoord4;
  varying float cosAngle4;
#endif

#if NUM_PROJECTORS > 5
  uniform mat4 m_ProjectorViewProjectionMatrix5;

  #ifdef IS_PARALLEL_PROJECTION5
    uniform vec3 m_ProjectorDirection5;
  #else
    uniform vec3 m_ProjectorLocation5;
  #endif

  varying vec4 projCoord5;
  varying float cosAngle5;
#endif

#if NUM_PROJECTORS > 6
  uniform mat4 m_ProjectorViewProjectionMatrix6;

  #ifdef IS_PARALLEL_PROJECTION6
    uniform vec3 m_ProjectorDirection6;
  #else
    uniform vec3 m_ProjectorLocation6;
  #endif

  varying vec4 projCoord6;
  varying float cosAngle6;
#endif

#if NUM_PROJECTORS > 7
  uniform mat4 m_ProjectorViewProjectionMatrix7;

  #ifdef IS_PARALLEL_PROJECTION7
    uniform vec3 m_ProjectorDirection7;
  #else
    uniform vec3 m_ProjectorLocation7;
  #endif

  varying vec4 projCoord7;
  varying float cosAngle7;
#endif

const mat4 biasMat = mat4(0.5, 0.0, 0.0, 0.0,
                          0.0, 0.5, 0.0, 0.0,
                          0.0, 0.0, 0.5, 0.0,
                          0.5, 0.5, 0.5, 1.0);

void main() 
{
  vec4 worldPos = g_WorldMatrix * vec4(inPosition, 1.0);
  gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);

  #if NUM_PROJECTORS > 0
    projCoord0 = biasMat * m_ProjectorViewProjectionMatrix0 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION0
      cosAngle0 = dot(inNormal, -m_ProjectorDirection0);
    #else 
      cosAngle0 = dot(inNormal, normalize(m_ProjectorLocation0 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 1
    projCoord1 = biasMat * m_ProjectorViewProjectionMatrix1 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION1
      cosAngle1 = dot(inNormal, -m_ProjectorDirection1);
    #else 
      cosAngle1 = dot(inNormal, normalize(m_ProjectorLocation1 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 2
    projCoord2 = biasMat * m_ProjectorViewProjectionMatrix2 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION2
      cosAngle2 = dot(inNormal, -m_ProjectorDirection2);
    #else 
      cosAngle2 = dot(inNormal, normalize(m_ProjectorLocation2 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 3
    projCoord3 = biasMat * m_ProjectorViewProjectionMatrix3 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION3
      cosAngle3 = dot(inNormal, -m_ProjectorDirection3);
    #else 
      cosAngle3 = dot(inNormal, normalize(m_ProjectorLocation3 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 4
    projCoord4 = biasMat * m_ProjectorViewProjectionMatrix4 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION4
      cosAngle4 = dot(inNormal, -m_ProjectorDirection4);
    #else 
      cosAngle4 = dot(inNormal, normalize(m_ProjectorLocation4 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 5
    projCoord5 = biasMat * m_ProjectorViewProjectionMatrix5 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION5
      cosAngle5 = dot(inNormal, -m_ProjectorDirection5);
    #else 
      cosAngle5 = dot(inNormal, normalize(m_ProjectorLocation5 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 6
    projCoord6 = biasMat * m_ProjectorViewProjectionMatrix6 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION6
      cosAngle6 = dot(inNormal, -m_ProjectorDirection6);
    #else 
      cosAngle6 = dot(inNormal, normalize(m_ProjectorLocation6 - inPosition));
    #endif
  #endif

  #if NUM_PROJECTORS > 7
    projCoord7 = biasMat * m_ProjectorViewProjectionMatrix7 * worldPos;

    #ifdef IS_PARALLEL_PROJECTION7
      cosAngle7 = dot(inNormal, -m_ProjectorDirection7);
    #else 
      cosAngle7 = dot(inNormal, normalize(m_ProjectorLocation7 - inPosition));
    #endif
  #endif
}
