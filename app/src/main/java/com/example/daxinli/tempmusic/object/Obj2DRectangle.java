package com.example.daxinli.tempmusic.object;

import android.opengl.GLES30;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Obj2DRectangle
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
	public FloatBuffer mTexCoorBuffer;
	public FloatBuffer mColorBuffer;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    int maTexCoorHandle;
    int CLStepHandle;
    int xHandle;
    
    int programId;
	int texId;
	int vCount;
    boolean initFlag=false;
    float x;
	float y;
	boolean isLoad=false;
	boolean isPure = false;

	int han=0;
	int lie=0;
	int HZ;
	int LZ;
	int muSjFactor;
	int count=0;
	int spng=0;

	float a,r,g,b;
	
	public Obj2DRectangle(float x, float y, float width, float height, float a, float r, float g, float b, int programId) {
		x+=width/2; y+=height/2;
		this.x=Constant.fromScreenXToNearX(x);	//将坐标转化为视口坐标
		this.y=Constant.fromScreenYToNearY(y);
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.programId = programId;
        this.isPure = true;
        initVertexDataRect(width,height);
    }
	public Obj2DRectangle(float x, float y, float picWidth, float picHeight, int texId, int programId)
	{
		x+=picWidth/2; y+=picHeight/2;
		this.x=Constant.fromScreenXToNearX(x);
		this.y=Constant.fromScreenYToNearY(y);
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);
	}
	/*
	public Obj2DRectangle(float x,float y,float picWidth,float picHeight,int texId,int programId,int spng)
	{
		this.spng=spng;
		this.x= Constant.fromScreenXToNearX(x);;
		this.y= Constant.fromScreenYToNearY(y);
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);
	}

	public Obj2DRectangle(float x,float y,float width,float height,int han,int lie,int HZ,int LZ,
			int texId,int programId)
	{
		this.x=Constant.fromScreenXToNearX(x);
		this.y=Constant.fromScreenYToNearY(y);
		isLoad=true;
		this.HZ=HZ;
		this.LZ=LZ;
		this.han=han;
		this.lie=lie;
		this.texId=texId;
		this.programId=programId;
		initVertexData(width,height);
	}
	*/

	public void initVertexData(float width,float height)
	{
		//屏幕->视口
		width = Constant.fromPixSizeToNearSize(width);		//将屏幕高度转化为视口高度
		height = Constant.fromPixSizeToNearSize(height);

		vCount=4;
		float vertices[]=new float[]
		{
				-width/2,height/2,0,
				-width/2,-height/2,0,
				width/2,height/2,0,
				width/2,-height/2,0
		};
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertices.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer=vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		float[] texCoor=new float[12];
		
		if(!isLoad)
		{
			texCoor=new float[]{
					0,0,0,1,1,0,
					1,1,1,0,0,1};
		}else
		{
			
			float sstep=(float)1/LZ;
			float tstep=(float)1/HZ;
			texCoor=new float[]
					{
					   sstep*han-sstep,tstep*lie-tstep,
					   sstep*han-sstep,tstep*lie,
					   sstep*han,tstep*lie-tstep,
					   
					   sstep*han,tstep*lie,
					   sstep*han,tstep*lie-tstep,
					   sstep*han-sstep,tstep*lie
					};
		}
		ByteBuffer cbb=ByteBuffer.allocateDirect(texCoor.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer=cbb.asFloatBuffer();
		mTexCoorBuffer.put(texCoor);
		mTexCoorBuffer.position(0);
	}
	public void initVertexDataRect(float width,float height) {			//初始化长方形的顶点数据	//需要顶点坐标以及顶点的颜色数据
		initVertexData(width,height);
		width = Constant.fromPixSizeToNearSize(width);		//将屏幕高度转化为视口高度
		height = Constant.fromPixSizeToNearSize(height);

		float colors[] = new float[] {
				r,g,b,a,
				r,g,b,a,
				r,g,b,a,
				r,g,b,a
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
		
		if(!isPure) maTexCoorHandle= GLES30.glGetAttribLocation(programId, "aTexCoor");
		else maColorHandle=GLES30.glGetAttribLocation(programId,"aColor");
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
		MatrixState2D.translate(x,y, 0);
		if(spng==1){
			MatrixState2D.scale(step/100,step/100,step/100);
		}
		if(spng==1){
			MatrixState2D.rotate(AngleSpng, 0, 0, 1);
		}
		if(spng==2){
			MatrixState2D.rotate(Angle2D, 0, 0, 1);
		}
		if(spng==3){
			MatrixState2D.rotate(AngleSpng, 0, 0, 1);
		}
        if(spng==4){
        	MatrixState2D.scale(step/100,step/100,step/100);
        }
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

    	if(!isPure) {
			GLES30.glVertexAttribPointer
					(
							maTexCoorHandle,
							2,
							GLES30.GL_FLOAT,
							false,
							2*4,
							mTexCoorBuffer
					);
		} else  {
			GLES30.glVertexAttribPointer
					(
							maColorHandle,
							4,
							GLES30.GL_FLOAT,
							false,
							4*4,
							mColorBuffer
					);
		}
    	
    	GLES30.glEnableVertexAttribArray(maPositionHandle);  
    	if(!isPure)GLES30.glEnableVertexAttribArray(maTexCoorHandle);
    	else GLES30.glEnableVertexAttribArray(maColorHandle);
    	
    	
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    	if(!isPure) GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,texId);
    	
    	
    	GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, vCount); 
    	
    	

    	MatrixState2D.popMatrix();
    	GLES30.glDisable(GLES30.GL_BLEND);
    	
	}
}
