package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgSender;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

public class CreateAHomeActivity extends BaseActivity implements View.OnClickListener{
    EditText editText_homePassword;
    Button btn_createYourHome;
    NetMsgSender netMsgSender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ahome);

        initView();
    }
    public void initView() {
        editText_homePassword = (EditText) findViewById(R.id.editText_homepassword);
        btn_createYourHome = (Button) findViewById(R.id.btn_createYourHome);

        btn_createYourHome.setOnClickListener(this);

        netMsgSender = new NetMsgSender(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_createYourHome:
                String requestCode = "password "+editText_homePassword.getText().toString();
                netMsgSender.sendMessage(0,requestCode);
                Intent intent = new Intent(CreateAHomeActivity.this,WaitOtherPeopleActivity1.class);
                // TODO: 2018/5/15 与服务器进行交互
                startActivity(intent);
                break;
        }
    }
}
