package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.daxinli.tempmusic.MutigameModule.Network.BroadCastReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;

public class CreateAHomeActivity extends AbHomeActivity implements View.OnClickListener{
    private EditText editText_homePassword;
    private Button btn_createYourHome;
    //NetMsgSender netMsgSender;
    private NetworkService.MyBinder myBinder;
    private BroadCastReceiver breceiver;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过binder建立activity与service之间的连接
            CreateAHomeActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ahome);

        initWork();
    }

    public void initWork() {
        editText_homePassword = (EditText) findViewById(R.id.editText_homepassword);
        btn_createYourHome = (Button) findViewById(R.id.btn_createYourHome);

        btn_createYourHome.setOnClickListener(this);

        //初始化service
        Intent intent = new Intent (CreateAHomeActivity.this,NetworkService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }
    //外部调用 针对创建房间返回的信息作出不同的调用处理
    public void netWaitTolaunchActivity(final boolean flag,final int clockID,final int sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    Intent intent = new Intent(CreateAHomeActivity.this,WaitOtherPeopleActivity1.class);
                    intent.putExtra("clockID",Integer.toString(clockID));
                    intent.putExtra("sessionID",Integer.toString(sessionID));
                    startActivity(intent);
                } else {
                    //CreateAHomeActivity.this.showAlerDialog("创建失败", "请更换您的密码", 0);
                    show_Toast("请更换您的房间密码>_<");
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_createYourHome:
                String requestCode = editText_homePassword.getText().toString();
                myBinder.sendMessage(0,requestCode);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new BroadCastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.daxinli.tempmusic.networkBroadCastAction");
        registerReceiver(breceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(breceiver);
        //动态注册 有注册就得有注销 否则会造成内存泄漏
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
                //关闭线程 回退activity
                CreateAHomeActivity.this.removeActivity();
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
