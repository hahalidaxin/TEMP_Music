package com.example.daxinli.tempmusic.util.effect.pathviewer;

/**
 * Created by DaxinLi on 2018/6/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by wuyr on 17-12-15 下午8:08.
 */

public class PathSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "PathSurfaceView";
    private volatile boolean isDrawing=true;
    private SurfaceHolder mSurfaceHolder;
    Context mcontext;
    private ArrayList<PathManager> pathManagerArrayList = new ArrayList<>();


    public PathSurfaceView(Context context) {
        this(context, null);
    }

    public PathSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mcontext = context;
        init();
        //new Thread(this).start();
    }
    public void init() {
        //setZOrderOnTop(true);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mSurfaceHolder.addCallback(this);
    }
    public void addNewPath(int width,int Mode,Path path) {
        PathManager pathManager = new PathManager();
        pathManager.setPath(path);
        pathManager.setLineWidth(width);
        pathManager.setMode(Mode);
        pathManagerArrayList.add(pathManager);
    }
    private static final int[] Rtime = {0,75,50,25,150,125,125,125};
    public void startAnimation(final Activity activity) {
        Log.e(TAG, String.format("startAnimation: %d",pathManagerArrayList.size()) );
        final ArrayList<PathManager> tmpPath = new ArrayList<>(pathManagerArrayList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int deltaTime = 25;
                for(int time = 0;time<150;time+=deltaTime) {     //time是开始动画的时间点
                    for(int i=0;i<8;i++) {
                        if(time == Rtime[i]) {
                            final int tmpi = i;
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int j=0;j<4;j++) {
                                        tmpPath.get(tmpi*4+j).startAnimation();
                                    }
                                }
                            });
                        }
                    }
                    try {
                        Thread.sleep(deltaTime);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    public void run() {
        Log.e(TAG, "run: ");
        while (isDrawing) {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas == null) return;
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            for(PathManager pathManager:pathManagerArrayList) {
                pathManager.startDraw(canvas);
            }

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        restart();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    private void restart() {
        isDrawing = true;
        new Thread(this).start();
    }

    private void stop() {
        isDrawing = false;
        if(pathManagerArrayList!= null) {
            for(PathManager pathManager:pathManagerArrayList) {
                pathManager.stop();
            }
        }
    }
}