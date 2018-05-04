package com.example.daxinli.tempmusic.util.screenscale;

import com.example.daxinli.tempmusic.musicTouch.GameActivity;

/**
 * Created by Daxin Li on 2018/3/19.
 * 进行屏幕自适应坐标的计算
 */

public class Constant {
    static GameActivity mainActivity;
    //=======屏幕自适应数据=======start=======================================================//
    public static float SCREEN_WIDTH_STANDARD = 1080;
    public static float SCREEN_HEIGHT_STANDARD = 1920;
    public static float RATIO = SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
    public static ScreenScaleResult ssr;//屏幕自适应对象
    //=======屏幕自适应数据=======end=========================================================//


    public static float fromPixSizeToNearSize(float size)
    {
        return size*2/SCREEN_HEIGHT_STANDARD;
    }
    //屏幕x坐标到视口x坐标
    public static float fromScreenXToNearX(float x)
    {
        return (x-SCREEN_WIDTH_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
    }
    //屏幕y坐标到视口y坐标
    public static float fromScreenYToNearY(float y)
    {
        return -(y-SCREEN_HEIGHT_STANDARD/2)/(SCREEN_HEIGHT_STANDARD/2);
    }
    public static float fromScreenXToNearX_HP(float x) {
        return (x-SCREEN_HEIGHT_STANDARD/2)/(SCREEN_WIDTH_STANDARD/2);
    }
    public static float fromScreenYToNearY_HP(float y) {
        return -(y-SCREEN_WIDTH_STANDARD/2)/(SCREEN_WIDTH_STANDARD/2);
    }
    //实际屏幕x坐标到标准屏幕x坐标
    public static float fromRealScreenXToStandardScreenX(float rx)
    {
        return (rx-ssr.lucX)/ssr.ratio;
    }
    //实际屏幕y坐标到标准屏幕y坐标
    public static float fromRealScreenYToStandardScreenY(float ry)
    {
        return (ry-ssr.lucY)/ssr.ratio;
    }
    //从标准屏幕到实际屏幕x坐标
    public static float fromStandardScreenXToRealScreenX(float tx)
    {
        return tx*ssr.ratio+ssr.lucX;
    }
    //从标准屏幕到实际屏幕y坐标
    public static float fromStandardScreenYToRealScreenY2(float ty)
    {
        return ty*ssr.ratio-ssr.lucY;
    }
    public static float fromStandardScreenYToRealScreenY(float ty)
    {
        return ty*ssr.ratio+ssr.lucY;
    }
    public static float fromStandardScreenSizeToRealScreenSize(float size)
    {
        return size*ssr.ratio;
    }

}
