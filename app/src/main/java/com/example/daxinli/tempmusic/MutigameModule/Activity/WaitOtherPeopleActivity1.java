package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.WaitACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class WaitOtherPeopleActivity1 extends AbWaitActivity implements View.OnClickListener{
    private static final String TAG = "WaitOtherPeopleActivity";
    Button btn_startGame;
    Button btn_sendDanmu;
    TextView text_teamateLinked;
    EditText editText_Danmu;

    private NetworkService.MyBinder myBinder;
    private WaitACReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WaitOtherPeopleActivity1.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private boolean showDanmaku;
    private DanmakuContext danmakuContext;
    private DanmakuView danmakuView;
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    public int clockID;
    public int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait1);

        Intent intent = getIntent();
        clockID = Integer.parseInt(intent.getStringExtra("clockID"));
        sessionID = Integer.parseInt(intent.getStringExtra("sessionID"));     //保存作为一名成员的身份信息

        initView();
    }
    void initView() {
        btn_startGame = (Button) findViewById(R.id.btn_leader_startgame);
        text_teamateLinked = (TextView) findViewById(R.id.text_amout_peoplein);
        btn_sendDanmu = (Button) findViewById(R.id.btn_danmusend_createhome);
        editText_Danmu = (EditText) findViewById(R.id.edittext_danmu_createhome);
        danmakuView = (DanmakuView) findViewById(R.id.danmakuview_leader);

        btn_startGame.setOnClickListener(this);
        btn_sendDanmu.setOnClickListener(this);

        setNumbertoShow(1);

        //danmaku的初始化基本操作
       // netMsgSender = new NetMsgSender(this);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuView.start();
                showDanmaku = true;
                //generateSomeDanmaku();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser,danmakuContext);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_leader_startgame:
                //Intent intent  = new Intent(WaitOtherPeopleActivity1.this,MutiGamingActivity.class);
                //startActivity(intent);
                myBinder.sendMessage("<#STARTGAME#>");
                break;
            case R.id.btn_danmusend_createhome:
                HideKeyboard(editText_Danmu);
                String requestCode = editText_Danmu.getText().toString();
                myBinder.sendMessage("<#DANMAKU#>"+Integer.toString(clockID)+"#"+requestCode);
                editText_Danmu.setText("");
                break;
        }
    }
    //显示虚拟键盘
    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }
    //隐藏虚拟键盘
    public static void HideKeyboard(View v){
        InputMethodManager imm = ( InputMethodManager) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0 );
        }
    }
    public void onActivityTrans(int type) {
        //进行activity之间的切换
        Intent intent = null;
        switch(type) {
            case 0:
                //开始游戏
                //intent = new Intent(WaitOtherPeopleActivity1.this,)
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            ShowAlertDialog("黄鹤大人","您真的要解散该房间吗？",0);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(danmakuView!=null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }

        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(danmakuView!=null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if(danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }

    AlertDialog.Builder builder;
    public void ShowAlertDialog(final String tititle,final String msg,final int type) {     //用户想要退出显示警告信息框
        if(builder!=null) return ;
        builder = new AlertDialog.Builder(WaitOtherPeopleActivity1.this);
        builder.setTitle(tititle);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type==0) {
                    myBinder.sendMessage("<#EXIT#>"));
                    WaitOtherPeopleActivity1.this.removeActivity();
                } else if(type==3) {
                    WaitOtherPeopleActivity1.this.removeActivity();
                }
                builder = null;
            }
        });
        /*
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder = null;
            }
        });
        */
        builder.show();
    }
    public void addDanmaku(final String content,final boolean withBorder) {    //添加一条弹幕
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                danmaku.text = content;
                danmaku.padding = 5;
                danmaku.textSize = sp2px(50);      //有待修改
                danmaku.textColor = Color.BLACK;
                danmaku.setTime(danmakuView.getCurrentTime());
                if (withBorder) {
                    danmaku.borderColor = Color.GREEN;
                }
                danmakuView.addDanmaku(danmaku);
            }
        });
    }
    public void setNumbertoShow(final int number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WaitOtherPeopleActivity1.this.text_teamateLinked.setText(
                        String.format("找呀找呀找胖友...(%d/%d)",number, CreateAHomeActivity.HOMEMATELIMIT));
            }
        });
    }
    private float sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale*0.5f);
    }
    public int getclockID() {
        return this.clockID;
    }

}
