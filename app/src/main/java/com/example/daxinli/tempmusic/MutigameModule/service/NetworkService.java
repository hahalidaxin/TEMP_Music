package com.example.daxinli.tempmusic.MutigameModule.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;

//用来与服务器维持长期心跳的服务，同时负责与服务器进行数据交互，负责向
//activity发送广播
public class NetworkService extends Service {
    private static final String TAG = "NetworkService";
    public MyBinder myBinder = new MyBinder();
    public NetMsgReceiver receiver;
    public NetworkService() {
    }

    @Override
    public void onCreate() {
        //无论是start还是bind的方法都会被调用
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启新的网络线程
        receiver = new NetMsgReceiver(this);
        receiver.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }
    public class MyBinder extends Binder {
        public Service getServece() {
            //返回当前的service实例
            return NetworkService.this;
        }
        public void sendMessage(final int type, final String requestCode) {
            String finalCode="";
            //线程内部进行操作
            Log.e(TAG, "sendMessage: 这里已经发送了消息"+requestCode);
            switch(type) {
                case 0:     //新建一个房间
                    finalCode = "<#CONNECT#>LEADER#"+requestCode;
                    break;
                case 1:     //进入一个房间
                    finalCode = "<#CONNECT#>NORMAL#"+requestCode;
                    break;
                case 2:
                    finalCode = "<#CreateHome#> "+requestCode;
                    break;
                case 3:     //leader选择开始游戏
                    finalCode = "<#StartGame#>";
                    break;
                case 4:     //处理弹幕的发送
                    finalCode = "<#Danmu#> "+requestCode;
                    break;
                case 5:     //身为leader退出房间
                    finalCode = "<#EXIT#>"+requestCode; //退出需要提供clockID
                    break;
                case 6:     //身为teamate退出房间
                    finalCode = "<#TeamateExit#>";
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
            }
            final String finalCodetoSend = finalCode;
            //另开一个线程进行数据的发送
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        receiver.dout.writeUTF(finalCodetoSend);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
