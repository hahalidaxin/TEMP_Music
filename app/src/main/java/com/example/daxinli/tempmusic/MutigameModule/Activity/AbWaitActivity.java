package com.example.daxinli.tempmusic.MutigameModule.Activity;

import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

/**
 * Created by Daxin Li on 2018/5/19.
 */

public abstract class AbWaitActivity extends BaseActivity {
    public abstract void addDanmaku(String content, boolean withBorder);

    public abstract int getclockID();
}
