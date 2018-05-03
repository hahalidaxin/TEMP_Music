package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.object.Obj2DLine;

import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Daxin Li on 2018/5/3.
 * 多人游戏中的节奏型工具，用来显示节奏型
 */

public class rhythmTool extends BaseViewTool{
    private Queue<points> ptsQueue;
    private Queue<points> tmpQueue;
    private Obj2DLine lineDrawer;

    public rhythmTool(Context context) {
        super(context);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        ptsQueue = new LinkedBlockingQueue<points>();

        int uplineY = ((int) y+0.1f*h);
        int downlineY = int(y+0.9f*h);
        int numberLimit = 6;
        float ptSpanWidth = 0.8f*w/numberLimit;
        for(int i=0;i<numberLimit;i++) {
            int upx = int(this.viewX+i*ptSpanWidth);
            int downx = int(upx+0.5*ptSpanWidth);
            ptsQueue.add(points(upx,uplineY));
            if(i!=numberLimit-1)
                ptsQueue.add(points(downx,downlineY));
        }


    }

    @Override
    public void onTouch(MotionEvent event) {

    }

    @Override
    public void onDraw() {
        float pts[15] = new float[];
        int pts_cnt=0;
        tmpQueue = ptsQueue;

        while(!tmpQueue.isEmpty()) {
            pts[pts_cnt++] = tmpQueue.remove();
        }
        lineDrawer.setLinePoints(pts);
        lineDrawer.drawSelf();
    }

    public class points {
        public int x,y;
        public points(int x,int y) {
            this.x = x,this.y = y;
        }
    }
}
