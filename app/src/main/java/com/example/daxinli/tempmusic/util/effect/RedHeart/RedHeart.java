package com.example.daxinli.tempmusic.util.effect.RedHeart;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

/**
 * Created by Daxin Li on 2018/3/27.
 * 游戏的奖励 红心 可以增加玩家的生命值
 * 应该绘制在最上层 priority
 */

public class RedHeart {
    static final int stateTimeLimit = 30;       //静止时redheart停止的帧数

    static final float speed2 = 5.0f;          //被点击后移动的速度
    static final float jianshaowidth = 80.0f;   //被点击后减小的大小
    static final float jianshaoheight = 80.0f;
    static final float AngleSpan = 3.5f;        //点击后红心的旋转角度
    static final float disLimit = 30.0f;        //如果最终距离小于limit就直接移动到最终位置

    float targetX = 800.0f;        //最终移动到的位置
    float targetY = 20.0f;

    int AnimType;
    float speed;
    float x,y;
    float width,height;
    float xAngle;
    int state,state3Time;               //生命周期有三个阶段：1随着下落阶段 2被点击飞走阶段 3落到指定位置显示生命值阶段
    boolean isDead;
    boolean isDrawable ;                //在创建动画结束后 设置为!isDrawable
    Obj2DRectangle objRedHeart;


    public RedHeart(float x,float y,float width,float height,float speed) {     //第一种类型的redHeart 下落动画
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        state = 1;
        state3Time = 0;
        xAngle = 0.0f;
        isDead = false;
        AnimType = 0;
    }
    public RedHeart(float width,float height) {         //第二种类型的redheart 仅需要显示生命值的动画
        AnimType = 1;
        isDead = false;
        state3Time = 0;
        this.x = targetX;
        this.y = targetY;
        this.width = width-jianshaowidth;
        this.height = height-jianshaoheight;
    }
    public void go() {
        if(AnimType == 0) {
            if(state == 1)
                this.y += speed;
            else if(state==2) {
                float dh = this.y-targetY;
                float dw = this.x-targetX;
                float costheta = dw / (float)Math.sqrt(dw*dw+dh*dh);
                float sintheta = dh / (float)Math.sqrt(dw*dw+dh*dh);
                //进行动画的计算 改变xy
                this.x-=speed2*costheta;
                this.y-=speed2*sintheta;
                this.xAngle += this.AngleSpan;
                if(SFUtil.distance(this.x,this.y,targetX,targetY)<=disLimit) {
                    this.x = targetX;
                    this.y = targetY;
                    state = 3;
                }
            }
        }
    }
    public void drawSelf() {
        if(!isDead) {

            if(AnimType==0) {
                if(state==3) {
                    //绘制生命值数字
                    DrawUtil.drawNumber(targetX+width,targetY,GameData.num_W,GameData.num_H,GameData.gamerHealth);
                    //计算阶段三的时间
                    if(++state3Time>stateTimeLimit) {
                        //声明周期结束
                        isDead = true;
                    }
                }
                objRedHeart = new Obj2DRectangle(x,y,width,height ,
                        TextureManager.getTextures("pic_rheart_g.png"), ShaderManager.getShader(2));
                if(state==2) {      //对红心进行旋转
                    objRedHeart.setRotate2D(2,xAngle);
                }
                objRedHeart.drawSelf();
            } else if(AnimType==1) {
                DrawUtil.drawNumber(targetX+width,targetY,GameData.num_W,GameData.num_H,GameData.gamerHealth);
                //计算阶段三的时间
                if(++state3Time>stateTimeLimit) {
                    //声明周期结束
                    isDead = true;
                }
                objRedHeart = new Obj2DRectangle(x,y,width,height,
                        TextureManager.getTextures("pic_rheart_g.png"),ShaderManager.getShader(2));
                objRedHeart.drawSelf();
            }
        }

    }
    public void onTouch(MotionEvent e) {    //内部处理点击事件
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float pressX = Constant.fromRealScreenXToStandardScreenX(e.getX());
                float pressY = Constant.fromRealScreenYToStandardScreenY(e.getY());
                if(SFUtil.isin(pressX,pressY,new Area(x-5,y-5,width+10,height+10))) {
                    synchronized (GameData.lock) {
                        GameData.gamerHealth ++;
                    }
                    runAnim();
                }
                break;
        }
    }
    public void runAnim() {
        state = 2;
        width -= jianshaowidth;
        height -= jianshaoheight;
        //根据当前的生命值计算红心的最终位置
        String str_gamerHealth = Integer.toString(GameData.gamerHealth);
        int length = str_gamerHealth.length();
        this.targetX = GameData.STANDARD_WIDTH-GameData.num_W*length-width-20;
        this.targetY = 20;
    }
    /*
    public void showHealth() {      //单独在右上角显示一下生命值
        //绘制中自动对三阶段进行计时
        isDead = false;
        isDrawable = true;
        state3Time = 0;
    }
    */
    public boolean getIsDead() {
        return isDead;
    }
    public void restart() {
        this.isDead = false;
        state3Time = 0;
    }
}