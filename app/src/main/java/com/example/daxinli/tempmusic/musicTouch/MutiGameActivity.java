package com.example.daxinli.tempmusic.musicTouch;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.daxinli.tempmusic.MutigameModule.Muti_SurfaceView;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiGameActivity extends BaseActivity {
    private Muti_SurfaceView muti_surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
