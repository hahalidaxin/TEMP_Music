package com.example.daxinli.tempmusic.util.effect.ProgressTool;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.DrawUtil;

/**
 * Created by Daxin Li on 2018/6/12.
 */

public class ProgressToolView extends BaseViewTool {
    private static final String TAG = "ProgressToolView";
    Context mcontext;
    public ProgressToolView(Context context) {
        super();
        this.mcontext = context;
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
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
        Log.e(TAG, String.format("onDraw: %f",GameData.gameProgressRatio));
        DrawUtil.drawRect(0,0, 1080*GameData.gameProgressRatio,10,0.8f,39/255f,1.0f,115/255f);
    }
}
