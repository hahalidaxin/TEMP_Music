package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.object.Obj2DLine;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Daxin Li on 2018/5/3.
 * 多人游戏中的节奏型工具，用来显示节奏型
 */

public class RhythmTool extends BaseViewTool{
    private static final String TAG = "RhythmTool";
    private Obj2DLine lineDrawer;
    //private Obj2DPoint surPtsDrawer;  //环绕点地绘画对象
    private Obj2DRectangle centralPtDrawer; //中心点的回话对象1
    private Obj2DRectangle backGround;
    private PointsManager ptMg;

    private float borderX,borderY,borderWidth,borderHeight;
    private float[] lineformer = new float[30];
    private float[] linelater = new float[30];   //绘制线的两条的坐标信息
    private int lineFormerNum=0,lineLaterNum=0;

    private float speed = 5;            //向右移动的总速度

    public int drawLinePtsNum;

    public RhythmTool(Context context,int x,int y,int w,int h) {
        super(context);
        onInit(x,y,w,h);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        lineDrawer = new Obj2DLine(0.2f,0,0,0, ShaderManager.getShader(6));
        backGround = new Obj2DRectangle(x,y,w,h,0.3f,94/255,1.0f,129/255,ShaderManager.getShader(6));
        ptMg = new PointsManager(x,y,w,h);
        centralPtDrawer = new Obj2DRectangle(ptMg.centralX,ptMg.centralY,
                                             ptMg.CW,ptMg.CH,
                                             ptMg.Ca,ptMg.Cr,ptMg.Cg,ptMg.Cb,
                                                ShaderManager.getShader(6));
        centralPtDrawer.setHP(true);
        this.borderX = x; this.borderY = y;
        this.borderWidth = w; this.borderHeight = h;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        backGround.setHP(true);
        backGround.drawSelf();
        ptMg.go();
        Queue<points> tmpQueue = new LinkedBlockingQueue<points>(ptMg.linePtsQueue);

        getLinePtsArray(tmpQueue);
        lineDrawer.setColor(0.2f,0,0,1);
        lineDrawer.setLinePoints(this.lineFormerNum/2,this.lineformer);
        lineDrawer.drawSelf();

        lineDrawer.setColor(0.2f,0,0,0);
        lineDrawer.setLinePoints(this.lineLaterNum/2,this.linelater);
        lineDrawer.drawSelf();

        centralPtDrawer.setX(ptMg.centralX-ptMg.CW/2);
        centralPtDrawer.setY(ptMg.centralY-ptMg.CH/2);
        centralPtDrawer.drawSelf();
    }

    public void getLinePtsArray(Queue<points> tmpQueue) {        //添加边界节点，返回节点的位置数组
        float[] tmpPts = new float[30];
        int pts_cnt = 0,size;

        while(!tmpQueue.isEmpty()) {
            points pt = tmpQueue.remove();
            tmpPts[pts_cnt++] = pt.x;
            tmpPts[pts_cnt++] = pt.y;
        }

        //构造两条不同颜色线段的位置信息
        int line1cnt=0,line2cnt=0;
        size = pts_cnt;
        if(tmpPts[0]!=borderX) {
            lineformer[line1cnt++] =  this.borderX;
            int spanY = Math.abs((int)(tmpPts[1]-tmpPts[3]));
            int spanX = Math.abs((int)(tmpPts[0]-tmpPts[2]));
            float tx = tmpPts[1]>tmpPts[3]? tmpPts[0]-borderX : borderX-tmpPts[size-2]+borderWidth;
            lineformer[line1cnt++] =  borderY + borderHeight - spanY * (tx)/spanX;
        }
        int i;
        for(i=0;i<size;i+=2) {
            if(tmpPts[i]>=this.ptMg.centralX) break;
            lineformer[line1cnt++] = tmpPts[i];
            lineformer[line1cnt++] = tmpPts[i+1];
        }
        lineformer[line1cnt++] = ptMg.centralX;
        lineformer[line1cnt++] = ptMg.centralY;


        linelater[line2cnt++] = ptMg.centralX;
        linelater[line2cnt++] = ptMg.centralY;
        for(;i<size;i++) {
            linelater[line2cnt++] = tmpPts[i];
        }
        if(tmpPts[0]!=borderX) {
            linelater[line2cnt++] = borderX+borderWidth;
            linelater[line2cnt++] = lineformer[1];
        }
        this.lineFormerNum = line1cnt;
        this.lineLaterNum = line2cnt;
    }

    public class points {
        public int x,y,type;
        //type==1 标识中心点
        public points(int x,int y) {
            this.x = x;
            this.y = y;
            this.type=0;
        }
    }

    private class PointsManager {
        private static final String TAG = "PointsManager";
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

        public PointsManager(int x,int y,int w,int h) {
            this.borderX = x; this.borderY = y;
            this.borderWidth = w; this.borderHeight = h;

            uplineY = (int)(y);
            downlineY = (int)(y+h);
            float ptSpanWidth = w/(numberLimit/2);



            int insertRk = (numberLimit+1)/2-1;

            for(int i=0;i<(numberLimit+1)/2;i++) {
                int upx = (int)(x+i*ptSpanWidth);
                int downx = (int)(upx+0.5*ptSpanWidth);
                linePtsQueue.add(new points(upx,uplineY));

                if(linePtsQueue.size()==insertRk+1) {                      //初始时刻中心点的坐标
                    centralX = upx; centralY = uplineY;
                }
                if(i!=(numberLimit+1)/2-1)
                    linePtsQueue.add(new points(downx,downlineY));
            }
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
}
