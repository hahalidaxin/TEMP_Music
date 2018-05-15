package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.app.Activity;

/**
 * Created by Daxin Li on 2018/5/15.
 * network工作的管理者 向client提供对server的操作函数
 */

public class NetMsgSender {
    Activity mcontext;
    Muti_NetworkThread netTH;
    public NetMsgSender(Activity activity) {
        this.mcontext = activity;
        netTH = new Muti_NetworkThread();
        netTH.start();
    }
    public void sendMessage(int type,String requestCode) {
        //处理具体的client向server发出的请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                String finalCode="";
                //线程内部进行操作
                switch(type) {
                    case 0:     //新建一个房间
                        finalCode = "<#CreateHome#> "+requestCode;
                        break;
                    case 1:     //进入一个房间
                        finalCode = "<#EnterHome#> "+requestCode;
                        break;
                    case 2:
                        finalCode = "<#CreateHome#> "+requestCode;
                        break;
                    case 3:     //leader选择开始游戏
                        finalCode = "<#StartGame#>"
                        break;
                    case 4:     //处理弹幕的发送
                        finalCode = "<#Danmu#> "+requestCode;
                        break;
                }
            }
        }).start();
    }
}
