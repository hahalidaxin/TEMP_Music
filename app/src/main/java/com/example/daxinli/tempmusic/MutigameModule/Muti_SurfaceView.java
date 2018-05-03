package com.example.daxinli.tempmusic.MutigameModule;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/5/3.
 * 用来管理多人模式的GLsurfaceView
 */

public class Muti_SurfaceView extends GLSurfaceView{
    private Renderer mRenderer;
    private Context mcontext;

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

        }
        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //设置视口的大小 为当前view的宽高
            width = width;
            height = height;
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
        }
    }
}
