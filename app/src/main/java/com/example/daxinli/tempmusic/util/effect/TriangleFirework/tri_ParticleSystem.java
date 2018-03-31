package com.example.daxinli.tempmusic.util.effect.TriangleFirework;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Daxin Li on 2018/3/23.
 * 粒子系统的总操作类
 * 刚开始一个三角形向上 最后炸裂开8个三角形
 */

//singleTRi的参数表
//    float x,float y,float radius,             初始位置    与半径
//            float vx,float vy,                初始速度
//            float a,float r,float g,float b,  颜色
//            float xAngle,float xScale,        旋转角度因子与放大缩小因子
//            int lifeTime,int vgType)          总生命周期，vg因子类型

public class tri_ParticleSystem {
    private static final String TAG = "tri_ParticleSystem";
    ArrayList<tri_ParticleSingle> tris;
    int state;
    long beginTime;
    long lastTime;
    int lifeTime ;
    int particleType = 1;
    Random random = new Random();
    boolean isEnd = false;
    public tri_ParticleSystem(int particleType,float startX,float startY) {       //粒子系统的类型
        this.particleType = particleType;
        initData(startX,startY);
    }
    public void initData(float X,float Y) {
        //第一个三角形粒子
        tris = new ArrayList<tri_ParticleSingle>();           //初始化粒子数组
        int c = random.nextInt(tri_ParticleData.triColor.length);
        float a = tri_ParticleData.triColor[c][0],r = tri_ParticleData.triColor[c][1],
                g=tri_ParticleData.triColor[c][2],b=tri_ParticleData.triColor[c][3];
        float vx = 0 * tri_ParticleData.moveSpan[3];
        float vy = -1 * tri_ParticleData.moveSpan[3];
        tris.add(new tri_ParticleSingle(
                X,Y,100,
                vx,vy,
                a,r,g,b,
                tri_ParticleData.AngleSpan[2],1,
                tri_ParticleData.singleLifeTime[4],
                0
        ));

        //初始化数据
        lifeTime = tri_ParticleData.sysLifeTime;
        lastTime = 0;
        state = 1;
        isEnd = false;
        this.beginTime = System.currentTimeMillis();
    }
    private void runStateTwo(int type) {
        switch(particleType) {
            case 1:
                //第一类型： 散开三角形
                for(int i=1;i<9;i++) {
                    int rlifeTime = random.nextInt(tri_ParticleData.singleLifeTime.length);
                    int rAngSpan = random.nextInt(tri_ParticleData.AngleSpan.length);
                    int rScaleF = random.nextInt(tri_ParticleData.ScaleFactor.length);
                    int c = random.nextInt(tri_ParticleData.triColor.length);
                    float a = tri_ParticleData.triColor[c][0],r = tri_ParticleData.triColor[c][1],
                            g=tri_ParticleData.triColor[c][2],b=tri_ParticleData.triColor[c][3];
                    int rVG = random.nextInt(tri_ParticleData.vg.length);
                    //if(i<5) rVG = 0;
                    rVG = 2;
                    float vx = (float)Math.cos(i * Math.PI/4) * tri_ParticleData.moveSpan[3];
                    float vy = (float)Math.sin(i * Math.PI/4) * tri_ParticleData.moveSpan[3];
                    tri_ParticleSingle tri0 = tris.get(0);
                    tris.add(new tri_ParticleSingle(tri0.x,tri0.y,tri0.radius,
                            vx,vy,
                            a,r,g,b,
                            tri_ParticleData.AngleSpan[rAngSpan],tri_ParticleData.ScaleFactor[rScaleF],
                            tri_ParticleData.singleLifeTime[rlifeTime],
                            tri_ParticleData.vg[rVG]));
                }
                break;
            case 2:
                //第二类型： 烟花样式三角形
                for(int i=-1;i<3;i++) {
                    int rlifeTime = random.nextInt(tri_ParticleData.singleLifeTime.length);
                    int rAngSpan = random.nextInt(tri_ParticleData.AngleSpan.length);
                    int rScaleF = random.nextInt(tri_ParticleData.ScaleFactor.length-3);
                    int c = random.nextInt(tri_ParticleData.triColor.length);
                    float a = tri_ParticleData.triColor[c][0],r = tri_ParticleData.triColor[c][1],
                            g=tri_ParticleData.triColor[c][2],b=tri_ParticleData.triColor[c][3];
                    float vx = -(float)Math.cos(i * Math.PI/6) * tri_ParticleData.moveSpan[i+2];
                    float vy = -(float)Math.sin(i * Math.PI/6) * tri_ParticleData.moveSpan[i+1];
                    int rVG = 2;//random.nextInt(tri_ParticleData.vg.length);
                    tri_ParticleSingle tri0 = tris.get(0);
                    tris.add(new tri_ParticleSingle(tri0.x,tri0.y,tri0.radius,
                            vx,vy,
                            a,r,g,b,
                            tri_ParticleData.AngleSpan[rAngSpan],tri_ParticleData.ScaleFactor[rScaleF],
                            tri_ParticleData.singleLifeTime[rlifeTime],
                            tri_ParticleData.vg2[rVG]));
                    rlifeTime = random.nextInt(tri_ParticleData.singleLifeTime.length);
                    rAngSpan = random.nextInt(tri_ParticleData.AngleSpan.length);
                    rScaleF = random.nextInt(tri_ParticleData.ScaleFactor.length-3);
                    c = random.nextInt(tri_ParticleData.triColor.length);
                    a = tri_ParticleData.triColor[c][0];r = tri_ParticleData.triColor[c][1];
                            g=tri_ParticleData.triColor[c][2];b=tri_ParticleData.triColor[c][3];
                    vx = -(float)Math.cos(Math.PI-i * Math.PI/6) * tri_ParticleData.moveSpan[i+2];
                    vy = -(float)Math.sin(Math.PI-i * Math.PI/6) * tri_ParticleData.moveSpan[i+1];
                    rVG = 2;//random.nextInt(tri_ParticleData.vg.length);
                    tri0 = tris.get(0);
                    tris.add(new tri_ParticleSingle(tri0.x,tri0.y,tri0.radius,
                            vx,vy,
                            a,r,g,b,
                            tri_ParticleData.AngleSpan[rAngSpan],tri_ParticleData.ScaleFactor[rScaleF],
                            tri_ParticleData.singleLifeTime[rlifeTime],
                            tri_ParticleData.vg2[rVG]));
                }
                break;
        }
    }

    public void drawSelf() {        //绘制动画
        if(isEnd) return ;          //如果死亡则不进行绘制
        if(state==1)  {
            if(lastTime!=0) tris.get(0).go(System.currentTimeMillis()-lastTime);
            tris.get(0).drawSelf();
        }
        else if(state==2) {
            long currentTime = System.currentTimeMillis();
            for(int i=1;i<tris.size();i++) {
                tri_ParticleSingle tri = tris.get(i);
                if(tri==null) break;
                if(lastTime!=0) tri.go(currentTime-lastTime);
                tri.drawSelf();
            }
            if(currentTime-beginTime>=lifeTime) {
                isEnd = true;
            }
        }
        if(tris.get(0).isDead && state ==1) {
                runStateTwo(particleType);
                state = 2;
        }
        lastTime = System.currentTimeMillis();
    }
    public boolean isSysEnd() {
        //外部查询是否已经死亡
        return this.isEnd;
    }
}
