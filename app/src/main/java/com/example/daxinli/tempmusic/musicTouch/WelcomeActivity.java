package com.example.daxinli.tempmusic.musicTouch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.thread.Login_NetworkThread;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBackLayout;
import com.example.daxinli.tempmusic.view.floatbackground.FloatBitmap;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;


public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_GameStart;
    private Button btn_MutipleGame;
    private Button btn_GameSettings;
    private Button btn_GameExit;
    private Button btn_GameAbout;
    private FloatBackLayout floatBackLayout;
    private AVLoadingIndicatorView ani_Loading;
    public static SoundManager sound;
    private WelcomeActivity mActivity;
    private static final String TAG = "WelcomeActivity";
    private Handler handler;
    public Login_NetworkThread networkThread;
    private Random random = new Random();

    private int RID_picNote[] = {R.drawable.pic_note1,R.drawable.pic_note2,R.drawable.pic_note3,R.drawable.pic_note4,
            R.drawable.pic_note5,R.drawable.pic_note6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);          //设置全屏
        setContentView(R.layout.activity_welcome_loading);

        mActivity = this;

        //如果休眠主线程就会 使animation消失
        ani_Loading = (AVLoadingIndicatorView) findViewById(R.id.ani_loading);
        ani_Loading.show();

        initGame();

        handler = new Handler() {
            @Override
                public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 0:
                        //加载资源已经完成
                        setContentView(R.layout.activity_welcome);
                        sound.playMediaMusic(WelcomeActivity.this,R.raw.background,true);   //播放游戏的背景音乐
                        initViews();                            //setContentView之后调用获得View的实例
                        break;
                }
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (WelcomeActivity.sound.mp != null) {
                WelcomeActivity.sound.mp.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(floatBackLayout!=null) floatBackLayout.startFloat();
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (WelcomeActivity.sound.mp != null) {
                WelcomeActivity.sound.mp.pause();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(floatBackLayout!=null) floatBackLayout.endFloat();
    }
    public void initGame() {
        //开启新线程加载音乐
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        long ani_startTime = System.currentTimeMillis();
                        //networkThread = new Login_NetworkThread();    //初始化网络线程
                        //networkThread.start();
                        sound = new SoundManager(WelcomeActivity.this);
                        //补全游戏的开场动画
                        long ani_endTime = System.currentTimeMillis();
                        if(ani_endTime-ani_startTime< GameData.ani_Span) {
                            try {
                                Thread.sleep(GameData.ani_Span-(ani_endTime-ani_startTime));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(0);
                    }
                }
        ).start();
    }
    public void initViews() {
        btn_GameStart = (Button) findViewById(R.id.btn_GameStart);
        btn_MutipleGame = (Button) findViewById(R.id.btn_MutipleGame);
        btn_GameSettings = (Button) findViewById(R.id.btn_GameSettings);
        btn_GameExit= (Button) findViewById(R.id.btn_GameExit);
        floatBackLayout = (FloatBackLayout)findViewById(R.id.welcome_floatbackground);

        btn_GameStart.setOnClickListener(this);
        btn_GameExit.setOnClickListener(this);
        btn_GameSettings.setOnClickListener(this);
        btn_MutipleGame.setOnClickListener(this);

        floatBackLayout.setBackGround(this,R.drawable.pic_start_backg);
        for(int i=0;i<6;i++) {
            float posX = ((float)random.nextInt(100)/100.0f);
            float posY = ((float)random.nextInt(100)/100.0f);
            floatBackLayout.addFloatView(new FloatBitmap(this,posX,posY,RID_picNote[i]));
        }
        new Thread(new Runnable() {
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

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()) {
            case R.id.btn_GameStart:
                intent.setClass(this,GameActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btn_MutipleGame:
                intent.setClass(this,MutiGameActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btn_GameSettings:
                intent.setClass(this,SettingsActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btn_GameExit:
                removeAllActivity();
                System.exit(0);
                break;
        }
    }

}
