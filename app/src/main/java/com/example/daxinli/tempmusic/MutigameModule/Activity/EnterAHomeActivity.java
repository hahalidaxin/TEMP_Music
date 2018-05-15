package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgSender;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

public class EnterAHomeActivity extends BaseActivity implements View.OnClickListener {
    EditText editText_paswordtoEnter;
    Button btn_enterYourHome;
    NetMsgSender netMsgSender;
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

        netMsgSender = new NetMsgSender(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_enterYourHome:
                String requestCode = "pasword "+ editText_paswordtoEnter.getText().toString();
                netMsgSender.sendMessage(1,requestCode);
                Intent intent = new Intent(EnterAHomeActivity.this,WaitOtherPeopleActivity2.class);
                startActivity(intent);
                break;
        }
    }
}
