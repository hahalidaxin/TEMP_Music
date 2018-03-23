package com.example.daxinli.tempmusic.util;


import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.LoginActivity;
import com.example.daxinli.tempmusic.musicTouch.RegisterActivity;

import java.io.IOException;

/**
 * Created by Daxin Li on 2018/3/16.
 * 与network进行交互的金泰方法
 */

public class KeyThread {
    //外部调用keyThread内部函数的标志位
    //0-没有调用 1-Login 2-Register

    public KeyThread() {
    }

    //有了限定 : 如果需要调用KeyThread就必须持有Activity的引用

    private static final String TAG = "KeyThread";
    public static void broadcastLogin(final LoginActivity father, final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (GameData.lock) {
                        father.nt.dout.writeUTF("<#Login#>"+username+"#"+password);
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //查询用户登录信息
    }
    public static void broadcastRegister(final RegisterActivity Rt, final String username, final String password) {
        //进行用户注册
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Rt.nt.dout.writeUTF("<#Register#>"+username+"#"+password);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
