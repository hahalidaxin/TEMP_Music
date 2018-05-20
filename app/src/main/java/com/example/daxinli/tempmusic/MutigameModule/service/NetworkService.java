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
    public static final long HEART_BEAT_RATE = 3000;
    public MyBinder myBinder = new MyBinder();
    public NetMsgReceiver receiver;

    public NetworkService() {
    }

    @Override
    public void onCreate() {
        //无论是start还是bind的方法都会被调用
        Log.e(TAG, "onCreate: service onCreate发生");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启新的网络线程

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        this.receiver.interrupt();
        this.receiver.setFlag(false);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(receiver == null) {
            receiver = new NetMsgReceiver(this);
            receiver.start();
           // mHandler.postDelayed(mrunnable,HEART_BEAT_RATE);
        }
        Log.e(TAG, "onBind: service Bind发生");
        return myBinder;
    }
    public class MyBinder extends Binder {
        public Service getServece() {
            //返回当前的service实例
            return NetworkService.this;
        }
        public boolean sendMessage(final String requestCode) {
            Log.e(TAG, "向服务器发送消息："+requestCode);
            //另开一个线程进行数据的发送
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, "run: 这里发送了msg："+requestCode);
                        receiver.dout.writeUTF(requestCode);
                        //lastSendTime =System.currentTimeMillis();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return true;
        }
        public void restartNetThread() {    //负责重新启动网络线程
            if(receiver!=null) {
                receiver.interrupt();
                receiver.setFlag(false);
            }
            receiver = new NetMsgReceiver(NetworkService.this);
            receiver.start();
        }
    }
}
