package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;

import com.example.daxinli.tempmusic.MutigameModule.Muti_SurfaceView;

public class MutiGameActivity extends BaseActivity {
    private Muti_SurfaceView muti_surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        muti_surfaceView = new Muti_SurfaceView(this);
        setContentView(muti_surfaceView);                       //设置多人模式的主要View
    }
}
