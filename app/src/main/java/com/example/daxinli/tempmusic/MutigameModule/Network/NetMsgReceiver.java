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
    private static final String TAG = "NetMsgReceiver";
    public static final String SERVER_IP = "192.168.137.1";
    public static final long connectTimeLimit = 3000;
    public static final int SERVER_PORT = 9999;

    public DataInputStream din;
    public DataOutputStream dout;
    public Socket sc;

    private NetworkService mcontext;
    boolean flag =  true;

    public NetMsgReceiver(NetworkService mcontext) {
        this.mcontext = mcontext;
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
            mintent.setAction("com.example.daxinli.tempmusic.networkBroadCastAction");
            mintent.putExtra("type","ERROR");
            mintent.putExtra("errortype","DISCONNECT");
            mcontext.sendBroadcast(mintent);
        }
        /*
        try {
            String msg = din.readUTF();
            Log.e(TAG, "已经收到了信息"+msg);
        }catch(Exception e) {
            e.printStackTrace();
        }
        */
        Log.e(TAG, "服务器连接成功");
        //while(flag) {
            try {
                Log.e(TAG, "run: 我在这里等着一个msg的出现");
                String msg = din.readUTF();
                Log.e(TAG, "已经收到了发送来的信息 "+msg);
                //处理读来的返回信息
                try {
                    if(msg.startsWith("<#CONNECT#>")) {
                        msg = msg.substring(11);
                        msgSplits =  msg.split("#");
                        //创建或进入房间失败
                        if(msg.equals("ERROR1")) {
                            Intent intent = new Intent();
                            intent.setAction("com.example.daxinli.tempmusic.networkBroadCastAction");
                            intent.putExtra("type","ERROR");
                            intent.putExtra("errortype","HOMEFAULT");
                            mcontext.sendBroadcast(intent);
                        } else if(msg.equals("ERROR2")) {
                            Intent intent = new Intent();
                            intent.setAction("com.example.daxinli.tempmusic.networkBroadCastAction");
                            intent.putExtra("errortype","HOMEFAULT");
                            mcontext.sendBroadcast(intent);
                        } else {
                            //client创建成功 //返回clockId和sessionID信息
                            int clockID = Integer.parseInt(msgSplits[0]);
                            int sessionID = Integer.parseInt(msgSplits[1]);
                            Intent intent = new Intent();
                            intent.setAction("com.example.daxinli.tempmusic.networkBroadCastAction");
                            intent.putExtra("type","CONNECT");
                            intent.putExtra("clockID",clockID);
                            intent.putExtra("sessionID",sessionID);
                            mcontext.sendBroadcast(intent);
                        }

                    } else if(msg.startsWith("<#DESTROY#>")) {
                        //当前的client被消灭，显示alertDialog并强制退出
                        Intent intent = new Intent();
                        intent.setAction("com.example.daxinli.tempmusic.networkBroadCastAction");
                        intent.putExtra("type","DESTROY");
                        mcontext.sendBroadcast(intent);
                    }
                    else if(msg.startsWith("<#ERROR#>")) {       //服务器返回网络错误信息
                    }
                }catch(Exception e){
                    Log.e(TAG, "错误发生在分析字符串的额过程中");
                    e.printStackTrace();
                }
            } catch(Exception e) {
                Log.e(TAG, "出现了错误");
                e.printStackTrace();
            }
        //}
    }
    public void setFlag (boolean flag ) {
        this.flag = flag;
    }
}
