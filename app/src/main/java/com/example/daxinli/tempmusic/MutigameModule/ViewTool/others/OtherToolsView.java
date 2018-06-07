package com.example.daxinli.tempmusic.MutigameModule.ViewTool.others;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/6/8.
 */

public class OtherToolsView extends BaseViewTool {
    Muti_SurfaceView mcontext;
    Area Ar;
    Area areaBtnDone;
    Obj2DRectangle btnDone;
    boolean initFlag = false;
    public OtherToolsView(Muti_SurfaceView context, Area area) {
        this.mcontext = context;
        this.Ar = area;
        this.onInit(1,1,1,1);
    }

    @Override
    public void onInit(int x,int y,int width,int height) {
        areaBtnDone = new Area(1800,10,100,100);
        btnDone = new Obj2DRectangle(areaBtnDone.x,areaBtnDone.y,areaBtnDone.width,areaBtnDone.height, TextureManager.getTextures("down_right.png"),
                    ShaderManager.getShader(2));
        btnDone.setHP(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        float x = event.getX(),y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (SFUtil.isin(x, y, areaBtnDone)) {
                    //需要执行保存逻辑---异步通知instruView进行scmanager的onOver
                    mcontext.instruView.onMusicOver();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        if(!initFlag) {
            onInit(0,0,0,0);
            initFlag = true;
        }
        btnDone.drawSelf();
    }
}
