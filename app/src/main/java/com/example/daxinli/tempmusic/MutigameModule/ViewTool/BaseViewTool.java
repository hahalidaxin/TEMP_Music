package com.example.daxinli.tempmusic.MutigameModule.ViewTool;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Daxin Li on 2018/5/3.
 */

public abstract class BaseViewTool {
    public int viewX,viewY,viewWidth,viewHeight;
    public Context mcontext;
    public BaseViewTool(Context context) {
        this.mcontext = context;
    }
    public void onInit(int x,int y,int w,int h) {       //设置view的位置以及大小属性
        this.viewX = x;
        this.viewY = y;
        this.viewHeight = h;
        this.viewWidth = w;
    }
    public void onDestroy() {}
    public boolean onTouch(MotionEvent event) {
        return true;
    }
    public abstract void onDraw();
}
