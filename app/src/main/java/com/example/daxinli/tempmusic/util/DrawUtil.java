package com.example.daxinli.tempmusic.util;

import com.example.daxinli.tempmusic.MatrixState.MatrixState2D;
import com.example.daxinli.tempmusic.object.BN2DObject;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/3/19.
 */

public class DrawUtil {
    //绘制Bitmap
    public static void drawBitmap(float x,float y,float width,float height,String pname) {
        BN2DObject bitmap = new BN2DObject(x,y,width,height, TextureManager.getTextures(pname),
                ShaderManager.getShader(2));
        bitmap.drawSelf();
    }
    //绘制纯色方块
    public static  void drawRect(float x,float y,float width,float height,float a,float r,float g,float b) {
        BN2DObject bitmap = new BN2DObject(x,y,width,height,a,r,g,b,            // TODO: 2018/3/19 新建一个shader  用于接收argb绘制相应颜色方块
                ShaderManager.getShader(6));
        bitmap.drawSelf();
    }
    //绘制长方块
    // TODO: 2018/3/20 添加其他细节
    public static void drawLongSlide( float loadX, float loadY ,float width, float height) {
        DrawUtil.drawRect(loadX,loadY,width,height,0.2f,66/255,1,179/255);
    }
    //绘制长方形方块的阴影
    public static void drawLongSlideEffect(float startX, float startY, float width, float height) {
        //openGl中的颜色覆盖设置？？？
        DrawUtil.drawRect(startX,startY,width,height,1f,159/255,1,22/255);
    }
    //绘制特效相关

    static String[] numsF={"0.png","1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png"};
    public static void drawNumber(float x,float y,float numwidth,float numheight,int num) {
        String str_num = Integer.toString(num);
        for(int i=0;i<str_num.length();i++) {
            char c = str_num.charAt(i);
            int ni = c - '0';
            BN2DObject obj = new BN2DObject(x+i*numwidth,y,numwidth,numheight, TextureManager.getTextures(numsF[ni]),
                    ShaderManager.getShader(2));
            obj.drawSelf();
        }
    }
}
