package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.WaitACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiGamingActivity extends BaseActivity {
    private static final String TAG = "MutiGamingActivity";
    private Muti_SurfaceView msurfaceView;
    public SoundManager sound;

    public NetworkService.MyBinder myBinder;
    public WaitACReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WaitOtherPeopleActivity1.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        Log.e(TAG, "onCreate: 已经执行了");
        Intent intent = getIntent();
        int type = intent.getIntExtra("type");      //获取多人游戏加载的类型
        msurfaceView = new Muti_SurfaceView(this,type);
        setContentView(msurfaceView);
    }


    public void initData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
        sound = new SoundManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new WaitACReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.WAIT_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (WaitOtherPeopleActivity1.this,NetworkService.class);
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
