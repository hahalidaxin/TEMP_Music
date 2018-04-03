package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.object.Background;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.util.effect.RedHeart.RedHeart;
import com.example.daxinli.tempmusic.util.effect.TriangleFirework.tri_ParticleSystem;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/23.
 */

public class EffectView extends BaseView {
    public MySurfaceView mv;
    boolean initFlag = false;
    tri_ParticleSystem trisys;
    RedHeart redheart;
    Background background;
    MainSlide slide;
    float X=100,Y=100,Width=540,Height=720;
    public EffectView(MySurfaceView mv) {
        this.mv = mv;
    }
    @Override
    public void initView() {
        trisys = new tri_ParticleSystem(1,540,1000);
        redheart = new RedHeart(0,0,250,250,10f);
        background = new Background();
        TextureManager.loadingTexture(mv,0,18);
        slide = new MainSlide(X,  Y,  Width,  Height, 1, 1, "paino");
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        redheart.onTouch(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                background.switchBG();
                break;
        }
        if(e.getAction()== MotionEvent.ACTION_DOWN) {
            if (slide.state == 0) {
                slide.onTouchEvent(e);
            } else {
                slide = new MainSlide(X,  Y,  Width,  Height, 1, 1, "paino");
            }
        }
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        if(!initFlag) {
            initView();
            initFlag = true;
        }
        slide.drawSelf();
        //background.drawSelf();
    }
}
