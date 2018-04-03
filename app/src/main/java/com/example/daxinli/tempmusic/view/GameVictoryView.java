package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.effect.TriangleFirework.tri_ParticleSystem;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/29.
 * 游戏胜利界面
 */

public class GameVictoryView extends BaseView {
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

    Random random = new Random();
    int RandomInt = 0;
    int nowFireworkRk=0;
    int clockTime = 0;
    int lastFireTime = 0;
    boolean initFlag ;
    boolean btnRestart_isPressed;
    boolean btnExit_isPressed;

    public GameVictoryView(MySurfaceView mv) {
        this.mv = mv;
        initFlag = false;
    }

    @Override
    public void initView() {
        float loadX = (GameData.STANDARD_WIDTH-GameData.btnExit_W)/2;
        GameData.area_btn_restart = new Area(loadX,1400,GameData.btnExit_W,GameData.btnExit_H);     //控件的UI设置
        GameData.area_btn_exit = new Area(loadX,1650,GameData.btnExit_W,GameData.btnExit_H);
        btnRestart_isPressed = false;
        btnExit_isPressed = false;
        float W = GameData.STANDARD_WIDTH;
        float H = GameData.STANDARD_HIEGHT;
        backGroundBottom = new Obj2DRectangle(-100,0,W+200,H,1f,0,0,0,
                ShaderManager.getShader(6));
        backGround = new Obj2DRectangle(-100,GameData.area_btn_restart.y-80,W+200,H,1f,225/255f,57/255f,255/255f,
                ShaderManager.getShader(6));
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
                break;
            case MotionEvent.ACTION_UP:
                //按键的实际逻辑操作放在抬起事件中完成
                if(SFUtil.isin(x,y,GameData.area_btn_restart)) {
                    initFlag = false;
                    MySurfaceView.gameView.restartInitOp();
                    MySurfaceView.curView = MySurfaceView.gameView;
                } else if(SFUtil.isin(x,y,GameData.area_btn_exit)) {
                    mv.exit();
                }
                break;
        }
        return true;
    }

    @Override
    public void drawView(GL10 gl) {
        if(!initFlag) {
            initView();
            initFlag = true;
        }
        backGroundBottom.drawSelf();
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
        backGround.drawSelf();
        //绘制分数
        String str_score = Integer.toString(GameData.GameScore);
        int length = str_score.length();
        float loadX = (GameData.STANDARD_WIDTH-length*GameData.num_W)/2;
        DrawUtil.drawNumber(loadX,400,(float)GameData.num_W,(float)GameData.num_H,GameData.GameScore);
        //绘制按钮
        Area ar = GameData.area_btn_restart;
        if(btnRestart_isPressed)
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart2_gov.png");
        else
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart1_gov.png");
        ar = GameData.area_btn_exit;
        if(btnExit_isPressed)
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart2_gov.png");
        else
            DrawUtil.drawBitmap(ar.x,ar.y,ar.width,ar.height,"btn_restart1_gov.png");
        //backGround.drawSelf();
    }
}
