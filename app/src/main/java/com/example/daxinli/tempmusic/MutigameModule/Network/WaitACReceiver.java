package com.example.daxinli.tempmusic.MutigameModule.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.Activity.Composition.WaitActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 * 用来接收两个waitactivity返回msg的receiver
 */

public class WaitACReceiver extends BroadcastReceiver {
    private static final String TAG = "WaitACReceiver";
    public WaitActivity mcontext ;
    public WaitACReceiver(WaitActivity mcontext){
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
            mcontext.ShowAlertDialog(
                    "o(▼皿▼メ;)o","房主带着他的小姨子和三点五个亿跑啦,啊啊... TAT",1);
        } else if(msg.startsWith("<#NETWORKDOWN#>")) {
            mcontext.ShowAlertDialog("555...","我们的服务器牺牲了，请您回退TAT",3);
        } else if(msg.startsWith("<#WAITVIEW#>")) {    //按下按钮 切换activity
            msg = msg.substring(12);
            msgSplits = msg.split("#");
            Log.e(TAG, "比较一下吧： " + Integer.toString(mcontext.getclockID()) + " " + msgSplits[0]);
            if(msg.startsWith("STARTGAME&MUSIC")) {
                msg = msg.substring(16);
                mcontext.onActivityTrans(0,msg);
            } else
            if (msg.startsWith("STARTGAME")) {
                msg = msg.substring(10);
                mcontext.onActivityTrans(0,msg);
            } else if(msg.startsWith("STARTFALSE1")) {
                mcontext.ShowAlertDialog("~~o(>_<)o ~~","组员乐器未选择",4);
            }
            else if(msgSplits[1].startsWith("INSTRU")) {
                if (msgSplits[1].equals("INSTRUFALSE")) {   //乐器选择失败
                    Log.e(TAG, "在这里产生崩溃");
                    mcontext.ShowAlertDialog("_(:з」∠)_ ", "该乐器已经被选择", 4);
                } else {    //乐器选择成功
                    int type = msgSplits[1].charAt(6) - '0';
                    int state = msgSplits[2].equals("SELECT") ? 1 : 0;
                    int flag = Integer.parseInt(msgSplits[0]) == mcontext.getclockID() ? 1 : 0;
                    mcontext.setInstruSelect(type,state,flag);
                }
            }
        }
    }
}
