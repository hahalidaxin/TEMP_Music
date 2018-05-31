package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiGamingActivity extends BaseActivity {
    private static final String TAG = "MutiGamingActivity";
    private Muti_SurfaceView msurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        Intent intent = getIntent();
        String type = intent.getStringExtra(MutiGameActivity.MUTIGAMINGTYPE);
        msurfaceView = new Muti_SurfaceView(this,0);
        setContentView(msurfaceView);
    }
    public void initData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
    }
}
