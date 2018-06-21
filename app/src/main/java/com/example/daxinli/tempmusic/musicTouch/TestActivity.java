package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBackLayout;

import java.util.Random;

public class TestActivity extends AppCompatActivity{
    private static final String TAG = "TestActivity";
    Random random = new Random();
    FloatBackLayout floatBackLayout;
    Button button;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

         button = (Button)findViewById(R.id.test_button);
         image = (ImageView) findViewById(R.id.test_phone);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Animation ani = AnimationUtils.loadAnimation(TestActivity.this,R.anim.animation_rotate);
                                    image.startAnimation(ani);
                                }
                            });
                            try {
                                Thread.sleep(2000);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
