package com.example.daxinli.tempmusic.object;

import android.app.Activity;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.MutiPlayActivity;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.GameActivity;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.effect.ElseEffect.DrawScore;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;


/**
 * Created by hahal on 2018/2/26.
 * 单个滑块类
 */

public class MainSlide{
    private static final String TAG = "MainSlide";
    public int type;                                            //滑块的种类 0-点击滑块 1-拖长 长滑块
    public float X;
    public float Y;
    public float Width;
    public float Height;
    public float speed;
    public int Pitch;
    public int Instru;
    public int state;          //滑块的状态 0-正常 1-蓝色按压 2-红色没触摸

    private float baseTouchLimit;
    private float ls_minY;            //对于长的滑块进行触摸状态以及特效的判断
    private int touchMode;         //用户触摸状态 0-没有触摸过 1-触摸按下状态 2-触摸过已经抬起
    private float pressCurX;        //长时间按压时需要用的DOWN时候的XY坐标
    private float pressCurY;
    private DrawScore scoredraw;
    private int musicStreamID;
    Activity mactivity;
    int plusScoreTimes;

    Obj2DRectangle objSlide;

    public MainSlide(Activity activity, float X, float Y, float Width, float Height, int Pitch, int ttype, int instruType) {
        this.mactivity = activity;
        this.X = X;
        this.Y = Y;
        this.Width = Width;
        this.Height = Height;
        this.Pitch = Pitch;
        this.Instru = instruType;
        this.type = ttype;
        this.speed = GameData.gameSpeed[GameData.GameRK] * GameData.MainSlideTHSpan;
        this.state = 0;

        this.ls_minY = Y + Height;
        this.touchMode = 0;
        this.plusScoreTimes = 1;

        if(Height>GameData.gameSpeed[0]*600) type=2;
        else type=1;
        if (type == 1) {
            objSlide = new Obj2DRectangle(X, Y, Width, Height, 1, 0, 0, 0,            // TODO: 2018/3/19 新建一个shader  用于接收argb绘制相应颜色方块
                    ShaderManager.getShader(6));
        } else if(type==2){
            //
        }
        if(objSlide!=null) objSlide.setRadiusSpan(GameData.slideAnim1Radius[GameData.GameRK]);
    }
    public void go() {                                          //滑块进行下滑
        this.Y += this.speed; //GameData.gameSpeed[GameData.GameRK]*GameData.MainSlideTHSpan;
        this.ls_minY += this.speed; //GameData.gameSpeed[GameData.GameRK]*GameData.MainSlideTHSpan;
        //根据游戏难度决定每sleepSpan时间内滑块下落的高度
    }

