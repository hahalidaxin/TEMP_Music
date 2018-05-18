package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.app.Activity;

/**
 * Created by Daxin Li on 2018/5/15.
 * network工作的管理者 向client提供对server的操作函数
 */

public class NetMsgSender {
    Activity mcontext;
    public NetMsgReceiver netTH;

    public NetMsgSender(Activity activity) {
        this.mcontext = activity;
        netTH = new NetMsgReceiver();
        netTH.start();
    }
    public void sendMessage(final int type, final String requestCode) {
        String finalCode="";
        //线程内部进行操作
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
                    netTH.dout.writeUTF(finalCodetoSend);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void setFlag(boolean flag) {
        this.netTH.interrupt();
        this.netTH.setFlag(flag);
    }
}
