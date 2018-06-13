package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.MutiGameResultActivity;

/**
 * Created by Daxin Li on 2018/6/12.
 */

public class MutiGameResultReceiver extends BroadcastReceiver{
    MutiGameResultActivity mcontext;
    public MutiGameResultReceiver(MutiGameResultActivity context) {
        this.mcontext = context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        String[] msgSplits ;
        if(msg.startsWith("<#MUTIRES#>")) {     //显示一个游戏玩家的结果
            msg = msg.substring(11);
            if (msg.startsWith("ADDRES")) {
                //乐器类型-游戏进度-最后得分
                msg = msg.substring(7);
                msgSplits = msg.split("#");
                int[] res = new int[3];
                for (int i = 0; i < 3; i++) {
                    res[i] = Integer.parseInt(msgSplits[i].trim());
                }
                mcontext.addResult(res[0], res[1], res[2]);
            } else if(msg.startsWith("GAMEOVER")) {     //游戏全部结束
                msg = msg.substring(9);
                mcontext.onGameOver(Integer.parseInt(msg));
            }
        } else if(msg.startsWith("<#DESTROY#>")) {
            //显示警示信息 强制确定 销毁当前组员的activity
            mcontext.ShowAlertDialog(
                    "o(▼皿▼メ;)o","房主带着他的小姨子和三点五个亿跑啦,啊啊... TAT",1);
        } else if(msg.startsWith("<#NETWORKDOWN#>")) {
            mcontext.ShowAlertDialog("555...","我们的服务器牺牲了，请您回退TAT",3);
        }
    }
}
