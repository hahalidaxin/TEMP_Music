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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.daxinli.tempmusic.MutigameModule.Activity.Composition.WaitActivity;
import com.example.daxinli.tempmusic.MutigameModule.Network.HomeACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;

public class EnterAHomeActivity extends AbHomeActivity implements View.OnClickListener {
    EditText editText_paswordtoEnter;
    ActionProcessButton btn_enterYourHome;
    //NetMsgSender netMsgSender;
    private NetworkService.MyBinder myBinder;
    private HomeACReceiver breceiver;
    private Intent serviceIntent;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //通过binder建立activity与service之间的连接
            EnterAHomeActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ahome);

        initPathView();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        if(btn_enterYourHome!=null) btn_enterYourHome.setProgress(0);
        breceiver = new HomeACReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.HOME_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        serviceIntent = new Intent (EnterAHomeActivity.this,NetworkService.class);
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
        myBinder.sendMessage("<#DESTROYTHREAD#>");
        stopService(serviceIntent);         //结束service
    }

    public void initView() {
        editText_paswordtoEnter = (EditText) findViewById(R.id.editText_homepassword_toenter);
        btn_enterYourHome = (ActionProcessButton) findViewById(R.id.btn_enterYourHome);

        btn_enterYourHome.setOnClickListener(this);

        //netMsgSender = new NetMsgSender(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_enterYourHome:
                String requestCode = editText_paswordtoEnter.getText().toString();
                if(requestCode.length()==0) {
                    Animation ani_shake = AnimationUtils.loadAnimation(this,R.anim.animation_shake);
                    editText_paswordtoEnter.startAnimation(ani_shake);
                    return ;
                }
                btn_enterYourHome.setProgress(75);
                myBinder.sendMessage("<#CONNECT#>NORMAL#"+requestCode);
                break;
        }
    }

    public void netWaitTolaunchActivity(final int type,final boolean flag,final int clockID,final String sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    if(btn_enterYourHome!=null) btn_enterYourHome.setProgress(100);
                    Intent intent = new Intent(EnterAHomeActivity.this,WaitActivity.class);
                    intent.putExtra("clockID",clockID);
                    intent.putExtra("sessionID",sessionID);
                    intent.putExtra("activityType",1);
                    intent.putExtra("connectType",WaitActivity.CONNECT_COMPOSE);    //任意
                    startActivity(intent);
                } else {
                    btn_enterYourHome.setProgress(-1);
                    if(type==0)
                        EnterAHomeActivity.this.showAlerDialog("!!!∑(ﾟДﾟノ)ノ", "没有该房间", 3);
                    else if(type==1)
                        EnterAHomeActivity.this.showAlerDialog("!!!∑(ﾟДﾟノ)ノ","房间人数已经满了...",3);
                    else if(type==2)
                        EnterAHomeActivity.this.showAlerDialog("!!!∑(ﾟДﾟノ)ノ","啥，已经开始游戏了...",3);
                    else if(type==3)
                        EnterAHomeActivity.this.showAlerDialog("!!!∑(ﾟДﾟノ)ノ","房主大人正在选择乐谱，请稍后再试...",3);
                }
            }
        });
    }
    public void onActivityTrans(int clockID,String sessionID,String msg) {
        Intent intent = new Intent(EnterAHomeActivity.this,WaitActivity.class);
        intent.putExtra("clockID",clockID);
        intent.putExtra("sessionID",sessionID);
        intent.putExtra("activityType",1);
        intent.putExtra("connectType",WaitActivity.CONNECT_COMPOSE);    //任意
        intent.putExtra("InstruNum",msg);
        startActivity(intent);
    }
    //alertDialog 提示框 ：网络交互 信息 提示回退
    AlertDialog.Builder builder;
    public void showAlerDialog(String title,String Msg,final int type) {
        if(builder!=null) return ;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //结束线程 回退activity
                //EnterAHomeActivity.this.netMsgSender.setFlag(false);
                if(type==0) {
                    EnterAHomeActivity.this.removeActivity();
                } else if(type==2) {
                    //重新建立servece的net连接
                    myBinder.restartNetThread();
                } else if(type==3) {

                }
                btn_enterYourHome.setProgress(0);
                builder = null;
            }
        });
        if(type==1||type==2) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(type==2) {
                        EnterAHomeActivity.this.removeActivity();
                    }
                    builder = null;
                }
            });
        }
        builder.show();
    }
    PathSurfaceView pathSurfaceView;
    private void initPathView() {
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.enterhome_pathView);
        pathSurfaceView.initData("text/bliLine.txt");
        pathSurfaceView.startPathviewThread(this);
    }
}
