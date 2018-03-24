package com.example.daxinli.tempmusic.object;

/**
 * Created by Daxin Li on 2018/3/23.
 */

import android.opengl.GLES30;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Obj2DTriangle
{

    public static float loadPosition=90;
    public static float step=0;
    public static float AngleSpng=0;
    public static int spngId;
    public static float spngx=540f;
    public static float spngy=960f;
    public static float spng_Sizex=1080f;
    public static float spng_Sizey=1920f;
    public static float Angle2D=0;

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

    //正三角形的构造函数
    public Obj2DTriangle(float x, float y, float radius,
                         float a, float r, float g, float b,
                         float xAngle,float xScale,
                         int programId) {
        this.x= Constant.fromScreenXToNearX(x);	//将坐标转化为视口坐标
        this.y=Constant.fromScreenYToNearY(y);
        this.radius = Constant.fromPixSizeToNearSize(radius);
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.programId = programId;
        this.xAngle = xAngle;
        this.xScale = xScale;
        initVertexData();
    }
    //这里的Triagnle类可以绘制多个三角形
    //如果需要可以传入vertex的信息 直接进行三角形的绘制
    //这样可以减少管线的IO时间
    public void initVertexData()
    {

        vCount=3;
        float dx = radius* (float)Math.cos(Math.PI/6);
        float dy = radius/2;
        float vertices[]=new float[]
                {
                        0,radius,0,
                        -dx,-dy,0,
                        dx,-dy,0,
                };
        ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer=vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float colors[] = new float[] {
                r,g,b,a,
                r,g,b,a,
                r,g,b,a,
        };
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
    public void setY(float y)
    {
        this.y=Constant.fromScreenYToNearY(y);
    }

    public void setX(float x)
    {
        this.x=Constant.fromScreenXToNearX(x);
    }

    //每次绘制都会对转换矩阵进行重新计算
    //可以每次绘制钱自定义对于变换的信息达到动画的目的
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
        GLES30.glUniform1f(CLStepHandle, step);
        GLES30.glUniform1f(xHandle, loadPosition);
        MatrixState2D.pushMatrix();

        MatrixState2D.translate(x,y, 0);                                            //将原坐标移动到xy置顶窗口位置

        MatrixState2D.rotate(xAngle,0,0,1);    //旋转
        MatrixState2D.scale(xScale, xScale,xScale);   //缩小
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


        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);

        MatrixState2D.popMatrix();
        GLES30.glDisable(GLES30.GL_BLEND);

    }
}
