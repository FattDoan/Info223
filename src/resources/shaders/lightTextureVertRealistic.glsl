uniform mat4 transform;
uniform mat4 texMatrix;

attribute vec3 normal;
attribute vec4 position;
attribute vec4 color;
attribute vec2 texCoord;

varying vec3 vertNormal;
varying vec4 vertPosition;
varying vec4 vertColor;
varying vec4 vertTexCoord;
void main() {
    gl_Position = transform * position;
    vertNormal = normal;
    vertPosition = position;
    vertColor = color;
    vertTexCoord = texMatrix * vec4(texCoord, 1.0, 1.0);
}
