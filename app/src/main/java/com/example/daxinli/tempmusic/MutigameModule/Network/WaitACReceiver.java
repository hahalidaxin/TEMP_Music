package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.AbWaitActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 * 用来接收两个waitactivity返回msg的receiver
 */

public class WaitACReceiver extends BroadcastReceiver {
    public AbWaitActivity mcontext ;
    public WaitACReceiver(AbWaitActivity mcontext){
        this.mcontext = mcontext;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        switch(type) {

        }
    }
}
