package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.daxinli.tempmusic.MutigameModule.Network.MutiGamingReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;
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
        initData();
        Log.e(TAG, "onCreate: 已经执行了");
        Intent intent = getIntent();
        this.clockID = intent.getIntExtra("clockID",-1);
        this.sessionID = intent.getIntExtra("sessionID",-1);
        msurfaceView = new Muti_SurfaceView(this,intent);
        setContentView(msurfaceView);
    }


    public void initData() {
        sound = new SoundManager(this);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(1920, 1080);
        sound = new SoundManager(this);
        View decorView = getWindow().getDecorView();
        decorView = getWindow().getDecorView();
        //在生成surfaceView之前进行调用，防止后续opengl进行屏幕投影的时候出现问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new MutiGamingReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.MUTIGAMING_AC_ACTION);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                myBinder.sendMessage("<#EXIT#>");   //退出游戏标志
                Intent intent = new Intent(MutiGamingActivity.this,MutiGameActivity.class);
                startActivity(intent);
        }
        return super.onKeyDown(keyCode,event);
    }

    public void onStartGame() { //当游戏开始的时候
        //切换view到gameView
        msurfaceView.startGame();
    }
    public void onUItoShow(final int type) {
        if(type==0) {
            showAlertDialog("!!!∑(ﾟДﾟノ)ノ","王八蛋老板黄鹤...",0);
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
                            Intent intent = new Intent(MutiGamingActivity.this,MutiGameActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }).start();
        }
    }
    public void showAlertDialog(final String title,final String msg,final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //定时显示alert然后强制退出
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MutiGamingActivity.this);
                builder.setTitle(title);
                builder.setMessage(msg);
                builder.setCancelable(false);
                if(type!=0) {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                builder.show();
            }
        });
    }

    public void turnActivity(int type,Intent tint) {
        switch(type) {
            case 0:
                Intent intent = new Intent(MutiGamingActivity.this,MusicOverActivity.class);
                intent.putExtra("msg",tint.getStringExtra("msg"));
                intent.putExtra("activityType",tint.getIntExtra("activityType",-1));
                startActivity(intent);
                break;
        }
    }
}
