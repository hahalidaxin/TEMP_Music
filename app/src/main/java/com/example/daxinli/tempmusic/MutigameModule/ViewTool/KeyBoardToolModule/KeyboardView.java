package com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule;

import android.content.Context;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

/**
 * Created by Daxin Li on 2018/5/31.
 * 键盘的view类
 */

public class KeyboardView extends BaseViewTool {
    Obj2DRectangle imgKeyboard;
    Area imgArea;
    public KeyboardView(Context context,Area imgArea) {
        super(context);
        this.imgArea = imgArea;
        onInit(imgArea.x,imgArea.y,imgArea.width,imgArea,height);
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        imgKeyboard = new Obj2DRectangle(x,y,w,h,1f,0,0,0,
                ShaderManager.getShader(6));
        imgArea = new Area(x,y,w,h);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return super.onTouch(event);
    }

    @Override
    public void onDraw() {
        imgKeyboard.drawSelf();
    }
}

