#version 300 es
precision mediump float;
in  vec4 vColor;
out vec4 fragColor;
uniform float aCoreX;
uniform float aCoreY;
uniform float aRadius;

void main()
{
   fragColor = vColor;
   if(aRadius!=0.0) {
       float dis = distance(vec2(aCoreX,aCoreY),vec2(gl_FragCoord.x,gl_FragCoord.y));
       if(dis<=aRadius) {
            fragColor.a=0.2;
        }
    }
}