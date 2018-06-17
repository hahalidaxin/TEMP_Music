package com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.MutigameModule.Network.MutiGameResultReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;

public class MutiGameResultActivity extends AppCompatActivity {
    private static final String TAG = "MutiGameResultActivity";

    public NetworkService.MyBinder myBinder;
    public MutiGameResultReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MutiGameResultActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private int[] RID_Include = {R.id.include_res1,R.id.include_res2,R.id.include_res3,R.id.include_res4};
    private int[] DID_iconInstru = {R.drawable.icon_instru1_p1,R.drawable.icon_instru2_p1,R.drawable.icon_instru3_p1,R.drawable.icon_instru4_p1};
    private LinearLayout[] linear_result = new LinearLayout[4];
    private LinearLayout linear_finalRes;
    private int nowResNum=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_game_result);

        initView();
        //testInitView();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new MutiGameResultReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.MUTIGAMERECEIVE_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (MutiGameResultActivity.this,NetworkService.class);
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
                //不做反应
                break;
        }
        return true;
    }

    public void initView() {
        for(int i=0;i<4;i++) {
            linear_result[i] =(LinearLayout) findViewById(RID_Include[i]).findViewById(R.id.li_resultItem_mutires);
            linear_result[i].setVisibility(View.GONE);
        }
        linear_finalRes = (LinearLayout) findViewById(R.id.li_finalRes_mutires);
        linear_finalRes.setVisibility(View.GONE);

        View decorView = getWindow().getDecorView();
        decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(flag);
        }
    }
    private void testInitView () {
        for(int i=0;i<4;i++) {
            addResult(i,76,100);
        }
    }
    public void addResult(final int instruType, final int ratio, final int score) {
        final LinearLayout li = linear_result[nowResNum];
        new Thread(new Runnable() {
            @Override
            public void run() {
                int nowRatio = 0;
                while(nowRatio<ratio) {
                    final int tmpRatio = nowRatio;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ProgressBar) li.findViewById(R.id.progress_GameRatio_mutires)).setProgress(tmpRatio);
                        }
                    });
                    ++nowRatio;
                    long delayTime = 10+(long)((float)(nowRatio/ratio)*(nowRatio/ratio)*5.0f);
                    try {
                        Thread.sleep(delayTime);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //linear_result[nowResNum].findViewById(R.id.image_instru_mutires);
                ((TextView)li.findViewById(R.id.text_resScore_mutires)).setText("得分："+Integer.toString(score));
                Glide.with(MutiGameResultActivity.this).load(DID_iconInstru[instruType]).into((ImageView) li.findViewById(R.id.image_instru_mutires));
                linear_result[nowResNum++].setVisibility(View.VISIBLE);
            }
        });
    }
    public void onGameOver(final int finalScore) {  //当游戏已经全部结束
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView)findViewById(R.id.downAnchor_mutires)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.text_finalScore_mutires)).setText("小队得分："+Integer.toString(finalScore));
                ((Button)findViewById(R.id.btn_exitMutiGame)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Button按下需要进行activity的回退
                        Intent intent = new Intent(MutiGameResultActivity.this, MutiGameActivity.class);
                        startActivity(intent);
                    }
                });
                linear_finalRes.setVisibility(View.VISIBLE);
            }
        });
    }
    AlertDialog.Builder builder;            //用来处理多个builder重叠出现的情况
    public void ShowAlertDialog(final String tititle,final String msg,final int type) {     //用户想要退出显示警告信息框
        if(builder!=null) return ;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder = new AlertDialog.Builder(MutiGameResultActivity.this);
                builder.setTitle(tititle);
                builder.setMessage(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==1 || type==3) {
                            Intent intent = new Intent(MutiGameResultActivity.this,MutiGameActivity.class);
                            startActivity(intent);
                        }
                        builder = null;
                    }
                });
                builder.show();
            }
        });
    }
}
