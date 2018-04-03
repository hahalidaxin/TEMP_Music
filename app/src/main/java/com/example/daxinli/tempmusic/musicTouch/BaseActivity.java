package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Daxin Li on 2018/3/7.
 */

public class BaseActivity extends AppCompatActivity {
    private myApplication application;
    private BaseActivity mContext;

    protected void onCreate(Bundle savedInstanceState) {
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
