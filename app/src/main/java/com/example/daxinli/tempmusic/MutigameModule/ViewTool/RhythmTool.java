package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.object.Obj2DLine;
import com.example.daxinli.tempmusic.object.Obj2DPoint;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Math.abs;

/**
 * Created by Daxin Li on 2018/5/3.
 * 多人游戏中的节奏型工具，用来显示节奏型
 */

public class RhythmTool extends BaseViewTool{
    private static final int numberLimit = 7;
    private Queue<points> tmpQueue;
    private Obj2DLine lineDrawer;
    private Obj2DPoint surPtsDrawer;  //环绕点地绘画对象
    private Obj2DRectangle centralPtDrawer; //中心点的回话对象
    private PointsManager ptMg;

    private float speed;            //向右移动的总速度

    public RhythmTool(Context context,int x,int y,int w,int h) {
        super(context);
        onInit(x,y,w,h);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        lineDrawer = new Obj2DLine(0.2f,0,0,0, ShaderManager.getShader(6));
        surPtsDrawer = new Obj2DPoint(ShaderManager.getShader(7));
        ptMg = new PointsManager(x,y,w,h);
        centralPtDrawer = new Obj2DRectangle(ptMg.centralX,ptMg.centralY,
                                             ptMg.CW,ptMg.CH,
                                             ptMg.Ca,ptMg.Cr,ptMg.Cg,ptMg.Cb,
                                                ShaderManager.getShader(6));
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        float pts[] = new float[15];
        int pts_cnt=0;

        ptMg.go();
        tmpQueue = new LinkedBlockingQueue<points>(ptMg.linePtsQueue);

        while(!tmpQueue.isEmpty()) {
            points pt = tmpQueue.remove();
            pts[pts_cnt++] = pt.x;
            pts[pts_cnt++] = pt.y;
        }
        lineDrawer.setLinePoints(pts_cnt,pts);

        for(int i=0;i<ptMg.surPtsList.size();i++) {
            pts[i] = ptMg.surPtsList.get(i);
        }
        surPtsDrawer.setPoints(1,0,0,0,10,ptMg.surPtsList.size(),pts);
        surPtsDrawer.drawSelf();

        centralPtDrawer.setX(ptMg.centralX);
        centralPtDrawer.setY(ptMg.centralY);
        centralPtDrawer.drawSelf();
    }

    public class points {
        public int x,y;
        public points(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class PointsManager {
        public static final int dyLimit = 5;        //最小Y误差
        public Queue<points> linePtsQueue;         //存储节奏型的点数据
        public ArrayList<points> surPtsList;        //存储中心店旁边点的坐标信息

        public float Ca = 1,Cr = 0,Cg = 0,Cb = 0;   //中心点的颜色
        public float CW = 10,CH = 10;               //中心点的长宽
        public float centralX,centralY;
        public boolean centerUp;                    //中心点是上升还是下降
        public int centralRk;                       //中心点在数组中的序号信息
        public int uplineY;
        public int downlineY;

        public Random random = new Random();
        public float tantheta;                      //中心点进行移动角度的正切值
        public int borderX,borderY,borderWidth,borderHeight;

        public PointsManager(int x,int y,int w,int h) {
            this.borderX = x; this.borderY = y;
            this.borderWidth = w; this.borderHeight = h;

            int uplineY = (int)(y);     this.uplineY = uplineY;
            int downlineY = (int)(y+0.9f*h);    this.downlineY = downlineY;
            float ptSpanWidth = 0.8f*w/(numberLimit/2);

            tantheta = (downlineY-uplineY)/ptSpanWidth;

            int insertRk = (numberLimit+1)/2-1;
            this.centralRk = insertRk;

            for(int i=0;i<(numberLimit+1)/2;i++) {
                int upx = (int)(x+i*ptSpanWidth);
                int downx = (int)(upx+0.5*ptSpanWidth);
                if(i==insertRk) {   //向队列中插入两个中心点坐标
                    linePtsQueue.add(points(upx,uplineY));
                    linePtsQueue.add(points(upx,uplineY));
                    centerUp= false;
                }
                linePtsQueue.add(new points(upx,uplineY));
                if(i!=(numberLimit+1)/2-1)
                    linePtsQueue.add(new points(downx,downlineY));
            }

            centralX = x; centralY = y;

            for(int i=0;i<10;i++) {
                surPtsList.add(new points(centralX+random.nextFloat(10),    //生成随机化环绕点地坐标
                        centralY+random.nextFloat(10)));
            }

        }
        public void go() {      //进行所有点系统的移动
            int qsize = linePtsQueue.size();
            for(int i=0;i<qsize;i++) {
                if(i==this.centralRk) {
                    points qnode = linePtsQueue.remove();
                    linePtsQueue.remove();
                    if(this.centerUp) {
                        qnode.y-=speed*tantheta;
                    } else {
                        qnode.y += speed * tantheta;
                    }
                    if(this.centerUp && Math.abs(qnode.y-uplineY)>dyLimit) {
                        this.centerUp = !this.centerUp;
                    } else if(!this.centerUp && Math.abs(qnode.y-downlineY)>dyLimit){
                        this.centerUp = !this.centerUp;
                    }
                    linePtsQueue.add(qnode);
                    linePtsQueue.add(qnode);
                }
                points qnode = linePtsQueue.remove();
                qnode.x -= speed;
                if(qnode.x < borderX) {
                    qnode.x = borderWidth+qnode.x;
                }
                linePtsQueue.add(qnode);
            }
            for(int i=0;i<surPtsList.size();i++) {
                surPtsList.get(i).y+=speed*tantheta;

                int flag = (random.nextFloat()<0.5?) -1:1;
                surPtsList.get(i).x+=random.nextInt(4)*flag;
                flag = (random.nextFloat()<0.5?) -1:1;
                surPtsList.get(i).y+=random.nextInt(4)*flag;
            }

        }
    }
}
