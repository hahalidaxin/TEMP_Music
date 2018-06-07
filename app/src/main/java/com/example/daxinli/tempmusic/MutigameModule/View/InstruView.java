package com.example.daxinli.tempmusic.MutigameModule.View;

import android.util.Log;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule.KeyboardView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.others.OtherToolsView;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
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
    Area areaBtnDone;
    Obj2DRectangle btnDone;

    public int instruType;
    public InstruView(Muti_SurfaceView context,int instruType) {
        this.mcontext = context;
        this.instruType = instruType;
        initView();
    }

    @Override
    public void initView() {
        areaKB = new Area(0,200,1920,880);
        areaRHY = new Area(560,10,800,200);
        areaOTH = new Area(0,0,1920,1080);
        TextureManager.loadingTexture(mcontext,29,16);
        kbview = new KeyboardView(mcontext,areaKB,instruType);
        rhyview= new RhythmTool(mcontext.mcontext,areaRHY,1000);
        othView = new OtherToolsView(mcontext,areaOTH);

        //areaBtnDone = new Area(10,10,800,800);
        //btnDone = new Obj2DRectangle(areaBtnDone.x,areaBtnDone.y,areaBtnDone.width,areaBtnDone.height, TextureManager.getTextures("down_right.png"),
        //        ShaderManager.getShader(2));

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
        Log.e(TAG, "结束对乐谱的技术 ");
        //kbview.scmanager.onMusicOver();
    }
}
