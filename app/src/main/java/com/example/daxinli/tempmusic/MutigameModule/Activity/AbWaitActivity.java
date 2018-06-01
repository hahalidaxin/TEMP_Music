package com.example.daxinli.tempmusic.MutigameModule.Activity;

import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 */

public abstract class AbWaitActivity extends BaseActivity {
    static public final int TYPE_NORMAL = 0;
    static public final int TYPE_LEADER = 1;
    public abstract void addDanmaku(final String content, final boolean withBorder);

    public abstract int getclockID();

    public abstract void setNumbertoShow(final int text) ;

    public abstract void ShowAlertDialog(final String tititle,final String msg,final int type);

    public abstract void onActivityTrans(int type) ;
}
