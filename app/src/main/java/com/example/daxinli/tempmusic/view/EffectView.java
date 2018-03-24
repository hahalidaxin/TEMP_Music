package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.util.effect.TriangleFirework.tri_ParticleSystem;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/23.
 */

public class EffectView extends BaseView {
    boolean initFlag = false;
    tri_ParticleSystem trisys;
    @Override
    public void initView() {
        trisys = new tri_ParticleSystem();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void drawView(GL10 gl) {
        if(!initFlag) {
            initView();
            initFlag = true;
        }
        //DrawUtil.drawNumber(200,20,(float) GameData.num_W,(float)GameData.num_H,GameData.GameScore);

        //Obj2DTriangle tri = new Obj2DTriangle(540f,1800f,50f,
        //                                                    1f,0f,0f,0f,
        //                                                        0f,1f, ShaderManager.getShader(6));
        //tri.drawSelf();
        trisys.drawSelf();
    }
}
