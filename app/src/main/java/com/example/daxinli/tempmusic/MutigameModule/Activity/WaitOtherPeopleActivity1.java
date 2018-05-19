package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;

import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class WaitOtherPeopleActivity1 extends BaseActivity implements View.OnClickListener{
    Button btn_startGame;
    Button btn_sendDanmu;
    TextView text_teamateLinked;
  //  NetMsgSender netMsgSender;
    EditText editText_Danmu;

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

        increaseNumPeapleLinked(1);

        //danmaku的初始化基本操作
       // netMsgSender = new NetMsgSender(this);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuView.start();
                showDanmaku = true;
                generateSomeDanmaku();
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
                //leader决定开始游戏 开启一个activity activity用opengles实现
               // netMsgSender.sendMessage(3,"");
                Intent intent  = new Intent(WaitOtherPeopleActivity1.this,MutiGamingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_danmusend_createhome:
                String requestCode = editText_Danmu.getText().toString();
              //  netMsgSender.sendMessage(4,requestCode);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            ShowAlertDialog();
            // TODO: 2018/5/19 需要通知客户端
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(danmakuView!=null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
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

    public void ShowAlertDialog() {     //用户想要退出显示警告信息框
        AlertDialog.Builder builder = new AlertDialog.Builder(WaitOtherPeopleActivity1.this);
        builder.setTitle(">_<");
        builder.setMessage("您是否确定退出当前房间");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //销毁activity 回退activity
               //WaitOtherPeopleActivity1.this.netMsgSender.sendMessage(5,"");
                WaitOtherPeopleActivity1.this.removeActivity();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    public void increaseNumPeapleLinked(int number) {   //外部调用增加人数 //外部调用需要在OnUIThread中使用
        if(number== MutiGameActivity.TEAMATENUMBETLIMIT) {
            text_teamateLinked.setText("连接人数已满，请开始游戏");
        } else {
            String str = "现在连接的人数："+Integer.toString(number);
            text_teamateLinked.setText(str);
        }
    }
    private void addDanmaku(String content,boolean withBorder) {    //添加一条弹幕
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
    private void generateSomeDanmaku() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                    while(showDanmaku) {
                        int time = new Random().nextInt(300);
                        String content  = ""+time+time;
                        addDanmaku(content,false);
                        try {
                            Thread.sleep(time);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();
    }
    private float sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale*0.5f);
    }

}
