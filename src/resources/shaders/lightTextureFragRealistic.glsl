#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform mat4 modelview;
uniform mat4 transform;
uniform mat3 normalMatrix;
uniform vec4 lightPosition;

uniform sampler2D texture;
uniform mat4 texMatrix;

varying vec4 vertColor;
varying vec3 vertNormal;
varying vec4 vertPosition;
varying vec4 vertTexCoord;

void main() {
    vec3 ecPosition = vec3(modelview * vertPosition);
    vec3 ecNormal = normalize(normalMatrix * vertNormal);

    vec3 lightPos = lightPosition.xyz;
    vec3 direction = normalize(lightPos - ecPosition);

    float distance = length(lightPos - ecPosition);
    float attenuation = 1.0 / (1.0 + 0.001 * distance + 0.0001 * distance * distance);

    float intensity = max(0.0, dot(direction, ecNormal)) * attenuation;

    vec4 texColor = texture2D(texture, vertTexCoord.st);
    gl_FragColor = vec4(intensity, intensity, intensity, 1.0) * vertColor * texColor;
}
