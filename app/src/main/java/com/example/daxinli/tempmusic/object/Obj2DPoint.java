package com.example.daxinli.tempmusic.object;

import android.opengl.GLES30;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Daxin Li on 2018/5/3.
 * 负责用OpneGlES绘制 圆点 可以用点精灵技术绘制不同大小的原点
 */

public class Obj2DPoint {
    public FloatBuffer mVertexBuffer;
    public FloatBuffer mColorBuffer;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    int CLStepHandle;
    int xHandle;
    int pointSizeHandle;

    int programId;
    int vCount;
    boolean initFlag=false;

    float a,r,g,b;
    float pts[];
    int ptSize ;

    public Obj2DPoint(int programId) {
        this.programId = programId;
    }
    public void setPoints(float a,float r,float g,float b,int pointSize,int pointCount,float[] pts) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.vCount = pointCount;

        float vertices[]=new float[vCount*3];
        float colors[] = new float[vCount*4];
        int verCnt=0,verColorCnt=0;
        this.ptSize = pointSize;

        for (int i=0;i<vCount;i++) {
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

        pointSizeHandle=GLES30.glGetUniformLocation(programId,"pointSize");
    }

    public void drawSelf()
    {
        if(!initFlag)
        {
            initShader();
            initFlag=true;
        }
        if(mVertexBuffer==null) {
            return ;
        }

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA,GLES30.GL_ONE_MINUS_SRC_ALPHA);

        GLES30.glUseProgram(programId);
        MatrixState2D.pushMatrix();

        GLES30.glUniform1f(pointSizeHandle,ptSize);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState2D.getFinalMatrix(), 0);
        GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT, false, 3*4, mVertexBuffer);
        GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false, 4*4, mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);

        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount);

        MatrixState2D.popMatrix();

        GLES30.glDisable(GLES30.GL_BLEND);

    }
}