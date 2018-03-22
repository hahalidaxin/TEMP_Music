package com.example.daxinli.tempmusic.util.screenscale;

/**
 * Created by Daxin Li on 2018/3/19.
 */

public class ScreenScaleResult {
    public int lucX;
    public int lucY;
    public float ratio;
    ScreenOrien so;

    public ScreenScaleResult(int lucX, int lucY, float ratio, ScreenOrien so) {
        this.lucX = lucX;
        this.lucY = lucY;
        this.ratio = ratio;
        this.so = so;
    }

    public String toString() {
        return "lucX=" + this.lucX + ", lucY=" + this.lucY + ", ratio=" + this.ratio + ", " + this.so;
    }
}
