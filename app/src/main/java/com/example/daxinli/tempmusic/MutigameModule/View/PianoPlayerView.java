package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule.KeyboardView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/5/31.
 * 多人游戏中使用钢琴进行游戏制作的界面
 */

public class PianoPlayerView extends BaseViewTool {
    Context mcontext;
    KeyboardView kbview;
    RhythmTool rhyview;
    Area areaKB;
    Area areaRHY;

    public PianoPlayerView(GLSurfaceView context) {
        super(context);
        this.mcontext = context;
        onInit(0,0,1920,1080);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        areaKB = new Area(10,500,540,300);
        areaRHY = new Area(10,10,540,200);
        kbview = new KeyboardView(mcontext,areaKB);
        rhyview = new RhythmTool(mcontext,areaRHY);
        TextureManager.loadingTexture(mcontext,27,13);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return kbview.onTouch(event);   //对触摸事件向下传递
    }

    @Override
    public void onDraw() {
        kbview.onDraw();
        rhyview.onDraw();
    }
}
