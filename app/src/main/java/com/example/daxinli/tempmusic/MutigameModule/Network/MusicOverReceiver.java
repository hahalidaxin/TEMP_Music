package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.compose.MusicOverActivity;

/**
 * Created by Daxin Li on 2018/6/8.
 */

public class MusicOverReceiver extends BroadcastReceiver {
    public MusicOverActivity mcontext;
    public MusicOverReceiver(MusicOverActivity context) {
        mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if(msg.startsWith("<#MUSICOVERVIEW#>")) {
            msg = msg .substring(17);
            if(msg.startsWith("RECEIVED")) {
                if(mcontext.activityType==1)    //如果是组员
                    mcontext.onMusicReceived();
            } else if(msg.startsWith("NUMBER")) {
                String[] msgSplits = msg.substring(7).split("#");
                mcontext.setNumbertoShow(Integer.parseInt(msgSplits[0]));
            }
        }
    }
}
