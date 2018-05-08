package com.example.daxinli.tempmusic.object;

import android.opengl.GLES30;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Obj2DRectangle
{
	private static final String TAG = "Obj2DRectangle";
	private float radiusSpan = 20.0f;
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
    float x,NearX;
	float y,NearY;
	float Width;
	float Height;
	float xAngle = 0.0f;
	boolean isLoad=false;
	boolean isPure = false;

	int han=0;
	int lie=0;
	int HZ;
	int LZ;
	int spng=0;

	float a,r,g,b;
	float rx=0,ry=0;		//矩形方块的中心点[传入]
	int rXHandle;
	int rYHandle;
	int clockTime=0;		//时钟 记录动画开始时间
	float AnimRadius=0;		//扩大的半径 [传入]
	float RadiusLimit;
	boolean isAnim; 		//动画进行的标志位
	int AnimRadiusHandle;
	
	public Obj2DRectangle(float x, float y, float width, float height, float a, float r, float g, float b, int programId) {
		x+=width/2; y+=height/2;
        this.NearX = Constant.fromScreenXToNearX(x);
        this.x = x;
        this.NearY = Constant.fromScreenYToNearY(y);
        this.y = y;
		this.Width = width;
        this.Height = height;
		this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.programId = programId;
        this.isPure = true;
        this.RadiusLimit = (float)Math.sqrt((width/2)*(width/2)+(height/2)*(height/2))+5.0f;
        initVertexDataRect(width,height);
    }
	public Obj2DRectangle(float x, float y, float picWidth, float picHeight, int texId, int programId)
	{
		x+=picWidth/2; y+=picHeight/2;
		this.NearX = Constant.fromScreenXToNearX(x);
		this.x = x;
		this.NearY = Constant.fromScreenYToNearY(y);
		this.y = y;
		this.Width = picWidth;
		this.Height = picHeight;
		this.texId=texId;
		this.programId=programId;
		initVertexData(picWidth,picHeight);
	}

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

        rXHandle=GLES30.glGetUniformLocation(programId,"aCoreX");
        rYHandle=GLES30.glGetUniformLocation(programId,"aCoreY");
        AnimRadiusHandle=GLES30.glGetUniformLocation(programId,"aRadius");
	}
	public void setY(float y)
	{
		y+=Height/2;
		this.NearY = Constant.fromScreenYToNearY(y);
		this.y = y;
	}
	
	public void setX(float x)
	{
		x+=Width/2;
		this.NearX = Constant.fromScreenXToNearX(x);
		this.x = x;
	}
	public void setRadiusSpan(float x) {
		this.radiusSpan = x;
	}
	public void setAngleRotate(int spng,float angle2D) {		//设置长方形进行旋转的角度信息
		this.spng = spng;
		this.Angle2D = angle2D;
	}

	public void setColor(float a,float r,float g,float b) {		//重新设置颜色 // 更改缓冲区数据
		this.a = a;
		this.r = r; this.g = g; this.b = b;
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
	public boolean isEqualColor(float a,float r,float g,float b) {
		return this.a ==a && this.r==r && this.g==g && this.b==b;
	}

	public void runAnim(int idx) {
		switch(idx) {
			case 1:
				//此处激活扩散特效
				isAnim = true;
				clockTime++;
				break;
		}
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
		//--------------------------------------------
    	if(isAnim) {
			//向片元着色器中传入全局变量
			if(AnimRadius>RadiusLimit) {	//边界判定 停止动画
				isAnim = false;
				AnimRadius = 0;
				clockTime = 0;
				setColor(0.2f,0,0,0);
			} else {
				clockTime++;
				AnimRadius+=radiusSpan;
			}
		} else {
    		AnimRadius = 0;			//Anim动画着色器中的标志位
		}
		GLES30.glUniform1f(AnimRadiusHandle,AnimRadius);
		GLES30.glUniform1f(rXHandle,Constant.fromStandardScreenXToRealScreenX(this.x));
		GLES30.glUniform1f(rYHandle, Constant.fromStandardScreenYToRealScreenY2(GameData.STANDARD_HIEGHT-this.y));
		//---------------------------------------------
		MatrixState2D.pushMatrix();
		MatrixState2D.translate(this.NearX,this.NearY, 0);
		if(spng==1){
			MatrixState2D.scale(step/100,step/100,step/100);
		}
		if(spng==1){
			MatrixState2D.rotate(AngleSpng, 0, 0, 1);
		}
		if(spng==2){		//spng angle2D对于2d图形进行旋转
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
	public void setRotate2D(int spng,float Angle2D) {
		this.spng = spng;
		this.Angle2D = Angle2D;
	}
}
