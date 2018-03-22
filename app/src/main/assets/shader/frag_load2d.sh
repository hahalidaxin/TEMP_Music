#version 300 es
precision mediump float;
in float vxPosition;
in vec2 vTextureCoord;
uniform sampler2D sTexture;
uniform float xPosition;
out vec4 fragColor;
void main()                         
{         
     float ff=xPosition-90.0;
     if(vxPosition+0.72>= 0.0 && vxPosition +0.72< ff/625.0){
         fragColor = vec4(1.0,0.0,0.0,0.5);     
     }else if(vxPosition+0.72>=ff/625.0 && vxPosition+0.72<=1.44){
         fragColor = texture(sTexture, vTextureCoord);
     }
    
}              