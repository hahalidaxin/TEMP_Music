package com.example.daxinli.tempmusic.musicTouch;

import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;
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
        initPathView();
    }

    PathSurfaceView pathSurfaceView;
    private void initPathView() {
        int pathCnt;
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.test_pathView);

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
                int lineWidth  = pathCnt<10?5:10;
                pathSurfaceView.addNewPath(lineWidth, type, npath);
                pathSurfaceView.addNewPath(lineWidth, type, n2path);

                paths.add(npath);
                pathCnt++;
            }
            pathCnt*=2;
            Log.e(TAG, String.format("init: %d",pathCnt));
            in.close();
            reader.close();
            long end = System.currentTimeMillis();
        } catch(Exception e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          pathSurfaceView.startAnimation(TestActivity.this);
                                      }
                                  }
                    );
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
