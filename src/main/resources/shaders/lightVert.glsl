uniform mat4 modelview;
uniform mat4 transform;
uniform mat3 normalMatrix;
uniform mat4 texMatrix;

uniform vec4 lightPosition;  // Light position in world coordinates

attribute vec4 position;
attribute vec4 color;
attribute vec3 normal;
attribute vec2 texCoord;

varying vec4 vertColor;

void main() {
  gl_Position = transform * position;
  vec3 ecPosition = vec3(modelview * position);  // Eye coordinates
  vec3 ecNormal = normalize(normalMatrix * normal);

  vec3 lightPos = lightPosition.xyz;
  // Calculate direction from the vertex to the light source
  vec3 direction = normalize(lightPos - ecPosition);
  
  // Compute the distance from the light to the vertex
  float distance = length(lightPos - ecPosition);
  //distance = max(1.f, distance);  // Avoid division by zero
                                   
  // Apply inverse square law for light attenuation
  float attenuation = 1.0 / (1.0 + 0.001 * distance + 0.0001 * distance * distance);  // Avoid division by zero

  // Calculate the lighting intensity
  float intensity = max(0.0, dot(direction, ecNormal)) * attenuation;
  //float intensity = attenuation;
  //float intensity = max(0.0, dot(direction, ecNormal));
  // Apply the lighting intensity to the color
  vertColor = vec4(intensity, intensity, intensity, 1.0) * color;

}
