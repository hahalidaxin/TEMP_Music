package com.example.daxinli.tempmusic;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.GameActivity;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.view.BaseView;
import com.example.daxinli.tempmusic.view.EffectView;
import com.example.daxinli.tempmusic.view.GameVictoryView;
import com.example.daxinli.tempmusic.view.GameView;
import com.example.daxinli.tempmusic.view.GameoverView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
    public GameActivity  activity;
    public static BaseView curView;         //在每个子View中对curView进行修改
    public static GameView gameView;
    public static GameoverView gameoverView;
    public static EffectView effView;
    public static GameVictoryView gameVictoryView;

    private SceneRenderer mRenderer;

    public static boolean isPause = false;
    public boolean isInitOver = false;
    private static boolean isExit = false;

    public MySurfaceView(Context context)
    {
        super(context);
        activity=(GameActivity) context;
        this.setEGLContextClientVersion(3);

        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //建立View之间的跳转
                if(curView==gameView) {
                    if(isPause) {           //当前游戏是否为暂停
                    } else {
                        //exit?
                    }
                } else if(curView==gameoverView) {
                    //exit()
                }
                break;
        }
        return super.onKeyDown(keyCode,event);
    }

    private static final String TAG = "MySurfaceView";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(curView == null) {
            return true;
        }
        return curView.onTouchEvent(event);
    }

    public void exit() {
        //结束游戏actviity
        activity.removeActivity();
    }
    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        public void onDrawFrame(GL10 gl)
        {
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //View的初始化与纹理的加载放在这里 ！！
            if(curView == null)  {
                gameView = new GameView(MySurfaceView.this);
                gameoverView = new GameoverView(MySurfaceView.this);
                effView = new EffectView(MySurfaceView.this);
                gameVictoryView = new GameVictoryView(MySurfaceView.this);
                curView = gameView;
                GameData.viewState = GameData.Game_playing;             //设置游戏界面的View号
            }
            curView.drawView(gl);
        }
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            GLES30.glViewport
                    (
                            0,
                            0,
                            width,
                            height
                    );

            float ratio= (float) width/height;
            MatrixState2D.setInitStack();
            MatrixState2D.setCamera(0,0,5,0f,0f,0f,0f,1f,0f);
            MatrixState2D.setProjectOrtho(-ratio, ratio, -1, 1, 1, 100);
            
            MatrixState2D.setCamera(0,0,5,0f,0f,0f,0f,1f,0f);
            MatrixState2D.setLightLocation(0,50,0);

        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            GLES30.glClearColor(255f,255f,255f, 1.0f);
            GLES30.glEnable(GL10.GL_CULL_FACE);
            ShaderManager.loadCodeFromFile(activity.getResources());
            ShaderManager.compileShader();
        }
    }

}
