package com.example.daxinli.tempmusic.util.effect.TriangleFirework;

/**
 * Created by Daxin Li on 2018/3/23.
 * 存放粒子系统相关的数据信息
 */

public class tri_ParticleData {
    //lifeTime和moveSpan可以组合出三角形的生命周期效果
    public static final float[] AngleSpan = {0.2f,0.4f,0.6f,0.8f,1f};      //每次三角形的旋转角度 //5
    public static final float[] ScaleFactor = {0.97f,0.98f,0.985f,0.99f,0.95f};      //每次三角形的每次缩小程度   //5
    public static final int sysLifeTime = 3000;        //粒子系统的总声明周期
    public static final int[] singleLifeTime = { 800,1000,1200,1400,1600,1800,2000 };  //每个粒子的声明周期 可选择  //3
    public static final float[] moveSpan = {0.4f,0.5f,0.8f,1.0f,1.2f};   //粒子单位运动的距离大小
    public static final float[][] triColor = {          //三角形的颜色可选库
            {1,32/255f,80/255f,1f},        //深蓝
            {1,132/255f,40/255f,1f},       //粉紫
            {1,27/255f,161/255f,1f},       //天蓝
            {1,56/255f,1f,186/255f},       //淡绿
            {1,3/255f,1f,22/255f},         //草绿
    };
    public static final float[] vg = {0f,0.01f,0.02f,0.03f};        //粒子的重力因子
}
