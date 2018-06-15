package com.example.daxinli.tempmusic.view.floatbackground;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.daxinli.tempmusic.util.SFUtil;

/**
 * Created by DaxinLi on 2018/6/14.
 */

public class FloatBitmap extends FloatObject {
    Activity activity;
    Bitmap bitmap;
    public FloatBitmap(Activity activity, float posX, float posY, int img) {
        super(posX, posY);
        this.activity = activity;

        //setAlpha(120);
        setColor(Color.WHITE);
        bitmap = SFUtil.resizeBitmap(((BitmapDrawable) activity.getResources().getDrawable(img))
                .getBitmap(),100,100);
        objHeight = 100; objWidth = 100;
    }

    @Override
    public void drawFloatObject(Canvas canvas, float x, float y, Paint paint) {
        int width = 40;
        canvas.drawBitmap(bitmap, x, y, paint);
    }
}
