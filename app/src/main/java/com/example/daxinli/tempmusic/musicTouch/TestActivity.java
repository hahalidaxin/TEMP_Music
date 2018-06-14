package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBackLayout;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBitmap;

import java.util.Random;

public class TestActivity extends AppCompatActivity{
    Random random = new Random();
    FloatBackLayout floatBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        intiView();
    }
    public void intiView() {
        floatBackLayout = (FloatBackLayout) findViewById(R.id.floatbackLayout_backg);
        for(int i=0;i<10;i++) {
            floatBackLayout.addFloatView(new FloatBitmap(this,0.2f,0.3f,R.drawable.icon_instru1_p1));
        }
        new Thread(new Runnable() {     //延时启动  //给View一个初始化的时间
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        floatBackLayout.startFloat();
                    }
                });
            }
        }).start();
        ((Button)findViewById(R.id.test_btn1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatBackLayout.startFloat();
            }
        });
        ((Button)findViewById(R.id.test_btn2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatBackLayout.endFloat();
            }
        });
    }
}
