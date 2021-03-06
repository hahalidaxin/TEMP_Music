package com.example.daxinli.tempmusic.util.screenscale;

/**
 * Created by Daxin Li on 2018/3/19.
 */

public class ScreenScaleUtil {
    static final float sHpWidth = 1920.0F;
    static final float sHpHeight = 1080.0F;
    static final float whHpRatio = 1.7777778F;
    static final float sSpWidth = 1080.0F;
    static final float sSpHeight = 1920.0F;
    static final float whSpRatio = 0.5625F;

    public ScreenScaleUtil() {
    }

    public static ScreenScaleResult calScale(float targetWidth, float targetHeight) {
        ScreenScaleResult result = null;
        ScreenOrien so = null;
        if (targetWidth > targetHeight) {
            so = ScreenOrien.HP;
        } else {
            so = ScreenOrien.SP;
        }
        float targetRatio;
        float ratio;
        float realTargetWidth;
        float realTargetHeight;
        float lcuX;
        float lcuY;
        //等比例缩放并剪裁

        if (so == ScreenOrien.HP) {
            targetRatio = targetWidth / targetHeight;
            if (targetRatio > 1.7777778F) {
                ratio = targetHeight / 1080.0F;
                realTargetWidth = 1920.0F * ratio;
                lcuX = (targetWidth - realTargetWidth) / 2.0F;
                lcuY = 0.0F;
                result = new ScreenScaleResult((int) lcuX, (int) lcuY, ratio, so);
            } else {
                ratio = targetWidth / 1920.0F;
                realTargetWidth = 1080.0F * ratio;
                lcuX = 0.0F;
                lcuY = (targetHeight - realTargetWidth) / 2.0F;
                result = new ScreenScaleResult((int) lcuX, (int) lcuY, ratio, so);
            }
        }
        System.out.println("de");
        if (so == ScreenOrien.SP) {
            targetRatio = targetWidth / targetHeight;
            if (targetRatio > 0.5625F) {
                ratio = targetHeight / 1920.0F;
                realTargetWidth = 1080.0F * ratio;
                lcuX = (targetWidth - realTargetWidth) / 2.0F;
                lcuY = 0.0F;
                result = new ScreenScaleResult((int) lcuX, (int) lcuY, ratio, so);
            } else {
                ratio = targetWidth / 1080.0F;
                realTargetWidth = 1920.0F * ratio;
                lcuX = 0.0F;
                lcuY = (targetHeight - realTargetWidth) / 2.0F;
                result = new ScreenScaleResult((int) lcuX, (int) lcuY, ratio, so);
            }
        }
        //result = new ScreenScaleResult (0,0,1, ScreenOrien.SP);
        return result ;
    }
}
