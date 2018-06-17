 package com.example.daxinli.tempmusic.util.manager;


 import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

 public class TextureManager
{
	static String[] texturesName={
			"0.png","1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png",	//10
			"btn_restart1_gov.png","btn_restart2_gov.png","pic_rheart_g.png","btn_pause_g.png",	//14
			"BGrk1.png","BGrk2.png","BGrk3.png","BGrk4.png","r0.png","r1.png","r2.png","r3.png",	//22
			"r4.png","r5.png","r6.png","r7.png","r8.png","r9.png","btn_start_g.png",					//29
            "pic_kb_r0.png","pic_kb_r1.png","pic_kb_r2.png","pic_kb_r3.png","pic_kb_r4.png","pic_kb_r5.png","pic_kb_r6.png", //36
			"pic_kb_r7.png","pic_kb_r8.png","pic_kb_r9.png","pic_kb_r10.png","pic_kb_r11.png","pic_kb_r12.png","pic_kb_r13.png",        //43
			"down_right.png",    //44-
			"btn_exitgame1_gov.png","btn_exitgame2_gov.png", //46
			"pic_GameOver_downBar.png",//47
		};
	
	static HashMap<String,Integer> texList=new HashMap<String,Integer>();
	public static int initTexture(GLSurfaceView mv, String texName, boolean isRepeat)
	{
		int[] textures=new int[1];
		GLES30.glGenTextures
		(
				1,
				textures,
				0
		);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
		
		GLES30.glTexParameterf
		(
				GLES30.GL_TEXTURE_2D,
				GLES30.GL_TEXTURE_MAG_FILTER,
				GLES30.GL_LINEAR
		);
		
		GLES30.glTexParameterf
		(
				GLES30.GL_TEXTURE_2D,
				GLES30.GL_TEXTURE_MIN_FILTER, 
				GLES30.GL_NEAREST
		);
		if(isRepeat)
		{
			
			GLES30.glTexParameterf
			(
					GLES30.GL_TEXTURE_2D,
					GLES30.GL_TEXTURE_WRAP_S, 
					GLES30.GL_REPEAT
			);
			
			GLES30.glTexParameterf
			(
					GLES30.GL_TEXTURE_2D,
					GLES30.GL_TEXTURE_WRAP_T, 
					GLES30.GL_REPEAT
			);
		}else
		{
			
			GLES30.glTexParameterf
			(
					GLES30.GL_TEXTURE_2D,
					GLES30.GL_TEXTURE_WRAP_S, 
					GLES30.GL_CLAMP_TO_EDGE
			);
			
			GLES30.glTexParameterf
			(
					GLES30.GL_TEXTURE_2D,
					GLES30.GL_TEXTURE_WRAP_T, 
					GLES30.GL_CLAMP_TO_EDGE
			);
		}
		String path="pic/"+texName;
		InputStream in = null;
		try {
			in = mv.getResources().getAssets().open(path);
		}catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bitmap=BitmapFactory.decodeStream(in);
		GLUtils.texImage2D
		(
				GLES30.GL_TEXTURE_2D,
				0,
				bitmap,
				0
		);
		bitmap.recycle();
		return textures[0];
	}
	
	public static void loadingTexture(GLSurfaceView mv, int start, int picNum)
	{
		for(int i=start;i<start+picNum;i++)
		{
			int texture=0;

			texture=initTexture(mv,texturesName[i],false);
			texList.put(texturesName[i],texture);
		}
	}
	public static int getTextures(String texName)
	{
		int result=0;
		if(texList.get(texName)!=null)
		{
			result=texList.get(texName);
		}else
		{
			result=-1;
		}
		return result;
	}
}
