package com.example.daxinli.tempmusic.util.effect.TriangleFirework;

import android.util.Log;

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
    tri_ParticleSingle tris[] = new tri_ParticleSingle[9];       //8个小三角形
    int state;
    long beginTime;
    long lastTime;
    int lifeTime ;
    Random random = new Random();
    boolean isEnd = false;
    public tri_ParticleSystem() {       //拟定生命周期 >
        initData();
    }
    public void initData() {
        //第一个三角形粒子
        int c = random.nextInt(tri_ParticleData.triColor.length);
        float a = tri_ParticleData.triColor[c][0],r = tri_ParticleData.triColor[c][1],
                g=tri_ParticleData.triColor[c][2],b=tri_ParticleData.triColor[c][3];
        float vx = 0 * tri_ParticleData.moveSpan[1];
        float vy = -1 * tri_ParticleData.moveSpan[1];
        tris[0] = new tri_ParticleSingle(
                540,1800,100,
                vx,vy,
                a,r,g,b,
                tri_ParticleData.AngleSpan[2],1,
                tri_ParticleData.singleLifeTime[4],
                0
        );

        //初始化数据
        lifeTime = tri_ParticleData.sysLifeTime;
        lastTime = 0;
        state = 1;
        isEnd = false;
        this.beginTime = System.currentTimeMillis();
    }
    private void runStateTwo() {
        //构建散开的三角形粒子
        for(int i=1;i<9;i++) {
            int rlifeTime = random.nextInt(tri_ParticleData.singleLifeTime.length);
            int rAngSpan = random.nextInt(tri_ParticleData.AngleSpan.length);
            int rScaleF = random.nextInt(tri_ParticleData.ScaleFactor.length);
            int c = random.nextInt(tri_ParticleData.triColor.length);
            float a = tri_ParticleData.triColor[c][0],r = tri_ParticleData.triColor[c][1],
                    g=tri_ParticleData.triColor[c][2],b=tri_ParticleData.triColor[c][3];
            int rVG = random.nextInt(tri_ParticleData.vg.length);
            if(i<5) rVG = 0;
            float vx = (float)Math.cos(i * Math.PI/4) * tri_ParticleData.moveSpan[1];
            float vy = (float)Math.sin(i * Math.PI/4) * tri_ParticleData.moveSpan[1];

            tris[i] = new tri_ParticleSingle(tris[0].x,tris[0].y,tris[0].radius,
                                                    vx,vy,
                                                    a,r,g,b,
                                                    tri_ParticleData.AngleSpan[rAngSpan],tri_ParticleData.ScaleFactor[rScaleF],
                                                    tri_ParticleData.singleLifeTime[rlifeTime],
                                                    rVG);
        }
    }
    public void drawSelf() {        //绘制动画
        if(isEnd) return ;          //如果死亡则不进行绘制
        if(state==1)  {
            if(lastTime!=0) tris[0].go(System.currentTimeMillis()-lastTime);
            tris[0].drawSelf();
        }
        else if(state==2) {
            long currentTime = System.currentTimeMillis();
            for(int i=1;i<=8;i++) {
                if(lastTime!=0) tris[i].go(currentTime-lastTime);
                tris[i].drawSelf();
            }
            if(currentTime-beginTime>=lifeTime) {
                isEnd = true;
            }
        }
        if(tris[0].isDead && state ==1) {
                Log.e(TAG, "drawSelf: ");
                runStateTwo();
                state = 2;
        }
        lastTime = System.currentTimeMillis();
    }
    public boolean isSysEnd() {
        //外部查询是否已经死亡
        return this.isEnd;
    }
}
