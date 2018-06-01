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
import android.util.Log;
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

public class WaitOtherPeopleActivity2 extends AbWaitActivity implements View.OnClickListener {
    private static final String TAG = "WaitOtherPeopleActivity";
    Button btn_sendDanmu;
    EditText editText_Danmu;
    TextView text_peopletoShow;

    private NetworkService.MyBinder myBinder;
    private WaitACReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WaitOtherPeopleActivity2.this.myBinder = (NetworkService.MyBinder)service;
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
        //加载两种不同的视图
        setContentView(R.layout.activity_wait2);

        Intent intent = getIntent();
        clockID = intent.getIntExtra("clockID",-1);
        sessionID = intent.getIntExtra("sessionID",-1);
        if(clockID==-1) {
            Log.e(TAG, "获取sessionID时发生了错误");
        }

        initView();
    }
    public void initView() {
        btn_sendDanmu = (Button) findViewById(R.id.btn_danmusend_enterhome);
        editText_Danmu = (EditText) findViewById(R.id.edittext_danmu_enterhome);
        danmakuView = (DanmakuView) findViewById(R.id.danmakuview_teamate);
        text_peopletoShow = (TextView) findViewById(R.id.text_showPeople_wait2);


        btn_sendDanmu.setOnClickListener(this);

        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuView.start();
                showDanmaku = true;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            ShowAlertDialog(">_<","您是否确定退出当前房间",0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_danmusend_enterhome:
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
        Intent intent = new Intent (WaitOtherPeopleActivity2.this,NetworkService.class);
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
    public void ShowAlertDialog(final String titile,final String msg,final int type) {     //用户想要退出显示警告信息框
        if(builder != null) return ;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder = new AlertDialog.Builder(WaitOtherPeopleActivity2.this);
                builder.setTitle(titile);
                builder.setMessage(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //       WaitOtherPeopleActivity2.this.netMsgSender.sendMessage(6,"");
                        if(type==0||type==1||type==3) {
                            WaitOtherPeopleActivity2.this.removeActivity();
                            if(type==0) {
                                //向服务器发送退出的信息
                                myBinder.sendMessage("<#EXIT#>");
                            }
                        } else {
                        }
                        builder = null;
                    }
                });
                if(type==0) {
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder = null;
                        }
                    });
                }
                builder.show();
            }
        });
    }
    public void addDanmaku(final String content,final boolean withBorder) {    //暴露添加弹幕的方法
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                danmaku.text = content;
                danmaku.padding = 5;
                danmaku.textSize = sp2px(50);      //有待修改
                danmaku.textColor = Color.BLACK;
                danmaku.setTime(danmakuView.getCurrentTime());
                if(withBorder) {
                    danmaku.borderColor = Color.GREEN;
                }
                danmakuView.addDanmaku(danmaku);
            }
        });
    }
    public void onActivityTrans(int type) {
        //进行activity之间的切换
        Intent intent = null;
        switch(type) {
            case 0:
                //开始游戏
                intent = new Intent(WaitOtherPeopleActivity2,MutiGamingActivity.class);
                intent.putExtra("type",TYPE_NORMAL);
                startActivity(intent);
                break;
        }
    }
    public void setNumbertoShow(final int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WaitOtherPeopleActivity2.this.text_peopletoShow.setText(
                        String.format("Where Are Your?(%d/%d)",num,CreateAHomeActivity.HOMEMATELIMIT)
                );
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
