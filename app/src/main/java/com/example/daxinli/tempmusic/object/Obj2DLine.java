package com.example.daxinli.tempmusic.object;

import android.opengl.GLES30;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Daxin Li on 2018/5/3.
 * 负责在OpengGLes中绘制直线
 */

public class Obj2DLine {

    public FloatBuffer mVertexBuffer;
    public FloatBuffer mColorBuffer;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    int CLStepHandle;
    int xHandle;

    int programId;
    int vCount;
    boolean initFlag=false;
    float x,y,radius;
    public float xAngle,xScale;

    float a,r,g,b;

    public Obj2DLine(float a,float r,float g,float b,int programId){
        this.a = a;
        this.r = a;
        this.g = g;
        this.b = b;
        this.programId = programId;
    }
    public void setLinePoints(int vertexCount,float[] pts)
    {
        vCount=vertexCount;
        float vertices[]=new float[vertexCount*3];
        float colors[] = new float[vertexCount*4];
        int verCnt=0,verColorCnt=0;

        for (int i=0;i<vertexCount;i++) {
            vertices[verCnt++] = Constant.fromScreenXToNearX_HP(Constant.fromStandardScreenXToRealScreenX(pts[i<<1]));
            vertices[verCnt++] = Constant.fromScreenYToNearY_HP(Constant.fromStandardScreenYToRealScreenY(pts[(i<<1)+1]));
            vertices[verCnt++] = 0;

            colors[verColorCnt++] = r;
            colors[verColorCnt++] = g;
            colors[verColorCnt++] = b;
            colors[verColorCnt++] = a;
        }
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer=vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        ByteBuffer cbb=ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer=cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void initShader()
    {
        maPositionHandle = GLES30.glGetAttribLocation(programId, "aPosition");
        maColorHandle=GLES30.glGetAttribLocation(programId,"aColor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(programId, "uMVPMatrix");
        CLStepHandle=GLES30.glGetUniformLocation(programId, "CLStep");
        xHandle=GLES30.glGetUniformLocation(programId, "xPosition");
    }

    public void drawSelf()
    {
        if(!initFlag)
        {
            initShader();
            initFlag=true;
        }
        GLES30.glEnable(GLES30.GL_BLEND);

        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA,GLES30.GL_ONE_MINUS_SRC_ALPHA);

        GLES30.glUseProgram(programId);
        GLES30.glLineWidth(10);
        MatrixState2D.pushMatrix();

        GLES30.glUniformMatrix4fv
                (
                        muMVPMatrixHandle,
                        1,
                        false,
                        MatrixState2D.getFinalMatrix(),
                        0
                );
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );


        GLES30.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES30.GL_FLOAT,
                        false,
                        4*4,
                        mColorBuffer
                );

        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);


        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, vCount);

        MatrixState2D.popMatrix();
        GLES30.glDisable(GLES30.GL_BLEND);

    }
}
