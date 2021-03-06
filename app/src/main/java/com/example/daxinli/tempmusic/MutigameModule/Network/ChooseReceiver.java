package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.ChooseMusicActivity;

/**
 * Created by Daxin Li on 2018/6/10.
 */

public class ChooseReceiver extends BroadcastReceiver {
    ChooseMusicActivity mcontext;
    public ChooseReceiver(ChooseMusicActivity context) {
        mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if(msg.startsWith("<#CHOOSEVIEW#>")) {
            msg = msg.substring(14);
            if(msg.startsWith("INSTRUNUM")) {
                mcontext.onActivityTrans(msg.substring(10));
            }
        }else if(msg.startsWith("<#NETWORKDOWN#>")) {
            mcontext.ShowAlertDialog("555...", "我们的服务器牺牲了，请您回退TAT", 3);
        }
    }
}
