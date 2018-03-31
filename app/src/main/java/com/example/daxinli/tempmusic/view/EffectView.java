package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.object.Background;
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
    public EffectView(MySurfaceView mv) {
        this.mv = mv;
    }
    @Override
    public void initView() {
        trisys = new tri_ParticleSystem(1,540,1000);
        redheart = new RedHeart(0,0,250,250,10f);
        background = new Background();
        TextureManager.loadingTexture(mv,0,18);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        redheart.onTouch(e);
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                background.switchBG();
                break;
        }
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        if(!initFlag) {
            initView();
            initFlag = true;
        }
        background.drawSelf();
    }
}
