package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.object.Obj2DLine;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Daxin Li on 2018/5/3.
 * 多人游戏中的节奏型工具，用来显示节奏型
 */

public class RhythmTool extends BaseViewTool{
    private static final int numberLimit = 7;
    private Queue<points> ptsQueue;
    private Queue<points> tmpQueue;
    private Obj2DLine lineDrawer;

    public RhythmTool(Context context) {
        super(context);
        onInit(0,0,1000,1000);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        ptsQueue = new LinkedBlockingQueue<points>();
        lineDrawer = new Obj2DLine(0.2f,0,0,0, ShaderManager.getShader(6));

        int uplineY = (int)(y);
        int downlineY = (int)(y+0.9f*h);

        float ptSpanWidth = 0.8f*w/(numberLimit/2);
        for(int i=0;i<(numberLimit+1)/2;i++) {
            int upx = (int)(this.viewX+i*ptSpanWidth);
            int downx = (int)(upx+0.5*ptSpanWidth);
            ptsQueue.add(new points(upx,uplineY));
            if(i!=(numberLimit+1)/2-1)
                ptsQueue.add(new points(downx,downlineY));
        }
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        float pts[] = new float[15];
        int pts_cnt=0;
        tmpQueue = new LinkedBlockingQueue<points>(ptsQueue);

        while(!tmpQueue.isEmpty()) {
            points pt = tmpQueue.remove();
            pts[pts_cnt++] = pt.x;
            pts[pts_cnt++] = pt.y;
        }
        lineDrawer.setLinePoints(numberLimit,pts);
        lineDrawer.drawSelf();
    }

    public class points {
        public int x,y;
        public points(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }
}
