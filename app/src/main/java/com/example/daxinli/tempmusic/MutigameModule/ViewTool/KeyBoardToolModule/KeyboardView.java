package com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.Activity.Manager.MusicScoreManager;
import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;

/**
 * Created by Daxin Li on 2018/5/31.
 * 键盘的view类
 */

public class KeyboardView extends BaseViewTool {
    static final float BLACKKEYWIDTHRATIO = 0.0609f;
    static final float BLACKKEYHEIGHTRATIO = 0.61461f;
    static final float WHITEKEYWIDThRATIO = 0.125f;
    Obj2DRectangle[] instruKeys = new Obj2DRectangle[14];
    Muti_SurfaceView mcontext;
    Obj2DRectangle imgKeyboard;
    MusicScoreManager scoreManager;

    private int nowKeyPressed;
    private int instruType;
    private float bckW ;
    private float bckHdown ;
    private float whiW ;
    private float[] blackkeylocX = {2.79f,8.04f,15.42f,20.21f,24.94f};
    private float imgVirW = 36.16f;
    Area Ar;


    public KeyboardView(Muti_SurfaceView context, Area imgArea,int intruType) {
        this.mcontext = context;
        this.Ar = imgArea;
        this.instruType = intruType;
        onInit((int)(imgArea.x),(int)(imgArea.y),(int)(imgArea.width),(int)(imgArea.height));
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        nowKeyPressed = 0;
        Ar = new Area(x,y,w,h);
        bckW = w*BLACKKEYWIDTHRATIO;
        bckHdown = h*BLACKKEYHEIGHTRATIO;
        whiW = w*WHITEKEYWIDThRATIO;
        for (int i=0;i<14;i++) {
            String filename = String.format("pic_kb_r%d.png",i);
            instruKeys[i] = new Obj2DRectangle(Ar.x,Ar.y,Ar.width,Ar.height, TextureManager.getTextures(filename),
                    ShaderManager.getShader(2));
            instruKeys[i].setHP(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //触摸按下
                int ans = -1;
                //检查是否是黑键
                int i;
                for (i = 0; i < 5; i++)
                    if (SFUtil.isin(x, y, new Area(Ar.x + blackkeylocX[i] / imgVirW * Ar.width, Ar.y, bckW, bckHdown))) {
                        break;
                    }
                if (i != 5) ans = i + 9;

                for (i = 0; i < 8; i++)
                    if (SFUtil.isin(x, y, new Area(Ar.x + i * whiW, Ar.y, whiW, Ar.height))) break;
                if (i != 8) ans = i + 1;
                //点中了琴键 需要产生乐谱并且反馈一个音节
                onKeyPress(ans);
                break;
            case MotionEvent.ACTION_UP:
                //触摸抬起
                // TODO: 2018/6/1  终止音效播放
                //向viewTool中传入一个音节已经完成的信息 停止显示
                //将此音节信息传入产生的乐谱文件
                scoreManager.onWrite();
                break;
        }
        return true;
    }

    @Override
    public void onDraw() {
        instruKeys[nowKeyPressed].drawSelf();
    }

    public void onKeyPress(int key) {
        //播放音效 调用不同的特效
        switch(this.instruType) {
            case 0:
                //piano
                break;
            case 1:
                //instru1
                break;
            case 2:
                //instru2
                break;
            case 3:
                //instru3
                break;
        }
        //切换显示图片
        //同时需要结合抬起动作
    }
}

