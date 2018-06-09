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
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.daxinli.tempmusic.MutigameModule.Network.MusicOverReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;
import com.wang.avi.AVLoadingIndicatorView;

public class MusicOverActivity extends AppCompatActivity {
    public AVLoadingIndicatorView avi;

    public NetworkService.MyBinder myBinder;
    public MusicOverReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicOverActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        setContentView(R.layout.activity_music_over);
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
        initView();
        sendMusic();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new MusicOverReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.MUSICOVER_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent(MusicOverActivity.this,NetworkService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    public void initView() {
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi_sendMusic);
        avi.show();
    }
    public void sendMusic() {
        //在单独的线程中进行数据的发送
        new Thread(new Runnable() {
            @Override
            public void run() {
                //需要在msg外部对music的信息进行声明
                try {
                    Thread.sleep(3000);
                }catch(Exception e) {
                    e.printStackTrace();
                }
                Intent intent = getIntent();
                myBinder.sendMessage("<#MUSICOVERVIEW#>MUSICSENDED#"+intent.getStringExtra("msg"));
            }
        }).start();
    }
    public void onMusicReceived() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MusicOverActivity.this.avi.hide();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MusicOverActivity.this);
                builder.setTitle("ヾ(◍°∇°◍)ﾉﾞ");
                builder.setMessage("已上传文件到服务器");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MusicOverActivity.this, MutiGameActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }
}
