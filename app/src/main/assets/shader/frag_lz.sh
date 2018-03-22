#version 300 es
precision mediump float;
uniform vec4 startColor;
uniform vec4 endColor;
uniform float sjFactor;
uniform float bj;
uniform sampler2D sTexture;
in vec2 vTextureCoord;
in vec3 vPosition;
out vec4 fragColor;
void main()                         
{               
    vec4 colorTL = texture(sTexture, vTextureCoord); 
    vec4 colorT;
    float disT=distance(vPosition,vec3(0.0,0.0,0.0));
    float tampFactor=(1.0-disT/bj)*sjFactor;
    vec4 factor4=vec4(tampFactor,tampFactor,tampFactor,tampFactor);
    colorT=clamp(factor4,endColor,startColor);
    colorT=colorT*colorTL.a;  
    fragColor=colorT;
}