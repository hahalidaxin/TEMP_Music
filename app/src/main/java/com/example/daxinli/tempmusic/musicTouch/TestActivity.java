package com.example.daxinli.tempmusic.musicTouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.manager.SoundManager;

public class TestActivity extends AppCompatActivity{
    SoundManager sound;
    LinearLayout test_li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        intiView();
    }
    public void intiView() {
        sound = new SoundManager(this);
        sound.initSound();
        test_li = (LinearLayout) findViewById(R.id.test_li);
        test_li.setVisibility(View.INVISIBLE);
        test_li.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test_li.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}
