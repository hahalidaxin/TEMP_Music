package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.AbHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.CreateAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.EnterAHomeActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 */

public class HomeACReceiver extends BroadcastReceiver {
    private static final String TAG = "HomeACReceiver";
    AbHomeActivity mcontext;
    public HomeACReceiver(AbHomeActivity mcontext) {
        this.mcontext = mcontext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收到广播的处理  分解intent对于消息进行定位
        String msg = intent.getStringExtra("msg");
        String[] msgSplits ;
        if(msg.startsWith("<#CONNECT#>")) {
            msg = msg.substring(11);
            msgSplits = msg.split("#");
            //创建或进入房间失败
            if (msg.startsWith("ERROR")) { //没有这个房间
                int type = msg.charAt(5)-'0'-1;
                mcontext.netWaitTolaunchActivity(type,false, 0, "");
            }
            else { //client创建成功 //返回clockId和sessionID信息
                int clockID = Integer.parseInt(msgSplits[0]);
                String sessionID = msgSplits[1];
                if(msgSplits.length==2)
                    mcontext.netWaitTolaunchActivity(0,true,clockID,sessionID);
                else if(msgSplits.length==3)
                    ((EnterAHomeActivity)mcontext).onActivityTrans(clockID,sessionID,msgSplits[2]);
            }
        } else if(msg.startsWith("<#ERROR#>")) {
            msg = msg.substring(9);
            if(msg.equals("DISCONNECT")) {
                mcontext.showAlerDialog("网络故障TAT","服务器好像睡着啦...请回退",0);
            }
        } else if(msg.startsWith("<#NETWORKDOWN#>")) {
            mcontext.showAlerDialog("(;´༎ຶД༎ຶ`)","我们的服务器挂了，请重新连接",2);
        } else if (msg.startsWith("<#CREATEVIEW#>")) {
            msg = msg.substring(14);
            if(mcontext instanceof CreateAHomeActivity) {
                ((CreateAHomeActivity) mcontext).onPlayerActivityTrans(msg);
            }
        }
    }
}
