package com.example.daxinli.tempmusic.thread;

import android.app.Activity;
import android.widget.Toast;

import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.LogUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Daxin Li on 2018/3/8.
 * 与服务器进行网络交互 传输数据
 */

public class Login_NetworkThread extends Thread {
    private boolean flag;
    private boolean pause;
    private Socket s;
    public DataInputStream din;
    public DataOutputStream dout;
    private String[] strs;
    private static final String TAG = "Login_NetworkThread";
    private Activity father;

    public Login_NetworkThread() {
    }

    @Override
    public void run() {
        this.flag = true;
        this.pause = false;
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(GameData.serverIP,GameData.sertVerPort),10000);
            LogUtil.i(TAG,"[INFO] 已经连接服务器");
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
        } catch(Exception e) {
            LogUtil.i(TAG,"[ERROR]连接服务器失败");    //如何进一步传参
            Toast.makeText(father, "咱家服务器崩了无法登陆，请返回(；´д｀)ゞ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //dout的readUTF是实时的，如果没有接受到就停止不进行下一步
        while(flag) {
            if (!pause) {
                try {
                    //根据不同的交互信息进行处理
                    String msg = din.readUTF();
                    if (msg != null) {
                        if (msg.startsWith("<#Login#>")) {                   //服务器返回用户登录信息 【检查逻辑放在服务器】 //1-正确 2-用户不存在 3-用户存在但不正确
                            GameData.login_InfoQ = msg.substring(9);        //协议：1- CON_1 2- CON_2
                            LogUtil.i(TAG, "[QUERYINFO] Login " + GameData.login_InfoQ);
                        } else if (msg.startsWith("<#Register#>")) {// 协议：1- CON_1 注册成功 2- CON_2 用户名已经被注册
                            GameData.register_infoQ = msg.substring(12);
                            LogUtil.i(TAG, "[QUERYINFO] Register" + GameData.register_infoQ);
                        }
                    }
                    //本次网络请求结束 解锁
                    synchronized (GameData.lock) {
                        if (GameData.lockNetworkThread) GameData.lockNetworkThread = false;
                    }
                } catch (Exception e) {
                    // TODO: 2018/3/16 错误处理用户信息提示
                    e.printStackTrace();
                }
            }
        }
        LogUtil.i(TAG,"[INFO] 断开服务器");
        try {
            if(din!=null) din.close();
            if(dout!=null) dout.close();
            if(s!=null) s.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public void setPause(boolean flag) {
        this.pause = flag;
    }
    public void setFather(Activity father) {this.father = father;}


}
