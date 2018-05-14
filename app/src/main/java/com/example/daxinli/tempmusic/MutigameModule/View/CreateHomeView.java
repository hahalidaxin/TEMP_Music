package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/10.
 */

public class CreateHomeView extends BaseView{
    private RhythmTool mrhTool;
    private Context mcontext;
    private Area

    public CreateHomeView(Context context) {
        this.mcontext = context;
        initView();
    }
    @Override
    public void initView() {
        mrhTool = new RhythmTool(mcontext,50,50,1000,1000,500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = e.getX(),y = e.getY();
        if()
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        mrhTool.onDraw();
    }
}
