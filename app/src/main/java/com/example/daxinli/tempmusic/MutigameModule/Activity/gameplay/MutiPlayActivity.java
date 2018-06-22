package com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.example.daxinli.tempmusic.MutigameModule.Network.GameplayReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.HomeActivity;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

public class MutiPlayActivity extends BaseActivity {
    private static final String TAG = "MutiPlayActivity";
    public NetworkService.MyBinder myBinder;
    public GameplayReceiver breceiver;
    public MySurfaceView mySurfaceView;
    public static SharedPreferences.Editor editor;  //保存上次退出的保留
    public static SharedPreferences sp;
    public int instruType;
    public SoundManager sound;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MutiPlayActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    Intent mintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.mintent = getIntent();
        StringBuffer builder = new StringBuffer();
        String[] strs = mintent.getStringExtra("musicScore").split("\\$\\$");
        instruType = mintent.getIntExtra("instruType",-1);

        for(int i=0;i<strs.length;i++) {
            builder.append(strs[i]+"#");
        }
        GameData.mainMusicScore = builder;

        Log.e(TAG, "onCreate: "+GameData.mainMusicScore);
        //加载资源文件
        sound = new SoundManager(this);
        initScreenData();               //初始化屏幕数据 为后续计算对应点做准备
        loadSettings();

        mySurfaceView = new MySurfaceView(this);
        mySurfaceView.requestFocus();
        mySurfaceView.setFocusableInTouchMode(true);
        setContentView(mySurfaceView);                          //以SurfaceView作为主界面
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
                        Intent intent = new Intent(MutiPlayActivity.this,HomeActivity.class);
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
                                Intent intent = new Intent(MutiPlayActivity.this,HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                builder.show();
            }
        });
    }

    public void exit() {            //暴露退出函数
        this.removeActivity();
    }
    public void initScreenData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
        //System.out.println(Integer.toString(dm.widthPixels)+" "+Integer.toString(dm.heightPixels));
    }
    public void loadSettings() {
        sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        //获得音效设置
        boolean isMusicOn = sp.getBoolean("MUSICEFFECT",true);      //default Value
        GameData.GameEffect = isMusicOn;
    }
    public void exitActivity(int type) {
        if(type==0) {
            Intent intent = new Intent(MutiPlayActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }
    //将所有的通信操作都集成到MutiPlayActivity中，方便统一管理。\
    boolean changeFlag = false;
    public void onSendMessage(int type,Intent intent) {
        //1: gameover //2:gamevitory
        Log.e(TAG, "onSendMessage: ");
        switch(type) {
            case 1:
                if(!changeFlag) {
                    String ratio = Integer.toString(intent.getIntExtra("ratio", -1));
                    String score = Integer.toString(intent.getIntExtra("score", -1));
                    myBinder.sendMessage(String.format("<#MUTIPLAYVIEW#>GAMEOVER#%d#%s#%s" ,instruType,ratio,score));
                    Intent mintent = new Intent(MutiPlayActivity.this, MutiGameResultActivity.class);
                    startActivity(mintent);
                    changeFlag = true;
                }
                break;
            case 2:
                break;
        }
    }
}
