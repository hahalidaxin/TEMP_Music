package com.example.daxinli.tempmusic.MutigameModule.View;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule.KeyboardView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/31.
 * 多人游戏中使用钢琴进行游戏制作的界面
 */

public class InstruView extends BaseView {
    Muti_SurfaceView mcontext;
    KeyboardView kbview;
    RhythmTool rhyview;
    Area areaKB;
    Area areaRHY;

    public int instruType;
    public InstruView(Muti_SurfaceView context,int instruType) {
        this.mcontext = context;
        this.instruType = instruType;
        initView();
    }

    @Override
    public void initView() {
        areaKB = new Area(10,500,540,300);
        areaRHY = new Area(10,10,540,200);
        kbview = new KeyboardView(mcontext,areaKB,instruType);
        rhyview = new RhythmTool(mcontext,areaRHY,50);
        TextureManager.loadingTexture(mcontext,27,13);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return kbview.onTouch(e);   //对触摸事件向下传递
    }

    @Override
    public void drawView(GL10 gl) {
        kbview.onDraw();
        rhyview.onDraw();
    }
}
