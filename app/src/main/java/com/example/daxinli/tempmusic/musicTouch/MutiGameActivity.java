package com.example.daxinli.tempmusic.musicTouch;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.daxinli.tempmusic.MutigameModule.Activity.MutiGamingActivity;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.R;

public class MutiGameActivity extends BaseActivity implements View.OnClickListener{
    private Muti_SurfaceView muti_surfaceView;
    public static final String MUTIGAMINGTYPE="type";
    public static final String ENTERHOMETYPE="1";
    public static final String CREATEHOMETYPE="2";
    private Button btn_enterHome;
    private Button btn_createHome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_muti_game);
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
                intent = new Intent(MutiGameActivity.this,MutiGamingActivity.class);
                intent.putExtra(MUTIGAMINGTYPE,CREATEHOMETYPE);
                startActivity(intent);
                break;
            case R.id.btn_enterAHome:
                intent = new Intent(MutiGameActivity.this,MutiGamingActivity.class);
                intent.putExtra(MUTIGAMINGTYPE,ENTERHOMETYPE);
                startActivity(intent);
                break;
        }
    }
}
