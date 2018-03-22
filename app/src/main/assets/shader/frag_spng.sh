#version 300 es
precision mediump float;
uniform float CLStep;
in vec2 vTextureCoord;
uniform sampler2D sTexture;
out vec4 fragColor;
void main()                         
{    
   vec4 finalColor; 
   vec4 switchcolor;
   float f=CLStep/100.0;     

   switchcolor= texture(sTexture, vTextureCoord)*(1.0+1.5*f); 
   finalColor=vec4(switchcolor.r,switchcolor.g,switchcolor.b,switchcolor.a*f);
   fragColor=finalColor;
}              