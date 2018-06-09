package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.MutiGamingActivity;

/**
 * Created by Daxin Li on 2018/6/1.
 */

public class MutiGamingReceiver extends BroadcastReceiver {
    MutiGamingActivity mcontext;
    public MutiGamingReceiver(MutiGamingActivity context) {
        mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if(msg.startsWith("<#MUTIGAMING#>")) {
            msg = msg.substring(13);
            if(msg.startsWith("STARTGAME")) {
                //开始游戏
                mcontext.onStartGame();
            }
        } else if(msg.startsWith("<#DESTROY#>")) {
            mcontext.onUItoShow(0);
        }
    }
}
