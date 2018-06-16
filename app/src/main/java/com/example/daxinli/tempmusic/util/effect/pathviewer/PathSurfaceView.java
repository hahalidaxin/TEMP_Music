package com.example.daxinli.tempmusic.util.effect.pathviewer;

/**
 * Created by DaxinLi on 2018/6/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by wuyr on 17-12-15 下午8:08.
 */

public class PathSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Object lock = new Object();
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
    public void setDuration(int duration) {
        for(PathManager pathmanager:pathManagerArrayList) {
            pathmanager.setAnimationDuration(duration);
        }
    }
    private int[] Rtime = {0,75,50,25,150,125,125,125};
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

    //从filename中获取所有线的位置信息 并创建manager
    public void initData(String fileName) {
        int pathCnt=0;
        BufferedReader reader = null;
        InputStream in = null;
        long start = System.currentTimeMillis();
        try {
            in = getResources().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String[] strs = reader.readLine().trim().split(" ");
            float imgW = Float.parseFloat(strs[0]);
            float imgH = Float.parseFloat(strs[1]);
            while((line=reader.readLine())!=null) {
                if(line.length()==0) continue;
                strs = line.trim().split(" ");
                int n = Integer.parseInt(strs[0].trim());
                int type = Integer.parseInt(strs[1].trim());
                Path npath = new Path();
                Path n2path = new Path();

                for(int i=0;i<n;i++) {
                    strs = reader.readLine().trim().split(" ");
                    float x = Float.parseFloat(strs[0]);
                    float y = Float.parseFloat(strs[1]);
                    x = (x/imgW*1080.0f);
                    y = (y/imgH*1920.0f);
                    if(i==0) {
                        npath.moveTo(x,y);
                        n2path.moveTo(1080.0f-x,y);
                    }
                    else {
                        npath.lineTo(x,y);
                        n2path.lineTo(1080.0f-x,y);
                    }
                }
                int lineWidth  = pathCnt<10?5:10;
                addNewPath(lineWidth, type, npath);
                addNewPath(lineWidth, type, n2path);
                pathCnt++;
            }
            pathCnt*=2;
            in.close();
            reader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        initFlag = true;
    }

    //开启电流线的自动刷新线程
    private boolean initFlag=false;
    public void startPathviewThread(final Activity activity) {
        if(!initFlag) return ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(refreshSpan);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    activity .runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          startAnimation(activity);
                                      }
                                  }
                    );
                    //处理需要对pathView更新的工作
                    while(!stack.empty()){
                        Intent item;
                        synchronized (lock) {
                            item = stack.pop();
                        }
                        int type = item.getIntExtra("type",-1);
                        if(type==0) {
                            int i=item.getIntExtra("i",-1);
                            int a = item.getIntExtra("a",-1);
                            int r = item.getIntExtra("r",-1);
                            int g = item.getIntExtra("g",-1);
                            int b = item.getIntExtra("b",-1);
                            tmodifColor(i, a, r, g, b);
                        } else if(type==1) {
                            int[] rtime = item.getIntArrayExtra("rtime");
                            modifyLineStartTime(rtime);
                        }
                    }
                }
            }
        }).start();
    }

    private Stack<Intent> stack = new Stack<>();
    public void modifyData(Intent intent) {
        synchronized (lock) { stack.push(intent); }
    }
    public void tmodifColor(int i,int a,int r,int g,int b) {
        pathManagerArrayList.get(i).setRGB(a,r,g,b);
    }
    public void modifyLineStartTime(int[] rime) {
        this.Rtime = rime;
    }
    private long refreshSpan = 2000L;
    public void setRefreshSpan(long span) {         //更改路线显示的间隔
        this.refreshSpan = span;
    }
    public int getMangerListSize() {
        return this.pathManagerArrayList.size();
    }
}