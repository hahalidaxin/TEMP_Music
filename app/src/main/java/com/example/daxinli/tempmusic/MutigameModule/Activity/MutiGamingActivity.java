package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.example.daxinli.tempmusic.MutigameModule.Network.MutiGamingReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiGamingActivity extends BaseActivity {
    private static final String TAG = "MutiGamingActivity";
    private Muti_SurfaceView msurfaceView;
    public SoundManager sound;

    public NetworkService.MyBinder myBinder;
    public MutiGamingReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MutiGamingActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private int clockID;
    private int sessionID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sound = new SoundManager(this);
        setContentView(R.layout.activity_muti_gaming);
        Button btnstart = (Button)findViewById(R.id.btn1);
        Button btnend = (Button)findViewById(R.id.btn2);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.playMediaMusic(MutiGamingActivity.this,R.raw.piano_1,false);
            }
        });
        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.stopMediaMusic();
            }
        });
        /*
        initData();
        Log.e(TAG, "onCreate: 已经执行了");
        Intent intent = getIntent();
        int type = intent.getIntExtra("type",0);      //获取多人游戏加载的类型
        this.clockID = intent.getIntExtra("clockID",-1);
        this.sessionID = intent.getIntExtra("sessionID",-1);
        msurfaceView = new Muti_SurfaceView(this,type);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(msurfaceView);
        */
    }


    public void initData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(1920, 1080);
        sound = new SoundManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new MutiGamingReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.WAIT_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (MutiGamingActivity.this,NetworkService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onStartGame() { //当游戏开始的时候
        //切换view到gameView
        msurfaceView.startGame();
    }
}
