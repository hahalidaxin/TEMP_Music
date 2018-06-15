package com.example.daxinli.tempmusic.musicTouch;

import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathView;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBackLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class TestActivity extends AppCompatActivity{
    private static final String TAG = "TestActivity";
    Random random = new Random();
    FloatBackLayout floatBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        View decorView = getWindow().getDecorView();
        decorView = getWindow().getDecorView();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
        init();
    }

    private int[] RID_pathView = {R.id.path_view1};/*, R.id.path_view2,R.id.path_view3,R.id.path_view4,
                    R.id.path_view5,R.id.path_view6,R.id.path_view7,R.id.path_view8,R.id.path_view9,R.id.path_view10,
            R.id.path_view11,R.id.path_view12,R.id.path_view13,R.id.path_view14,R.id.path_view15,R.id.path_view16
            ,R.id.path_view17,R.id.path_view18,R.id.path_view19,R.id.path_view20}; */
    PathView [] pathViews = new PathView[20];
    private int pathCnt;

    private void init() {
        for(int i=0;i<1;i++) {
            pathViews[i] = (PathView) findViewById(RID_pathView[i]);
        }

        ArrayList<Path> paths = new ArrayList<Path>();
        pathCnt=0;

        BufferedReader reader = null;
        InputStream in = null;
        long start = System.currentTimeMillis();
        try {
            in = getResources().getAssets().open("text/bliLine.txt");
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String[] strs = reader.readLine().trim().split(" ");
            float imgW = Float.parseFloat(strs[0]);
            float imgH = Float.parseFloat(strs[1]);
            Log.e(TAG, String.format("init: %f %f",imgW,imgH));
            while((line=reader.readLine())!=null) {
                if(line.length()==0) continue;
                strs = line.trim().split(" ");
                int n = Integer.parseInt(strs[0].trim());
                int type = Integer.parseInt(strs[1].trim());
                //paths.get(pathCnt) = new Path();
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
                pathViews[pathCnt].setPath(npath);
                pathViews[pathCnt].setLineWidth(5);
                pathViews[pathCnt].setMode(type);
                pathViews[pathCnt+10].setPath(n2path);
                pathViews[pathCnt+10].setLineWidth(5);
                pathViews[pathCnt].setMode(type);
                paths.add(npath);
                pathCnt++;
            }
            pathCnt*=2;
            Log.e(TAG, String.format("init: %d",pathCnt));
            in.close();
            reader.close();
            long end = System.currentTimeMillis();
            Log.e(TAG, String.format("initTIme: %d",end-start));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long start = System.currentTimeMillis();
                Log.e(TAG, "onTouchEvent: " );
                for(int i=0;i<pathCnt/2;i++) {
                    if(i==1 || i==4) {
                        long sleepTime=0;
                        if(i==1) sleepTime = 1000;
                        else if(i==4) sleepTime = 2000;
                        final long finalSleepTime=sleepTime;
                        final int finali = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(finalSleepTime);
                                }catch(Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pathViews[finali].startAnimation();
                                        //pathViews[finali+10].startAnimation();
                                    }
                                });
                            }
                        }).start();
                    } else {
                        pathViews[i].startAnimation();
                        //pathViews[i+10].startAnimation();
                    }
                }
                long end = System.currentTimeMillis();
                Log.e(TAG, String.format("onTouchEvent: %d",end-start));
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
