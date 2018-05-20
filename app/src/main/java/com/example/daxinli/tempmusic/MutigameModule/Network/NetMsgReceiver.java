package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.Intent;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daxin Li on 2018/5/15.
 * 多人游戏的网络线程 具体负责与server的socket通信
 */

public class NetMsgReceiver extends Thread {
    public static final String HOME_AC_ACTION="com.example.daxinli.tempmusic.homeacaction";
    public static final String NORMAL_AC_ACTION="com.example.daxinli.tempmusic.normalacaction";
    public static final String WAIT_AC_ACTION="com.example.daxinli.tempmusic.waitacaction";
    private static final String TAG = "NetMsgReceiver";
    public static final String SERVER_IP = "192.168.137.1";
    public static final long connectTimeLimit = 3000;
    public static final int SERVER_PORT = 9999;

    public DataInputStream din;
    public DataOutputStream dout;
    public Socket sc;

    private NetworkService mcontext;
    private boolean broadCastDown;
    boolean flag =  true;

    public NetMsgReceiver(NetworkService mcontext) {
        this.mcontext = mcontext;
        this.broadCastDown = false;
    }
    public boolean connectWithServer() {
        boolean flag  = false;
        long beginTime = System.currentTimeMillis();
        //while(!flag) {                          //不断连接直到成功为止
            try {
                sc= new Socket();
                sc.connect(new InetSocketAddress(SERVER_IP,SERVER_PORT),1000);
                din = new DataInputStream(sc.getInputStream());
                dout = new DataOutputStream(sc.getOutputStream());
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
            /*
            if(System.currentTimeMillis()-beginTime>connectTimeLimit) {
                //连接失败处理问题
                Log.e(TAG, "connectWithServer: "+"some errors happended in connection");
                return false;        //这里退出Thread
            }
        }
        */
        return true;
    }
    @Override
    public void run() {
        String[] msgSplits;

        if(!connectWithServer()) {
            Intent mintent = new Intent();
            mintent.setAction(HOME_AC_ACTION);
            mintent.putExtra("msg","<#ERROR#>DISCONNECT");
            mcontext.sendBroadcast(mintent);
            this.setFlag(false);
        }
        Log.e(TAG, "服务器连接成功");
        while(flag) {
            try {
                String msg = din.readUTF();
                Log.e(TAG, "已经收到了发送来的信息 "+msg);
                //处理读来的返回信息
                try {
                    if(msg.startsWith("<#CONNECT#>")) {
                        Intent intent = new Intent();
                        intent.setAction(HOME_AC_ACTION);
                        intent.putExtra("msg",msg);
                        mcontext.sendBroadcast(intent);
                    } else if(msg.startsWith("<#DESTROY#>")) {
                        //当前的client被消灭，显示alertDialog并强制退出
                        Intent intent = new Intent();
                        intent.setAction(NORMAL_AC_ACTION);
                        intent.putExtra("msg",msg);
                        mcontext.sendBroadcast(intent);
                    } else if(msg.startsWith("<#DANMAKU#>")
                            || msg.startsWith("<#SHOWNUMBER#>")) {
                        Intent intent = new Intent();
                        intent.setAction(WAIT_AC_ACTION);
                        intent.putExtra("msg",msg);
                        mcontext.sendBroadcast(intent);
                    }
                }catch(Exception e) {
                    Log.e(TAG, "错误发生在分析字符串的额过程中");
                    e.printStackTrace();
                }
            } catch(Exception e) {
                //Log.e(TAG, "出现了错误");
                if(!broadCastDown) {
                    Intent intent = new Intent();
                    intent.setAction(NetMsgReceiver.NORMAL_AC_ACTION);
                    intent.putExtra("msg", "<#NETWORK_DOWN#>");
                    mcontext.sendBroadcast(intent);
                    broadCastDown = true;
                }
                e.printStackTrace();
                this.interrupt();
                this.setFlag(false);
            }
        }
    }
    public void setFlag (boolean flag ) {
        this.flag = flag;
    }
}
