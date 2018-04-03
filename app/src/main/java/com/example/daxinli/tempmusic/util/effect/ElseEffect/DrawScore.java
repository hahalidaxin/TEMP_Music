package com.example.daxinli.tempmusic.util.effect.ElseEffect;

import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/3/20.
 * 分数绘制类 能够提供分数改变动画
 */

public class DrawScore {
    static String[] numsF={"0.png","1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png",
                            "r0.png","r1.png","r2.png","r3.png","r4.png","r5.png","r6.png","r7.png","r8.png","r9.png"};
    static int[] idxplus = {0,1,2,3,2,1,0};
    int initWidth = 120;
    int initHeight = 200;
    int idx;                    //分数绘制的帧序号
    boolean startAnim;

    public DrawScore() {
        idx = 0;
        startAnim = false;
    }
    public void runAnim() {
        startAnim = true;
    }
    public void drawSelf(int gameScore) {
        if(startAnim == true) {
            idx++;
            if(idx == idxplus.length) {
                startAnim = false;
                idx = 0;
            }
        }
        String str_score = Integer.toString(gameScore);
        int length = str_score.length();
        float numwidth = initWidth + idxplus[idx]*5;
        float numheight = initHeight + idxplus[idx]*5;
        float loadX = (GameData.STANDARD_WIDTH-length*numwidth)/2;
        float loadY = 20;

        String str_num = Integer.toString(gameScore);
        for(int i=0;i<str_num.length();i++) {
            char c = str_num.charAt(i);
            int ni = c - '0';
            Obj2DRectangle obj = new Obj2DRectangle(loadX+i*numwidth,loadY,numwidth,numheight, TextureManager.getTextures(numsF[ni+10]),
                    ShaderManager.getShader(2));
            obj.drawSelf();
        }
    }
}
