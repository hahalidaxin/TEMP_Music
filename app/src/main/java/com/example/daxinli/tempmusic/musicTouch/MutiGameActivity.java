package com.example.daxinli.tempmusic.musicTouch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.daxinli.tempmusic.MutigameModule.Activity.CreateAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.EnterAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;

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
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.mutigame_pathView);
        pathSurfaceView.initData("text/bliLine.txt");
        pathSurfaceView.startPathviewThread(this);
    }

}
