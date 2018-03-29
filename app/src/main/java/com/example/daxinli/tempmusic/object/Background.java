package com.example.daxinli.tempmusic.object;

import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/3/28.
 * emmm 这个弹起效果写的也太丑陋了 T——T
 * 有时间一定好好研究一下
 */

public class Background {
    private static final String TAG = "Background";
    static final String[] nameF = {"BGrk1.png","BGrk2.png","BGrk3.png","BGrk4.png"};
    static final float yg = 1.0f;           //竖直方向的假想加速度
    static final float yInitVel = 20.0f;     //y方向速度的初始值
    static final float bounceState1Limit = 1920.0f-100.0f;      //弹起的最大高度
    static final float VelDec = 50;     //弹起需要减少的速度绝对值
    static final float rongcuoSpan = 10.0f; //在这个范围内算作已经到达指定位置
    static final float boundAttachSpan = 20.0f; //掉落后上端会显示一部分的空白 需要加长图画

    boolean isDead;
    boolean isAnim;

    int rankBG;         //当前背景的序号
    float downLimit;    //处于上方的界面的最下方
    float baseWidth;
    float baseHeight;
    float yVel;         //竖直方向的瞬间速度
    int state;

    Obj2DRectangle [] BGS = new Obj2DRectangle[4];      //四种不同的背景

    public Background() {
        isDead = false;
        isAnim = false;
        rankBG = 0;
        state = 0;
        baseWidth = GameData.STANDARD_WIDTH;
        baseHeight = GameData.STANDARD_HIEGHT;
        for(int i=0;i<4;i++) {
            BGS[i] = new Obj2DRectangle(-100,0,baseWidth+200,baseHeight
                    , TextureManager.getTextures(nameF[i]), ShaderManager.getShader(2));
        }
    }
    public void drawSelf() {
        if(!isAnim) {
            BGS[rankBG].drawSelf();
        } else {
            //背景界面进行切换
            Obj2DRectangle BGup = new Obj2DRectangle(-100,downLimit-baseHeight-boundAttachSpan,baseWidth+200,baseHeight+2*boundAttachSpan
                    , TextureManager.getTextures(nameF[rankBG]), ShaderManager.getShader(2));
            Obj2DRectangle BGdown = new Obj2DRectangle(-100,downLimit,baseWidth+200,baseHeight
                    , TextureManager.getTextures(nameF[rankBG-1]), ShaderManager.getShader(2));
            BGup.drawSelf();
            BGdown.drawSelf();

            yVel+= yg;
            downLimit += yVel;
            if(Math.abs(downLimit-baseHeight)<rongcuoSpan || downLimit>baseHeight) {
                if(state==0) {
                    state++;
                    yVel -= VelDec;
                    yVel = -yVel;
                } else if(state==2) {
                    isAnim = false;
                }
            }
            if(state==1 && downLimit<bounceState1Limit) {
                yVel = -yVel;
                state++;
            }
            if(state!=0 && yVel >0) {   //没有达到上面条件的一种情况
                state = 2;
            }
            /*
            if(!isDropDown && downLimit<(baseHeight*2/3)) {
                yVel = yg;
            }
            if(Math.abs(downLimit-baseHeight)<10.0f || downLimit>baseHeight) {   //落到最低点进行反弹
                if(yVel>0 && yVel>yVelDec) {
                    yVel -= yVelDec;      //反弹减损
                }
                yVel = -yVel;       //反弹效果
                if(yVelDec-4.0f>0) yVelDec -= 4.0f;
                isDropDown = false;
            }
            Log.e(TAG, Float.toString(yVel));
            if(yVel<0 && yVel>-5.0f) {
                Log.e(TAG, Float.toString(yVel));
                if(baseHeight-downLimit<10.0f) isAnim = false;       //结束动画  //下一帧渲染静止
            }
            downLimit += yVel;
            */
        }
    }
    public void switchBG() {
        if(rankBG<3) {
            rankBG++;
            isAnim = true;
            //isDropDown = true;
            yVel = yInitVel;
            //yVelDec = yInitVelDec;
            state=0;
            downLimit = 0.0f;
        }
    }
}
