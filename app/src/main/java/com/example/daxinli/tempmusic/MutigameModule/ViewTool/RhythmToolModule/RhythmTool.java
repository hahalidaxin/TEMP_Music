package com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.object.Obj2DLine;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Daxin Li on 2018/5/3.
 * 多人游戏中的节奏型工具，用来显示节奏型
 */

public class RhythmTool extends BaseViewTool {
    private static final String TAG = "RhythmTool";
    public Context mcontext;
    public static final long refreshTimeSpan = 20;
    private Obj2DLine lineDrawer;
    //private Obj2DPoint surPtsDrawer;  //环绕点地绘画对象
    private Obj2DRectangle centralPtDrawer; //中心点的回话对象1
    private Obj2DRectangle backGround;
    public PointsManager ptMg;
    public RhythmToolThread mrhThread;
    private Area Ar;

    private float borderX,borderY,borderWidth,borderHeight;
    private float[] lineformer = new float[30];
    private float[] linelater = new float[30];   //绘制线的两条的坐标信息
    private int lineFormerNum=0,lineLaterNum=0;
    private int thistime;

    public int drawLinePtsNum;
    public boolean initFlag = false;

    public RhythmTool(Context context, Area area, int time) {
        thistime = time;
        this.mcontext = context;
        this.Ar = area;
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        lineDrawer = new Obj2DLine(0.2f,0,0,0, ShaderManager.getShader(6));
        backGround = new Obj2DRectangle(x,y,w,h,0.3f,94/255,1.0f,129/255,ShaderManager.getShader(6));
        backGround.setHP(true);
        backGround.setX(x); backGround.setY(y);
        ptMg = new PointsManager(x,y,w,h,thistime);
        centralPtDrawer = new Obj2DRectangle(ptMg.centralX,ptMg.centralY,
                ptMg.CW,ptMg.CH,
                ptMg.Ca,ptMg.Cr,ptMg.Cg,ptMg.Cb,
                ShaderManager.getShader(6));
        centralPtDrawer.setHP(true);
        this.borderX = x; this.borderY = y;
        this.borderWidth = w; this.borderHeight = h;

        //mrhThread = new RhythmToolThread(this);
        //mrhThread.start();
    }
    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        if(!initFlag) {
            initFlag = true;
            onInit((int)Ar.x,(int)Ar.y,(int)Ar.width,(int)Ar.height);
        }
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
}
