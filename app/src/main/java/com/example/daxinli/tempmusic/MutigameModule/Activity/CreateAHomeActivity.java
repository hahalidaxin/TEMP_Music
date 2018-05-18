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
    //外部调用 针对创建房间返回的信息作出不同的调用处理
    public void netWaitTolaunchActivity(final boolean flag,final int clockID,final int sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    Intent intent = new Intent(CreateAHomeActivity.this,WaitOtherPeopleActivity1.class);
                    intent.putExtra("clockID",clockID);
                    intent.putExtra("sessionID",sessionID);
                    startActivity(intent);
                } else {
                    showAlerDialog();
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_createYourHome:
                String requestCode = editText_homePassword.getText().toString();
                netMsgSender.sendMessage(0,requestCode);
                netWaitTolaunchActivity();
                break;
        }
    }

    @Override
    protected void onResume() {
        //netMsgSender = new NetMsgSender(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(netMsgSender!=null) {
            netMsgSender.setFlag(false);
        }
        super.onStop();
    }
    public void showAlerDialog() {

    }
}
