package com.example.daxinli.tempmusic.MutigameModule.View;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule.RhythmTool;
import com.example.daxinli.tempmusic.musicTouch.MutiGameActivity;
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
    private Context mcontext;
    private BaseView curView;
    private CreateHomeView mcreateView;
    private EnterHomeView menterView;

    private boolean initFlag = false;
    private RhythmTool rhythmTool;
    private long lastDrawTime;

    public Muti_SurfaceView(Context context,String initType)
    {
        super(context);
        this.mcontext = context;
        this.setEGLContextClientVersion(3);

        mRenderer = new SceneRenderer(initType);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer
    {
        private String initType;
        public SceneRenderer(String initType) {
            this.initType = initType;
        }
        public void onDrawFrame(GL10 gl)
        {
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            if(!initFlag) {
                //rhythmTool = new RhythmTool(mcontext,100,100,1000,1000,500);
                menterView = new EnterHomeView();
                mcreateView = new CreateHomeView();
                initFlag = true;
                if(initType.equals(MutiGameActivity.ENTERHOMETYPE)) {            //进入房间
                    curView = menterView;
                } else if(initType.equals(MutiGameActivity.CREATEHOMETYPE)) {   //创建房间
                    curView = mcreateView;
                }
                lastDrawTime = System.currentTimeMillis();
            }

            if(curView!=null) {
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

            Log.e(TAG, "Width : "+Integer.toString(width)+"  Height: "+Integer.toString(height) );
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
}
