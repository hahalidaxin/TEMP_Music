package com.example.daxinli.tempmusic.musicTouch;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.daxinli.tempmusic.MutigameModule.Activity.CreateAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.EnterAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MutiGameActivity extends BaseActivity implements View.OnClickListener{
    private Muti_SurfaceView muti_surfaceView;
    public static final String MUTIGAMINGTYPE="type";
    public static final String ENTERHOMETYPE="1";
    public static final String CREATEHOMETYPE="2";
    public static final int TEAMATENUMBETLIMIT = 4;
    private Button btn_enterHome;
    private Button btn_createHome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_game);
        initPathView();
        initView();
    }

    public void initView() {
        btn_createHome  = (Button) findViewById(R.id.btn_createAHome);
        btn_enterHome  = (Button) findViewById(R.id.btn_enterAHome);

        btn_enterHome.setOnClickListener(this);
        btn_createHome.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()) {
            case R.id.btn_createAHome:
                intent = new Intent(MutiGameActivity.this,CreateAHomeActivity.class);
                // TODO: 2018/5/15 向服务器发送开启房间的信息 
                // TODO: 2018/5/15 需要设置错误处理的情况
                startActivity(intent);
                break;
            case R.id.btn_enterAHome:
                intent = new Intent(MutiGameActivity.this,EnterAHomeActivity.class);
                // TODO: 2018/5/15 向服务器发送进入房间的信息 
                startActivity(intent);
                break;
        }
    }

    PathSurfaceView pathSurfaceView;
    private void initPathView() {
        int pathCnt=0;
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.mutigame_pathView);

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
                pathSurfaceView.addNewPath(lineWidth, type, npath);
                pathSurfaceView.addNewPath(lineWidth, type, n2path);
                pathCnt++;
            }
            pathCnt*=2;
            in.close();
            reader.close();
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
                                          pathSurfaceView.startAnimation(MutiGameActivity.this);
                                      }
                                  }
                    );
                }
            }
        }).start();
    }
}
