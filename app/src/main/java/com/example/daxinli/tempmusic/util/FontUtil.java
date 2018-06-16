package com.example.daxinli.tempmusic.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class FontUtil
{
	static int cIndex=0;
	static float textSize=40;
	static int R=255;
	static int G=255;
	static int B=255;
	static int A = 255;

	//在制定位置生成制作指定大的bitmap
    //利用canvas的drawTextAPI生成含有指定文字的bitmap
    public static void setRGB(int r,int g,int b,int a) {
        R = r; G = g; B = b; A = a;
    }
    public static void setTextSize(float size) {
        textSize = size;
    }
	public static Bitmap generateWLT(String str,int x,int y,int width,int height)
	{
		Paint paint=new Paint();
		paint.setARGB(A, R, G, B);
		paint.setTextSize(textSize);
		paint.setTypeface(null);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		Bitmap bmTemp=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvasTemp = new Canvas(bmTemp);
        canvasTemp.drawText(str, x,y, paint);
		return bmTemp;
	}
}