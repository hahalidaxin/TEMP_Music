package com.example.daxinli.tempmusic.MutigameModule.Activity;

import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 */

public abstract class AbHomeActivity extends BaseActivity {
    public abstract void showAlerDialog(String title,String Msg,final int type) ;
    public abstract void netWaitTolaunchActivity(final boolean flag,final int clockID,final int sessionID) ;
}
