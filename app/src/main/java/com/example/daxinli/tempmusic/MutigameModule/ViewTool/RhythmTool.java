package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.util.Log;
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
        //surPtsDrawer = new Obj2DPoint(ShaderManager.getShader(7));
        ptMg = new PointsManager(x,y,w,h);
        centralPtDrawer = new Obj2DRectangle(ptMg.centralX,ptMg.centralY,
                                             ptMg.CW,ptMg.CH,
                                             ptMg.Ca,ptMg.Cr,ptMg.Cg,ptMg.Cb,
                                                ShaderManager.getShader(6));
        this.borderX = x; this.borderY = y;
        this.borderWidth = w; this.borderHeight = h;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        backGround.drawSelf();
        ptMg.go();
        Queue<points> tmpQueue = new LinkedBlockingQueue<points>(ptMg.linePtsQueue);

        float [] tmpPts = getLinePtsArray(tmpQueue);
        lineDrawer.setLinePoints(this.drawLinePtsNum/2,tmpPts);
        lineDrawer.drawSelf();

        centralPtDrawer.setX(10);
        centralPtDrawer.setY(10);
        centralPtDrawer.drawSelf();
    }

    public float[] getLinePtsArray(Queue<points> tmpQueue) {        //添加边界节点，返回节点的位置数组
        float[] tmpPts = new float[30];
        float[] pts = new float[30];
        int pts_cnt = 0,size;

        while(!tmpQueue.isEmpty()) {
            points pt = tmpQueue.remove();
            tmpPts[pts_cnt++] = pt.x;
            tmpPts[pts_cnt++] = pt.y;
        }

        size = pts_cnt; pts_cnt = 0;
        if(tmpPts[0]!=borderX) {
            pts[pts_cnt++] =  this.borderX;
            int spanY = Math.abs((int)(tmpPts[1]-tmpPts[3]));
            int spanX = Math.abs((int)(tmpPts[0]-tmpPts[2]));
            float tx = tmpPts[1]>tmpPts[3]? tmpPts[0]-borderX : borderX-tmpPts[size-2]+borderWidth;
            if(tmpPts[1]>tmpPts[3]) {
                Log.e(TAG, " pts1:pts3 "+Float.toString(pts[1])+" "+Float.toString(pts[3]));
                Log.e(TAG, "tx: "+Float.toString(tx));
            }
            pts[pts_cnt++] =  borderY + borderHeight - spanY * (tx)/spanX;
        }
        for(int i=0;i<size;i++) {
            pts[pts_cnt++] = tmpPts[i];
        }
        if(tmpPts[0]!=borderX) {
            pts[pts_cnt++] = borderX+borderWidth;
            pts[pts_cnt++] = pts[1];
        }
        this.drawLinePtsNum = pts_cnt;
        return pts;
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
        public static final float Ca = 1,Cr = 0,Cg = 0,Cb = 0;   //中心点的颜色
        public static final float CW = 10,CH = 10;               //中心点的长宽

        public Queue<points> linePtsQueue = new LinkedBlockingQueue<points>();         //存储节奏型的点数据
        public points[] linePts = new points[15];

        public float centralX,centralY;
        public int uplineY;
        public int downlineY;

        public Random random = new Random();
        public int borderX,borderY,borderWidth,borderHeight;

        public boolean isFloatEqual(float a,float b) {  //判断两个float是否近似相等
            return Math.abs(a-b)<EPS;
        }

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
                if(linePtsQueue.size()==insertRk+1) {                      //向队列中插入两个中心点坐标
                    points cPT = new points(upx,uplineY); cPT.type=1;
                    //linePtsQueue.add(cPT);
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

            qcnt=-1;
            for(int i=0;i<qsize;i++) {
                points qnode = linePts[i];
                points qn1=linePts[0],qn2=linePts[1];
                if(qnode.type==1) {
                    qnode.x+=speed;
                    int qj;
                    qn1 = linePts[(i-1 +qsize)%qsize];
                    qn2 = linePts[(i+1)%qsize];
                    if(qn1.x>qnode.x) qn1.x-=borderWidth;
                    if(qn2.x<qnode.x) qn2.x+=borderWidth;
                    if (!(qnode.x >= qn1.x && qnode.x <= qn2.x)) {
                        for (qj = i + 1; qj < qsize-1; qj++) {
                            qn1 = linePts[qj];
                            qn2 = linePts[qj + 1];
                            if (qnode.x >= qn1.x && qnode.x <= qn2.x && (qn1.type != 0 && qn2.type != 0))
                                break;
                        }
                    }
                    qnode.y = qn1.y + (qn2.y - qn1.y) * (qnode.x - qn1.x) / (qn2.x - qn1.x);
                    qnode.x -= speed;
                    centralX = qnode.x;
                } else {
                    qnode.x -= speed;;
                    centralY = qnode.y;
                }
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
            //Log.e(TAG, "queue的size是"+Integer.toString(linePtsQueue.size()));
            //if(linePtsQueue.isEmpty()) {
            //    Log.e(TAG, "qcnt是"+Integer.toString(qcnt));
            //    Log.e(TAG, "qsize是"+Integer.toString(qsize));
            //}
        }
    }
}
