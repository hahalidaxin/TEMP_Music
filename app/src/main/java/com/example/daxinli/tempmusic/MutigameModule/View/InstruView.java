package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Intent;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule.KeyboardView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.others.OtherToolsView;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/31.
 * 多人游戏中使用钢琴进行游戏制作的界面
 */

public class InstruView extends BaseView {
    private static final String TAG = "InstruView";
    private boolean isInit = false;
    Muti_SurfaceView mcontext;
    KeyboardView kbview;
    OtherToolsView othView;
    RhythmTool rhyview;
    Area areaKB;
    Area areaRHY;
    Area areaOTH;
    Intent mintent;

    public InstruView(Muti_SurfaceView context,Intent intent) {
        this.mcontext = context;
        this.mintent = intent;
        initView();
    }

    @Override
    public void initView() {
        areaKB = new Area(0,200,1920,880);
        areaRHY = new Area(460,10,1000,190);
        areaOTH = new Area(0,0,1920,1080);
        TextureManager.loadingTexture(mcontext,29,15);
        kbview = new KeyboardView(mcontext,areaKB,mintent);
        rhyview= new RhythmTool(mcontext.mcontext,areaRHY,60000/mintent.getIntExtra("BPM",-1));
        othView = new OtherToolsView(mcontext,areaOTH);

        this.isInit = true;     //直到资源加载完成才返回标志，此时才可以进行view的Draw绘制
    }

    @Override
    public boolean getIsInit() {
        return this.isInit;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        kbview.onTouch(e);   //对触摸事件向下传递
        othView.onTouch(e);
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        kbview.onDraw();
        rhyview.onDraw();
        //btnDone.drawSelf();
        othView.onDraw();
    }
    public void onMusicOver() {
        Intent intent = new Intent();
        int instruType = intent.getIntExtra("instruType",-1
        );
        String msg = String.format("%s#%d#%s",mintent.getStringExtra("musicName"),
               mintent.getIntExtra("instruType",-1),this.kbview.scmanager.getMusic());
        intent.putExtra("msg",msg);
        intent.putExtra("activityType",mintent.getIntExtra("activityType",-1));
        mcontext.mcontext.turnActivity(0,intent);
    }
}