package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.AbHomeActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 */

public class HomeACReceiver extends BroadcastReceiver {
    AbHomeActivity mcontext;
    public HomeACReceiver(AbHomeActivity mcontext) {
        this.mcontext = mcontext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收到广播的处理  分解intent对于消息进行定位
        String type = intent.getStringExtra("type");
        switch(type) {
            case "CONNECT":
                int clockID = intent.getIntExtra("clockID",0);
                int sessionID = intent.getIntExtra("sessionID",0);
                mcontext.netWaitTolaunchActivity(true,clockID,sessionID);
                break;
            case "DESTROY":
                break;
            case "ERROR":
                String errorType = intent.getStringExtra("errortype");
                if(errorType.equals("DISCONNECT")) {
                    /*
                    if(mcontext instanceof CreateAHomeActivity) {
                        ((CreateAHomeActivity)mcontext).showAlerDialog("网络故障TAT","服务器好像睡着啦...请回退",0);
                    } else if(mcontext instanceof EnterAHomeActivity) {
                        ((EnterAHomeActivity)mcontext).showAlerDialog("网络故障TAT","服务器好像睡着啦...请回退",0);
                    }
                    */
                    mcontext.showAlerDialog("网络故障TAT","服务器好像睡着啦...请回退",0);
                } else if(errorType.equals("HOMEFAULT")) {
                    mcontext.netWaitTolaunchActivity(false,0,0);
                }
                break;
        }
    }
}