    public void drawSelf() {                      //滑块的绘制方法
        switch (state) {
            case 0:
                if(type==1) {
                    //绘制黑色方块
                    //DrawUtil.drawRect(X,Y,Width,Height,1,0,0,0);
                    objSlide.setX(X); objSlide.setY(Y);
                    objSlide.drawSelf();
                } else if(type==2)
                {
                    DrawUtil.drawLongSlide(X,Y,Width,Height);                        //绘制长方块
                }
                break;
            case 1:
                if(type==1) {
                    //DrawUtil.drawRect(X,Y,Width,Height,0.2f,0,0,0);
                    if(objSlide.isAnim) {
                        objSlide.setX(X);
                        objSlide.setY(Y);
                        objSlide.drawSelf();
                    } else {
                        if(!objSlide.isEqualColor(0.2f,0,0,0)) {
                            objSlide.setColor(0.2f, 0, 0, 0);
                        }
                        objSlide.setX(X); objSlide.setY(Y);
                        objSlide.drawSelf();
                    }
                } else
                {
                    if(touchMode == 1 && state == 1) ls_minY = Math.max(Y, Math.min(ls_minY,pressCurY));
                    if(ls_minY-Y<=5 && ls_minY-Y>=0 && plusScoreTimes>0) {       //当一直触摸到长滑块尾部的时候才会算作加分
                        synchronized (GameData.lock) {
                            GameData.GameScore += GameData.slideT2score;
                        }
                        //一个丑陋的触发score动画的方法
                        // TODO: 2018/3/28  以后需要该一下这个丑陋的方法
                        this.scoredraw.runAnim();
                        plusScoreTimes--;
                    }
                    DrawUtil.drawLongSlide(X,Y,Width,Height);
                    float finalY = Math.max(Y,ls_minY-20);
                    DrawUtil.drawLongSlideEffect(X,finalY,Width,Height-(finalY-Y));
                }
                break;
            case 2:
                DrawUtil.drawRect(X, Y, Width, Height,1,1,0,0);
                break;
        }
    }
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        eX = Constant.fromRealScreenXToStandardScreenX(eX);
        eY = Constant.fromRealScreenYToStandardScreenY(eY);
        if(type==1) {
            if(!SFUtil.isin(eX,eY,new Area(X,Y,Width,Height)))
            return false;

            if(state==0) {
                state = 1;
                objSlide.runAnim(1);
                playSoundKey();
                if(plusScoreTimes>0) {
                    synchronized (GameData.lock) {
                        GameData.GameScore += GameData.slideT1score;
                    }
                    plusScoreTimes--;
                }
                return true;
            }
        } else if(type==2) {                    //对于type==2的长滑块类型
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(SFUtil.isin(eX,eY,new Area(X,Y,Width,Height))) {
                        onTouchDown(event);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    onTouchMove(event);
                    break;
            }
            return SFUtil.isin(eX,eY,new Area(X,Y,Width,Height));   //返回是否在滑块区域内部
        }
        return false;
    }
    public void onTouchDown(MotionEvent event) {        //触摸按下
        pressCurX = event.getX(); pressCurY = event.getY();
        pressCurX = Constant.fromRealScreenXToStandardScreenX(pressCurX);
        pressCurY = Constant.fromRealScreenYToStandardScreenY(pressCurY);

        baseTouchLimit = Height*(4-GameData.GameRK)/6;
        if(state == 0 && pressCurY>=Y+baseTouchLimit) {
            playSoundKey();
            state = 1;
        }
        if(touchMode!=2 && state==1) touchMode = 1;
        if(touchMode==1 && state==1) ls_minY = Math.max(Y,Math.min(ls_minY,pressCurY));
    }
    public void onTouchMove(MotionEvent event) {
        if(touchMode==1 && state==1) ls_minY = Math.max(Y,(Math.min(ls_minY,event.getY()))); //最小不能小过Y
        //更新Slide信息
        float eX = Constant.fromRealScreenXToStandardScreenX(event.getX());
        float eY = Constant.fromRealScreenYToStandardScreenY(event.getY());
        if(!SFUtil.isin(eX,eY,new Area(X,Y,Width,Height)))
            if(touchMode == 1) {
                touchMode = 2;               // 只有touchMode为1的时候才会更新信息

            }
        //如果Move出去 则触摸结束
    }
    public void onTouchUp(MotionEvent event) {
        if(touchMode==1 && state==1) {
            touchMode = 2;

        }
        //如果触摸抬起 则触摸结束
    }
    public boolean getIsGetScore() {        //获取滑块是否已经得到分数
        return plusScoreTimes==0;
    }
    public void getScoreDraw(DrawScore scoredraw) {
        this.scoredraw = scoredraw;
    }
    public void playSoundKey() {
        if(mactivity instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity)mactivity;
            gameActivity.sound.playMusic(SoundManager.RESKeyMusic[Instru][Pitch],0);
        } else {
            MutiPlayActivity gameActivity = (MutiPlayActivity) mactivity;
            gameActivity.sound.playMusic(SoundManager.RESKeyMusic[Instru][Pitch],0);
        }
    }
}
