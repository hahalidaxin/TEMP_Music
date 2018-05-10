package com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Daxin Li on 2018/5/9.
 */
public class PointsManager {
    private static final String TAG = "PointsManager";
    private float speed = 5;            //向右移动的总速度
    private static final int numberLimit = 9;
    public static final float EPS = 5.0f;        //最小Y误差
    public static final float Ca = 1,Cr = 1,Cg = 0,Cb = 0;   //中心点的颜色
    public static final float CW = 100,CH = 100;               //中心点的长宽

    public Queue<points> linePtsQueue = new LinkedBlockingQueue<points>();         //存储节奏型的点数据
    public points[] linePts = new points[15];

    public float centralX,centralY;
    public int uplineY;
    public int downlineY;

    public Random random = new Random();
    public int borderX,borderY,borderWidth,borderHeight;

    public PointsManager(int x,int y,int w,int h,int time) {
        this.borderX = x; this.borderY = y;
        this.borderWidth = w; this.borderHeight = h;

        uplineY = (int)(y);
        downlineY = (int)(y+h);
        float ptSpanWidth = w/(numberLimit/2);



        int insertRk = (numberLimit+1)/2-1;
        int upx=0,downx=0;
        for(int i=0;i<(numberLimit+1)/2;i++) {
            upx = (int)(x+i*ptSpanWidth);
            downx = (int)(upx+0.5*ptSpanWidth);
            linePtsQueue.add(new points(upx,uplineY));

            if(linePtsQueue.size()==insertRk+1) {                      //初始时刻中心点的坐标
                centralX = upx; centralY = uplineY;
            }
            if(i!=(numberLimit+1)/2-1)
                linePtsQueue.add(new points(downx,downlineY));
        }
        this.speed = (downlineY-uplineY)/time*(Muti_SurfaceView.DRAWSPANTIME)*(downx-upx)/(downlineY-uplineY);
    }
    public void go() {
        int qsize = linePtsQueue.size();
        int qcnt = 0;
        while(!linePtsQueue.isEmpty()) {
            linePts[qcnt++] = linePtsQueue.remove();
        }
        //计算中心点的坐标位置
        centralX+=speed;
        for(int i=0;i<qsize;i++) {
            points qn1 = linePts[i],qn2 = linePts[i+1];
            if(centralX>=qn1.x && centralX<=qn2.x) {
                if(qn1.y<qn2.y) {
                    centralY = qn1.y + (qn2.y - qn1.y) * (centralX - qn1.x) / (qn2.x - qn1.x);
                } else {
                    centralY = qn2.y + (qn1.y - qn2.y) * (qn2.x - centralX) / (qn2.x - qn1.x);
                }
            }
        }
        centralX -= speed;
        //计算线段点的坐标位置
        qcnt=-1;
        for(int i=0;i<qsize;i++) {
            points qnode = linePts[i];
            points qn1=linePts[0],qn2=linePts[1];
            qnode.x -= speed;

            if (qnode.x < borderX) {
                qnode.x += borderWidth;
                qcnt=i;
            }
        }
        for(int i=qcnt+1;i<qsize;i++) {
            linePtsQueue.add(linePts[i]);
        }
        for(int i=0;i<=qcnt;i++) {
            linePtsQueue.add(linePts[i]);
        }
    }
}