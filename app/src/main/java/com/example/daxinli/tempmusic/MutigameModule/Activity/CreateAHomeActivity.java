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

import com.example.daxinli.tempmusic.MutigameModule.Network.HomeACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;

public class CreateAHomeActivity extends AbHomeActivity implements View.OnClickListener{
    private static final String TAG = "CreateAHomeActivity";
    public static final int HOMEMATELIMIT = 4;
    private EditText editText_homePassword;
    private Button btn_createYourHome;

    private NetworkService.MyBinder myBinder;
    private HomeACReceiver breceiver;
    private Intent serviceIntent;
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
    }
    //外部调用 针对创建房间返回的信息作出不同的调用处理
    public void netWaitTolaunchActivity(final boolean flag,final int clockID,final int sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    Intent intent = new Intent(CreateAHomeActivity.this,WaitActivity.class);
                    intent.putExtra("clockID",clockID);
                    intent.putExtra("sessionID",sessionID);
                    intent.putExtra("activityType",0);
                    startActivity(intent);
                } else {
                    CreateAHomeActivity.this.showAlerDialog("创建失败", "请更换您的密码", 2);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_createYourHome:
                String requestCode = editText_homePassword.getText().toString();
                myBinder.sendMessage("<#CONNECT#>LEADER#"+requestCode);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        breceiver = new HomeACReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.HOME_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        serviceIntent = new Intent (CreateAHomeActivity.this,NetworkService.class);
        startService(serviceIntent);
        bindService(serviceIntent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(connection);
        unregisterReceiver(breceiver);
        //动态注册 有注册就得有注销 否则会造成内存泄漏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //向service发送停止网络处理线程的信息
        myBinder.sendMessage("<#DESTROYTHREAD#>");
        stopService(serviceIntent);         //结束service
    }

    //alertDialog 提示框 ：网络交互信息 提示回退
    AlertDialog.Builder builder;
    public void showAlerDialog(String title,String Msg,final int type) {
        if(builder!=null) return ;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.setCancelable(false);
        if(type==1 || type==2) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(type==2) {
                        //取消重新连接网络 直接回退到上一个activity
                        CreateAHomeActivity.this.removeActivity();
                    }
                    builder = null;
                }
            });
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭线程 回退activity
                if(type==0||type==1) {
                    CreateAHomeActivity.this.removeActivity();
                } else if(type==2) {
                    //重新建立servece的net连接
                    myBinder.restartNetThread();
                }
                builder = null;
            }
        });
        builder.show();
    }
}
