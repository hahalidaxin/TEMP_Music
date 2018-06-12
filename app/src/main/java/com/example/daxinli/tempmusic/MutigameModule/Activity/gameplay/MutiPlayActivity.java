package com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.example.daxinli.tempmusic.MutigameModule.Network.GameplayReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;

public class MutiPlayActivity extends BaseActivity {
    public NetworkService.MyBinder myBinder;
    public GameplayReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MutiPlayActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_play);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new GameplayReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.MUTIGAMING_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (MutiPlayActivity.this,NetworkService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                myBinder.sendMessage("<#EXIT#>");
                myBinder.sendMessage("<#DESTROYTHREAD#>");
                showAlertDialog("_(:з」∠)_ ","正在退出房间",0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch(Exception e ) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(MutiPlayActivity.this,MutiGameActivity.class);
                        startActivity(intent);
                    }
                }).start();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showAlertDialog(final String title, final String msg, final int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //定时显示alert然后强制退出
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MutiPlayActivity.this);
                builder.setTitle(title);
                builder.setMessage(msg);
                builder.setCancelable(false);
                if(type!=0) {
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(type==1) {
                                Intent intent = new Intent(MutiPlayActivity.this,MutiGameActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                builder.show();
            }
        });
    }
}
