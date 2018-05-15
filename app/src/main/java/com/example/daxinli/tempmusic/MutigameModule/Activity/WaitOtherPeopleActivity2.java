package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgSender;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

public class WaitOtherPeopleActivity2 extends BaseActivity implements View.OnClickListener {
    Button btn_sendDanmu;
    EditText editText_Danmu;
    NetMsgSender netMsgSender;
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

        btn_sendDanmu.setOnClickListener(this);
        netMsgSender = new NetMsgSender(this);
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
                netMsgSender.sendMessage(4,requestCode);
                break;
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
                WaitOtherPeopleActivity2.this.netMsgSender.sendMessage(6,"");
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
                WaitOtherPeopleActivity2.this.netMsgSender.sendMessage(6,"");
                WaitOtherPeopleActivity2.this.removeActivity();
            }
        });
        builder.show();
    }
}
