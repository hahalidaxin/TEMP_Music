package com.example.daxinli.tempmusic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.daxinli.tempmusic.util.elseUtil.Area;

/**
 * Created by Daxin Li on 2018/3/4.
 * 有关于surfaceView的Util类
 */

public class SFUtil {

    //判断xy是否在所指的area中
    public static boolean isin(float x,float y,Area area) {
        if(x>=area.x && x<=area.x+area.width && y>=area.y && y<=area.y+area.height) return true;
        return false;
    }

    //根据要求与素材在canvas上绘制需要的数字
    public static void drawNums(Canvas canvas, String drawNum, Bitmap[] nums, float x, float y) {
        int len = drawNum.length();
        canvas.save();
        canvas.translate(x,y);
        for(int i=0;i<len;i++) {
            canvas.drawBitmap(nums[drawNum.charAt(i)-'0'],0,0,null);
            canvas.translate(nums[0].getWidth(),0);
        }
        canvas.restore();
    }

    public static void drawLongSlide(Canvas canvas, float width, float height, float loadX, float loadY) {
        float radius = width/8;
        float CircleX = loadX+width/2;
        float CircleY = loadY+height*3/4;

        canvas.save();
        Paint paint = new Paint();
        paint.setColor(Color.argb(100,66,255,179));
        canvas.drawRect(loadX,loadY,loadX+width,loadY+height,paint);
        canvas.restore();

        canvas.save();
        paint.setColor(Color.argb(100,202,255,115));
        canvas.translate(CircleX,CircleY);
        canvas.drawCircle(0,0,radius,paint);
        canvas.restore();

        canvas.save();
        paint.setStrokeWidth(radius/4);
        canvas.drawLine(CircleX,CircleY,CircleX,CircleY-height/4*3,paint);
        canvas.restore();
    }

    public static void drawLongSlideEffect(Canvas canvas, float startX, float startY, float width, float height) {
        canvas.save();
        Paint paint = new Paint();
        paint.setColor(Color.argb(100,202,255,115));
        canvas.drawRect(startX,startY,startX+width,startY+height,paint);
        canvas.restore();
    }
}
