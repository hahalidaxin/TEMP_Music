package com.example.daxinli.tempmusic.util.effect.TriangleFirework;

import com.example.daxinli.tempmusic.object.Obj2DTriangle;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

/**
 * Created by Daxin Li on 2018/3/23.
 * 一个三角形对象  三角形会随着速度进行
 */

public class tri_ParticleSingle {
    //描述当前三角形的信息
    int lifeTime;
    int nowTime;
    float x,y,z,radius;
    float a,r,g,b;
    float vx,vy,vg;
    Obj2DTriangle tri ;
    boolean isDead;
    float AngleSpan ;
    float ScaleFactor;
    public tri_ParticleSingle(float x,float y,float radius,
                              float vx,float vy,
                              float a,float r,float g,float b,
                              float xAngle,float xScale,
                              int lifeTime,int vgType) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.lifeTime = lifeTime;
        this.AngleSpan = xAngle;
        this.ScaleFactor = xScale;
        isDead = false;
        this.vx = vx;
        this.vy = vy;
        vg = tri_ParticleData.vg[vgType];
        tri = new Obj2DTriangle(x,y,radius,
                                a,r,g,b,
                                0,1,
                                ShaderManager.getShader(6));
    }
    //go是直接操作xyz信息还是直接进行矩阵变换的呢
    public void go(float lifeSpanStep) {
        if(!isDead) {
            if(vy>0) vy = vy+vg*lifeSpanStep;        //添加重力因子的影响
            x = x + vx*lifeSpanStep;        //setXY
            y = y + vy*lifeSpanStep;
            tri.xAngle += AngleSpan;
            tri.xScale *= ScaleFactor;
            nowTime += lifeSpanStep;
            if(nowTime>=lifeTime) isDead = true;
        }
    }
    public void drawSelf() {        //三角形的自我绘制
        if(!isDead) {
            tri.setX(x);
            tri.setY(y);
            tri.drawSelf();
        }
    }
}
