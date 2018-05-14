package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/10.
 */

public class EnterHomeView extends BaseView {
    Context mcontext;
    public EnterHomeView(Context context) {
        this.mcontext = context;
        initView();
    }
    @Override
    public void initView() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return true;
    }

    @Override
    public void drawView(GL10 gl) {

    }
}
