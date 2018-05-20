package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.R;

import java.util.Random;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class WaitOtherPeopleActivity2 extends AbWaitActivity implements View.OnClickListener {
    Button btn_sendDanmu;
    EditText editText_Danmu;
  //  NetMsgSender netMsgSender;
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

        initView();
    }
    public void initView() {
        btn_sendDanmu = (Button) findViewById(R.id.btn_danmusend_enterhome);
        editText_Danmu = (EditText) findViewById(R.id.edittext_danmu_enterhome);
        danmakuView = (DanmakuView) findViewById(R.id.danmakuview_teamate);

        btn_sendDanmu.setOnClickListener(this);
   //     netMsgSender = new NetMsgSender(this);

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            ShowAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_danmusend_enterhome:
                String requestCode = editText_Danmu.getText().toString();
         //       netMsgSender.sendMessage(4,requestCode);
                break;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(WaitOtherPeopleActivity2.this);
        builder.setTitle(">_<");
        builder.setMessage("您是否确定退出当前房间");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
         //       WaitOtherPeopleActivity2.this.netMsgSender.sendMessage(6,"");
                WaitOtherPeopleActivity2.this.removeActivity();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    public void onHomeisDismissed() {       //相应房间已经解散的情况
        AlertDialog.Builder builder = new AlertDialog.Builder(WaitOtherPeopleActivity2.this);
        builder.setTitle("o(▼皿▼メ;)o");
        builder.setMessage("房主欠下三点五个亿，带着他的小姨子跑了...");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             //   WaitOtherPeopleActivity2.this.netMsgSender.sendMessage(6,"");
                WaitOtherPeopleActivity2.this.removeActivity();
            }
        });
        builder.show();
    }
    public void addDanmaku(String content,boolean withBorder) {    //暴露添加弹幕的方法
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
                    addDanmaku(content,false);  //对于自己的发言可以加上一个border
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
    public int getclockID() {
        return this.clockID;
    }
}
