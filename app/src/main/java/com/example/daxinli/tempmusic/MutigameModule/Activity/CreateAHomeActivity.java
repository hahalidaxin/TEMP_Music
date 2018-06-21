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
import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.ChooseMusicActivity;
import com.example.daxinli.tempmusic.MutigameModule.Network.HomeACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;

public class CreateAHomeActivity extends AbHomeActivity implements View.OnClickListener{
    private static final String TAG = "CreateAHomeActivity";
    public static final int HOMEMATELIMIT = 4;
    private EditText editText_homePassword;
    private ActionProcessButton btn_createYourHome;
    private ActionProcessButton btn_mutiplaymusic;

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
    private int connectType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ahome);

        initPathView();
        initWork();
    }

    public void initWork() {
        editText_homePassword = (EditText) findViewById(R.id.editText_homepassword);
        btn_createYourHome = (ActionProcessButton) findViewById(R.id.btn_createYourHome);
        btn_mutiplaymusic= (ActionProcessButton) findViewById(R.id.btn_mutiplayMusic);

        btn_createYourHome.setMode(ActionProcessButton.Mode.ENDLESS);
        btn_mutiplaymusic.setMode(ActionProcessButton.Mode.ENDLESS);
        btn_createYourHome.setOnClickListener(this);
        btn_mutiplaymusic.setOnClickListener(this);
        connectType = 0;
    }
    //外部调用 针对创建房间返回的信息作出不同的调用处理
    public void netWaitTolaunchActivity(final int type ,final boolean flag,final int clockID,final String sessionID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag) {
                    if(connectType ==0) {
                        if(buttonPressed!= null)
                            buttonPressed.setProgress(100);        //按钮状态设置

                        Intent intent = new Intent(CreateAHomeActivity.this, WaitActivity.class);   //进入等待界面 此时需要提醒服务器端记性teamstate的修改
                        intent.putExtra("clockID", clockID);
                        intent.putExtra("sessionID", sessionID);
                        intent.putExtra("activityType", 0);
                        intent.putExtra("connectType",WaitActivity.CONNECT_COMPOSE);
                        myBinder.sendMessage("<#CREATEVIEW#>teamstate#0");
                        startActivity(intent);
                    } else if(connectType==1) {
                        //需要额外发送信息进行musiclist的获取
                        myBinder.sendMessage("<#CREATEVIEW#>MusicList");
                    }
                } else {
                    if(type==0) {
                        if(buttonPressed!= null)
                            buttonPressed.setProgress(-1);        //按钮状态设置
                        CreateAHomeActivity.this.showAlerDialog("创建失败", "请更换您的密码", 2);
                    }
                }
            }
        });
    }
    public void onPlayerActivityTrans(String msg) {
        if(buttonPressed!=null)
            buttonPressed.setProgress(100);
        Intent intent = new Intent(CreateAHomeActivity.this, ChooseMusicActivity.class);        //进入乐谱选择界面//这个时候其他组员依然不能够进入房间
        intent.putExtra("activityType",0);
        intent.putExtra("musicList",msg);
        startActivity(intent);
    }

    ActionProcessButton buttonPressed = null;
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_createYourHome:
                String requestCode = editText_homePassword.getText().toString();
                if(requestCode.length()==0) {
                    Animation ani_shake = AnimationUtils.loadAnimation(this,R.anim.animation_shake);
                    editText_homePassword.startAnimation(ani_shake);
                    return ;
                }
                connectType=0;
                myBinder.sendMessage("<#CONNECT#>LEADER#"+requestCode);
                btn_createYourHome.setProgress(75);
                buttonPressed = btn_createYourHome;
                break;
            case R.id.btn_mutiplayMusic:
                String msg = editText_homePassword.getText().toString();
                if(msg.length()==0) {
                    Animation ani_shake = AnimationUtils.loadAnimation(this,R.anim.animation_shake);
                    editText_homePassword.startAnimation(ani_shake);
                    return ;
                }
                connectType = 1;
                myBinder.sendMessage("<#CONNECT#>LEADER#"+msg);
                btn_mutiplaymusic.setProgress(75);
                buttonPressed = btn_mutiplaymusic;
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播//初始化广播接收器
        if(btn_createYourHome!=null) btn_createYourHome.setProgress(0);
        if(btn_mutiplaymusic!=null) btn_mutiplaymusic.setProgress(0);
        if(buttonPressed!=null) buttonPressed = null;

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
       // if(myBinder!=null)
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
                        if(buttonPressed!= null)
                            buttonPressed.setProgress(0);
                    }
                    builder = null;
                }
            });
        }
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(buttonPressed!=null) {
                    buttonPressed.setProgress(0);
                }
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

    PathSurfaceView pathSurfaceView;
    private void initPathView() {
        int pathCnt=0;
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.createhome_pathView);
        pathSurfaceView.initData("text/bliLine.txt");
        pathSurfaceView.startPathviewThread(this);
    }
}
