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

public class NetMsgReceiver extends Thread {
    private static final String TAG = "NetMsgReceiver";
    public static final String SERVER_IP = "192.168.137.1";
    public static final long connectTimeLimit = 3000;
    public static final int SERVER_PORT = 9999;

    public DataInputStream din;
    public DataOutputStream dout;
    public Socket sc;

    boolean flag =  true;

    public NetMsgReceiver() {

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
                flag = true;
            } catch(Exception e) {
                e.printStackTrace();
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
        if(!connectWithServer()) return ;
        Log.e(TAG, "已经连接成功啦！！！！！！！ ");
        while(flag) {
            try {
                String msg = din.readUTF();
                //处理读来的返回信息
                if(msg.startsWith("<#CONNECT#>")) {
                    msg.substring(11);
                    msgSplits =  msg.split("#");
                    if(msg.equals("SUCCESS")) {
                        //client创建成功 //返回clockId和sessionID信息
                        int clockID = Integer.parseInt(msgSplits[0]);
                        int sessionID = Integer.parseInt(msgSplits[1]);

                    }
                } else if(msg.startsWith("<#DESTROY#>")) {
                   //当前的client被消灭，显示alertDialog并强制退出

                }
                else if(msg.startsWith("<#ERROR#>")) {       //服务器返回网络错误信息
                    msg.substring(9);
                    if(msg.equals("TYPE1")) {
                        //LEADER创建client失败
                    } else if(msg.equals("TYPE2")) {
                        //TEAMATE创建client失败

                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setFlag (boolean flag ) {
        this.flag = flag;
    }
}
