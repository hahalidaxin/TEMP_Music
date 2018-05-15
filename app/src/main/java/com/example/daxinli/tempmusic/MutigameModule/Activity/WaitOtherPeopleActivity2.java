package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.os.Bundle;
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
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_danmusend_enterhome:
                String requestCode = editText_Danmu.getText().toString();
                netMsgSender.sendMessage(4,requestCode);
                break;
        }
    }
}
