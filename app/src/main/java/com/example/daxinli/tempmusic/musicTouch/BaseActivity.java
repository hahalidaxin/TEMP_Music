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

    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏和虚拟按键
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

        if(application == null) {
            application = (myApplication) getApplication();
        }
        mContext = this;
        addActivity();
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
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }
}
