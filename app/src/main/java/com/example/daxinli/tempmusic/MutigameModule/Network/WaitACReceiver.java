package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.AbWaitActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.WaitOtherPeopleActivity2;

/**
 * Created by Daxin Li on 2018/5/19.
 * 用来接收两个waitactivity返回msg的receiver
 */

public class WaitACReceiver extends BroadcastReceiver {
    private static final String TAG = "WaitACReceiver";
    public AbWaitActivity mcontext ;
    public WaitACReceiver(AbWaitActivity mcontext){
        this.mcontext = mcontext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        String[] msgSplits ;
        if(msg.startsWith("<#DANMAKU#>")) {
            msg = msg.substring(11);
            msgSplits = msg.split("#");
            boolean flag = false;
            //判断是否是自己发出的弹幕
            if(Integer.parseInt(msgSplits[0])==mcontext.getclockID()) {
                flag = true;
            }
            mcontext.addDanmaku(msgSplits[1],flag);
        } else if(msg.startsWith("<#SHOWNUMBER#>")) {
            msg = msg.substring(14);
            mcontext.setNumbertoShow(Integer.parseInt(msg));
        } else if(msg.startsWith("<#DESTROY#>")) {
            //显示警示信息 强制确定 销毁当前组员的activity
            ((WaitOtherPeopleActivity2)mcontext).ShowAlertDialog(1);
        }
    }
}
