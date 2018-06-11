package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.Composition.GWaitActivity;


/**
 * Created by Daxin Li on 2018/6/10.
 */

public class GWaitReceiver extends BroadcastReceiver {
    GWaitActivity mcontext;
    public GWaitReceiver(GWaitActivity context) {
        this.mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if(msg.startsWith("")) {

        }
    }
}
