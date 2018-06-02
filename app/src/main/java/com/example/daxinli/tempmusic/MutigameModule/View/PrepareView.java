package com.example.daxinli.tempmusic.MutigameModule.View;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/6/1.
 * 准备view
 */

public class PrepareView extends BaseView {
    Obj2DRectangle bckBackGround;
    Obj2DRectangle btn_StartGame;
    Area arBtnStartGame;
    String btnStartGamePname = "btn_startGame_pre.png";
    Muti_SurfaceView mcontext;
    public PrepareView(Muti_SurfaceView mcontext,int type) {
        this.mcontext = mcontext;
        initView();
    }

    @Override
    public void initView() {
        TextureManager.loadingTexture(mcontext,43,1);
        arBtnStartGame = new Area();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX(),y = e.getY();
        if(SFUtil.isin(x,y,arBtnStartGame)) {
            //开始游戏点击触发事件
            mcontext.mcontext.myBinder.sendMessage("<#STARTGAME#>");
        }
        return false;
    }

    @Override
    public void drawView(GL10 gl) {
        bckBackGround.drawSelf();
        DrawUtil.drawRect(0,0,1920,1080, 0.2f,1,1,1);
        DrawUtil.drawBitmap(arBtnStartGame.x, arBtnStartGame.y,arBtnStartGame.width,arBtnStartGame.height,btnStartGamePname);
    }
}
