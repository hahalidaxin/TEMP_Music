#version 300 es
precision mediump float;
uniform float ColorCS;
in vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform sampler2D adpmTexCoor;
out vec4 fragColor;
void main()                         
{  
  vec4 switchColor;
  vec4 finalColor;
  float bl=ColorCS/100.0;
  switchColor = texture(sTexture, vTextureCoord);
  finalColor  =vec4(switchColor.r,switchColor.g,switchColor.b,bl);

 fragColor = finalColor;  
}   