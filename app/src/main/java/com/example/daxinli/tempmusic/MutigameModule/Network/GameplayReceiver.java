package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.MutiPlayActivity;

/**
 * Created by Daxin Li on 2018/6/12.
 */

public class GameplayReceiver extends BroadcastReceiver {
    MutiPlayActivity mcontext;
    public GameplayReceiver(MutiPlayActivity context) {
        this.mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if(msg.startsWith("<#DESTROY#>")) {
            mcontext.showAlertDialog("!!!∑(ﾟДﾟノ)ノ","王八蛋老板黄鹤...",1);
        } else if(msg.startsWith("<#NETWORKDOWN#>")) {
            mcontext.showAlertDialog("555...", "我们的服务器牺牲了,请您回退TAT",1);
        } else if(msg.startsWith("<#MUTIPALYVIEW#>")) {

        }
    }
}
