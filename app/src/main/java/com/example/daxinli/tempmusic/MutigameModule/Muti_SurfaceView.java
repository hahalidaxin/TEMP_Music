package com.example.daxinli.tempmusic.MutigameModule;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmTool;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/3.
 * 用来管理多人模式的GLsurfaceView
 */

public class Muti_SurfaceView extends GLSurfaceView{
    public static final String TAG = "Muti_SurfaceView";
    public static final long DRAWSPANTIME = 20;
    private SceneRenderer mRenderer;
    private Context mcontext;
    private boolean initFlag = false;

    private RhythmTool rhythmTool;
    private long lastDrawTime;

    public Muti_SurfaceView(Context context)
    {
        super(context);
        this.mcontext = context;
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
                rhythmTool = new RhythmTool(mcontext,200,50,1000,1000);
                initFlag = true;
                lastDrawTime = System.currentTimeMillis();
            }

            if(rhythmTool!=null) {
                rhythmTool.onDraw();
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
