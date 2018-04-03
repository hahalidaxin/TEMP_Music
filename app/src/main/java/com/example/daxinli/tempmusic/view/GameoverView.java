package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/20.
 */

public class GameoverView extends BaseView {
    private MySurfaceView mv;
    private static final String TAG = "GameoverView";
    boolean isInit = false;
    boolean btnRestart_isPressed = false;
    boolean btnExit_isPressed = false;
    public GameoverView(MySurfaceView mv) {
        this.mv = mv;
    }
    @Override
    public void initView() {
        float loadX = (GameData.STANDARD_WIDTH-GameData.btnExit_W)/2;
        GameData.area_btn_restart = new Area(loadX,1400,GameData.btnExit_W,GameData.btnExit_H);     //控件的UI设置
        GameData.area_btn_exit = new Area(loadX,1650,GameData.btnExit_W,GameData.btnExit_H);
        btnRestart_isPressed = false;
        btnExit_isPressed = false;
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
                    isInit = false;
                    MySurfaceView.curView = MySurfaceView.gameView;
                    btnRestart_isPressed = false;
                } else if(SFUtil.isin(x,y,GameData.area_btn_exit)) {        //游戏退出//如何处理
                    mv.exit();
                    btnExit_isPressed = false;
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
        //绘制分数
        String str_score = Integer.toString(GameData.GameScore);
        int length = str_score.length();
        float loadX = (GameData.STANDARD_WIDTH-length*GameData.num_W)/2;
        DrawUtil.drawNumber(loadX,800,(float)GameData.num_W,(float)GameData.num_H,GameData.GameScore);
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
    }
    public void setBtnRestart_isPressed(boolean flag) { this.btnRestart_isPressed = flag; }
    public void setBtnExit_isPressed(boolean flag) { this.btnExit_isPressed = flag; }
}
