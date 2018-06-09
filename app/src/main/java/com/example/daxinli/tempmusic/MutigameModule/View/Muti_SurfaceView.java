package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Intent;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.MutigameModule.Activity.MutiGamingActivity;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.view.BaseView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/3.
 * 用来管理多人模式的GLsurfaceView
 */

public class Muti_SurfaceView extends GLSurfaceView {
    public static final String TAG = "Muti_SurfaceView";
    public static final long DRAWSPANTIME = 20;
    private SceneRenderer mRenderer;
    public MutiGamingActivity mcontext;
    public BaseView curView,playingView;
    public InstruView instruView;
    public Intent mintent;

    private RhythmTool rhy;
    private boolean initFlag = false;
    private long lastDrawTime;

    public Muti_SurfaceView(MutiGamingActivity context, Intent intent)
    {
        super(context);
        this.mcontext = context;
        this.mintent = intent;
        this.setEGLContextClientVersion(3);

        mRenderer = new SceneRenderer();
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        public void onDrawFrame(GL10 gl)
        {
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            if(!initFlag) {
                instruView = new InstruView(Muti_SurfaceView.this,mintent);
                curView = instruView;
                lastDrawTime = System.currentTimeMillis();
                initFlag = true;
            }

            if(curView!=null && curView.getIsInit()) {
                curView.drawView(gl);
            }

            if(System.currentTimeMillis()-lastDrawTime<DRAWSPANTIME) {
                try {
                    Thread.sleep(DRAWSPANTIME-(System.currentTimeMillis()-lastDrawTime));
                    lastDrawTime = System.currentTimeMillis();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            width = 1920;
            height = 1080;
            Log.e(TAG, String.format("onSurfaceCreated: %d %d",width,height));
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
            ShaderManager.loadCodeFromFile(mcontext.getResources());
            ShaderManager.compileShader();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(curView!=null) {
            curView.onTouchEvent(event);
        }
        return true;
    }

    public void startGame() {
        //将curView切换
        synchronized (this.curView) {
            //this.curView =
        }
    }
}
