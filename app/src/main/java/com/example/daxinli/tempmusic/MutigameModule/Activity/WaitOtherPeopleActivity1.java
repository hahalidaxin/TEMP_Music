package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgSender;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;

public class WaitOtherPeopleActivity1 extends BaseActivity implements View.OnClickListener{
    Button btn_startGame;
    Button btn_sendDanmu;
    TextView text_teamateLinked;
    NetMsgSender netMsgSender;
    EditText editText_Danmu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait1);

        initView();
    }
    void initView() {
        btn_startGame = (Button) findViewById(R.id.btn_leader_startgame);
        text_teamateLinked = (TextView) findViewById(R.id.text_amout_peoplein);
        btn_sendDanmu = (Button) findViewById(R.id.btn_danmusend_createhome);
        editText_Danmu = (EditText) findViewById(R.id.edittext_danmu_createhome);

        btn_startGame.setOnClickListener(this);
        btn_sendDanmu.setOnClickListener(this);

        increaseNumPeapleLinked(1);

        netMsgSender = new NetMsgSender(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_leader_startgame:
                //leader决定开始游戏 开启一个activity activity用opengles实现
                netMsgSender.sendMessage(3,"");
                Intent intent  = new Intent(WaitOtherPeopleActivity1.this,MutiGamingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_danmusend_createhome:
                String requestCode = editText_Danmu.getText().toString();
                netMsgSender.sendMessage(4,requestCode);
                break;
        }
    }
    public void increaseNumPeapleLinked(int number) {   //外部调用增加人数 //外部调用需要在OnUIThread中使用
        if(number== MutiGameActivity.TEAMATENUMBETLIMIT) {
            text_teamateLinked.setText("连接人数已满，请开始游戏");
        } else {
            String str = "现在连接的人数："+Integer.toString(number);
            text_teamateLinked.setText(str);
        }
    }
}
