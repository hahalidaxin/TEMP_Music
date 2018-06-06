package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/19.
 */
public abstract class BaseView
{
    public abstract void initView();
    public abstract boolean onTouchEvent(MotionEvent e);
    public abstract void drawView(GL10 gl);
    public abstract boolean getIsInit() ;
}