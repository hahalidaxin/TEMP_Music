package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.R;

public class EnterAHomeActivity extends AbHomeActivity implements View.OnClickListener {
    EditText editText_paswordtoEnter;
    Button btn_enterYourHome;
    //NetMsgSender netMsgSender;

    public int clockID;
    public int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ahome);

        initView();
    }
    public void initView() {
        editText_paswordtoEnter = (EditText) findViewById(R.id.editText_homepassword_toenter);
        btn_enterYourHome = (Button) findViewById(R.id.btn_enterYourHome);

        btn_enterYourHome.setOnClickListener(this);

        //netMsgSender = new NetMsgSender(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_enterYourHome:
                String requestCode = "pasword "+ editText_paswordtoEnter.getText().toString();
                //netMsgSender.sendMessage(1,requestCode);
                //可以实现异步等待
                break;
        }
    }
    public void netWaitTolaunchActivity(final boolean flag,final int clockID,final int sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    Intent intent = new Intent(EnterAHomeActivity.this,WaitOtherPeopleActivity2.class);
                    intent.putExtra("clockID",clockID);
                    intent.putExtra("sessionID",sessionID);
                    startActivity(intent);
                } else {
                    EnterAHomeActivity.this.showAlerDialog("进入失败", "没有找到匹配房间", 0);
                }
            }
        });
    }
    //alertDialog 提示框 ：网络交互信息 提示回退
    public void showAlerDialog(String title,String Msg,int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //结束线程 回退activity
                //EnterAHomeActivity.this.netMsgSender.setFlag(false);
                EnterAHomeActivity.this.removeActivity();
            }
        });
        if(type==1) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.show();
    }
}
