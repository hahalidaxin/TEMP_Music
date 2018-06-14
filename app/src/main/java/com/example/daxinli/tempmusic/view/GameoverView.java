package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.effect.TriangleFirework.tri_ParticleSystem;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/20.
 */

public class GameoverView extends BaseView {
    Object lock = new Object();
    static final int fire_minX = 100;
    static final int fire_maxX = 1000;
    static final int fire_minY = 1400;
    static final int fire_maxY = 1800;
    static final int minSpan = 60;
    static final int maxSpan = 100;
    static final int spanBetweenFires = 200;

    MySurfaceView mv;
    tri_ParticleSystem tris[] = new tri_ParticleSystem[4];
    Obj2DRectangle backGround;
    Obj2DRectangle backGroundBottom;
    ArrayList<tri_ParticleSystem> touchTriList = new ArrayList<>();
    Random random = new Random();
    int RandomInt = 0;
    int nowFireworkRk=0;
    int clockTime = 0;
    int lastFireTime = 0;

    private static final String TAG = "GameoverView";
    boolean isInit = false;
    boolean btnRestart_isPressed = false;
    boolean btnExit_isPressed = false;
    Area areaDownBar;
    public GameoverView(MySurfaceView mv) {
        this.mv = mv;
    }
    @Override
    public void initView() {
        float loadX = (GameData.STANDARD_WIDTH-GameData.btnExit_W)/2;
        GameData.area_btn_restart = new Area(loadX,1650,GameData.btnExit_W,GameData.btnExit_H);     //控件的UI设置
        GameData.area_btn_exit = new Area(25,1650,GameData.btnExit_H, GameData.btnExit_H);
        btnRestart_isPressed = false;
        btnExit_isPressed = false;
        areaDownBar = new Area(0,1400,1080,520);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        x = Constant.fromRealScreenXToStandardScreenX(x);
        y = Constant.fromRealScreenYToStandardScreenY(y);
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按键的按压效果放在按下事件中进行
                if(SFUtil.isin(x,y,GameData.area_btn_restart)) {
                    btnRestart_isPressed = true;
                } else if(SFUtil.isin(x,y,GameData.area_btn_exit)) {
                    btnExit_isPressed = true;
                }
                if(!SFUtil.isin(x,y,areaDownBar)) {     //如果点击的downbar上部分 //则需要产生一个新的粒子系统
                    synchronized (lock) {
                        touchTriList.add(new tri_ParticleSystem(x, y));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //按键的实际逻辑操作放在抬起事件中完成
                btnExit_isPressed = false;
                btnRestart_isPressed = false;
                if(SFUtil.isin(x,y,GameData.area_btn_restart)) {
                    isInit = false;
                    MySurfaceView.gameView.restartInitOp();
                    MySurfaceView.curView = MySurfaceView.gameView;
                } else if(SFUtil.isin(x,y,GameData.area_btn_exit)) {        //游戏退出//如何处理
                    mv.exit();
                }
                break;
        }
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        if(!isInit) {
            initView();
            isInit = true;
        }
        //绘制底色
        DrawUtil.drawRect(-100,0,GameData.STANDARD_WIDTH+200,GameData.STANDARD_HIEGHT,1,0,0,0);

        for(int i=0;i<touchTriList.size();i++) {
            if(touchTriList.get(i).isSysEnd()) {
                touchTriList.remove(i);
            }
        }
        synchronized (lock) {
            for (tri_ParticleSystem tri : touchTriList) {
                tri.drawSelf();
            }
        }
        ++clockTime;
        if(clockTime-lastFireTime>=RandomInt) {
            float loadX = (float)random.nextInt(fire_maxX-fire_minX)+fire_minX;
            float loadY = (float)random.nextInt(fire_maxY-fire_minY)+fire_minY;
            int rType = random.nextInt(2)+1;
            if(rType==2) {
                loadY -= 200;
            }
            tris[nowFireworkRk] = new tri_ParticleSystem(rType,loadX,loadY);
            nowFireworkRk = (nowFireworkRk+1) % 4;
            lastFireTime = clockTime;
            RandomInt = minSpan+random.nextInt(maxSpan-minSpan);
            if(nowFireworkRk==0) RandomInt = spanBetweenFires;
        }
        for(int i=0;i<4;i++) if(tris[i]!=null) {
            tris[i].drawSelf();
        }

        //绘制分数
        String str_score = Integer.toString(GameData.GameScore);
        int length = str_score.length();
        float numW = GameData.num_W*2.0f;
        float numH = GameData.num_H*3.0f;
        float loadX = (GameData.STANDARD_WIDTH-numW*length)/2;
        DrawUtil.drawNumber(loadX,500,numW,numH,GameData.GameScore);
        //绘制downBar
        DrawUtil.drawBitmap(areaDownBar.x,areaDownBar.y,areaDownBar.width,areaDownBar.height,"pic_GameOver_downBar.png");
        //绘制按钮
        Area ar = GameData.area_btn_restart;
        if(btnRestart_isPressed)
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart2_gov.png");
        else
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart1_gov.png");
        ar = GameData.area_btn_exit;
        if(btnExit_isPressed)
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_exitgame2_gov.png");
        else
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_exitgame1_gov.png");
    }
    public void setBtnRestart_isPressed(boolean flag) { this.btnRestart_isPressed = flag; }
    public void setBtnExit_isPressed(boolean flag) { this.btnExit_isPressed = flag; }

    @Override
    public boolean getIsInit() {
        return true;
    }
}
