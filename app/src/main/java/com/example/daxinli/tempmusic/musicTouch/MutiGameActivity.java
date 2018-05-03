package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.daxinli.tempmusic.MutigameModule.Muti_SurfaceView;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiGameActivity extends BaseActivity {
    private Muti_SurfaceView muti_surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        muti_surfaceView = new Muti_SurfaceView(this);
        muti_surfaceView.requestFocus();
        muti_surfaceView.setFocusableInTouchMode(true);
        setContentView(muti_surfaceView);                       //设置多人模式的主要View
    }
    public void initData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
    }
}
