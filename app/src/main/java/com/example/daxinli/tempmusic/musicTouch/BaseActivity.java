package com.example.daxinli.tempmusic.musicTouch;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Daxin Li on 2018/3/7.
 */

public class BaseActivity extends AppCompatActivity {
    private myApplication application;
    private BaseActivity mContext;
    private Toast toast;

    protected void onCreate(Bundle savedInstanceState) {
        //透明状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == View.SYSTEM_UI_FLAG_VISIBLE) {
                    onWindowFocusChanged(true);
                }
            }
        });
        super.onCreate(savedInstanceState);

        if(application == null) {
            application = (myApplication) getApplication();
        }
        mContext = this;
        addActivity();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView = getWindow().getDecorView();
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
    }

    public void addActivity() {
        application.addActivity(mContext);
    }
    public void removeActivity() {
        application.removeActivity(mContext);
    }
    public void removeAllActivity() {
        application.RemoveAll();
    }
    public void show_Toast(String text) {   //捆绑实现方法Toast
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
