package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daxin Li on 2018/5/15.
 * 多人游戏的网络线程 具体负责与server的socket通信
 */

public class Muti_NetworkThread extends Thread {
    private static final String TAG = "Muti_NetworkThread";
    public static final String SERVER_IP = "192.168.137.1";
    public static final long connectTimeLimit = 3000;
    public static final int SERVER_PORT = 9999;

    public DataInputStream din;
    public DataOutputStream dout;
    public Socket sc;

    boolean flag =  true;

    public Muti_NetworkThread() {

    }
    public boolean connectWithServer() {
        boolean flag  = false;
        long beginTime = System.currentTimeMillis();
        while(!flag) {                          //不断连接直到成功为止
            try {
                sc= new Socket();
                sc.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT),1000);
                din = new DataInputStream(sc.getInputStream());
                dout = new DataOutputStream(sc.getOutputStream());
                flag = true;
            } catch(Exception e) {
                e.printStackTrace();
            }
            if(System.currentTimeMillis()-beginTime>connectTimeLimit) {
                //连接失败处理问题
                Log.e(TAG, "connectWithServer: "+"some errors happended in connection");
                return false;        //这里退出Thread
            }
        }
        return true;
    }
    @Override
    public void run() {
        if(!connectWithServer()) return ;
        Log.e(TAG, "已经连接成功啦！！！！！！！ ");
        while(flag) {
            try {
                dout.writeUTF("这是一个用来交互的信息");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
