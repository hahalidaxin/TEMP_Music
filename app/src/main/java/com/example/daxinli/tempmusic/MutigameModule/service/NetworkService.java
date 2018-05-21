package com.example.daxinli.tempmusic.MutigameModule.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;

//用来与服务器维持长期心跳的服务，同时负责与服务器进行数据交互，负责向
//activity发送广播
public class NetworkService extends Service {
    private static final String TAG = "NetworkService";
    public static final long HEART_BEAT_RATE = 3000;
    public static final String HEART_BEAT_MESSAGE = "HEART_BEAT_MSG";
    public MyBinder myBinder = new MyBinder();
    public NetMsgReceiver receiver;

    private long lastSendTime;
    private Handler mHandler = new Handler();
    private Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis()-lastSendTime>=HEART_BEAT_RATE) {
                //定时间发送心跳连接 验证网络是否依然连接
                myBinder.sendMessage(HEART_BEAT_MESSAGE);
            }
            mHandler.postDelayed(mrunnable,HEART_BEAT_RATE);
        }
    };

    public NetworkService() {
    }

    @Override
    public void onCreate() {
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
        mHandler.removeCallbacks(mrunnable);    //移除心跳线程
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(receiver == null) {
            receiver = new NetMsgReceiver(this);
            receiver.start();
        }
        return myBinder;
    }
    public class MyBinder extends Binder {
        public Service getServece() {
            //返回当前的service实例
            return NetworkService.this;
        }
        public boolean sendMessage(final String requestCode) {
            Log.e(TAG, "SEND TO SERVER ："+requestCode);
            //另开一个线程进行数据的发送
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        receiver.dout.writeUTF(requestCode);
                        lastSendTime =System.currentTimeMillis();
                    } catch(Exception e) {
                        //处理网络连接错误 需要重新连接网络线程   //发送广播线程
                        Intent intent = new Intent();
                        intent.setAction(receiver.NORMAL_AC_ACTION);
                        intent.putExtra("msg","<#NETWORKDOWN#>");
                        sendBroadcast(intent);

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
        public void startHeartBeat() {      //开启心跳连接线程
            mHandler.postDelayed(mrunnable,HEART_BEAT_RATE);
        }
    }
}
