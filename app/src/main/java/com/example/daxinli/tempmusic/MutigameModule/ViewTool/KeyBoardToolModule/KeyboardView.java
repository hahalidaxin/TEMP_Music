package com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule;

import android.content.Intent;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;
import com.example.daxinli.tempmusic.MutigameModule.ViewTool.BaseViewTool;
import com.example.daxinli.tempmusic.R;
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
    private static final String TAG = "KeyboardView";
    static final float BLACKKEYWIDTHRATIO = 0.0609f;
    static final float BLACKKEYHEIGHTRATIO = 0.61461f;
    static final float WHITEKEYWIDThRATIO = 0.125f;
    static final int[][] RESkeyMusic = new int[4][];
    public MusicScoreManager scmanager;


    Obj2DRectangle[] instruKeys = new Obj2DRectangle[14];
    Muti_SurfaceView mcontext;
    Obj2DRectangle imgKeyboard;


    private int nowKeyPressed;
    private int instruType;
    private String musicName;
    private float bckW ;
    private float bckHdown ;
    private float whiW ;
    private float[] blackkeylocX = {2.79f,8.04f,15.42f,20.21f,24.94f};
    private float[][] X = {
            {3.10f,5.75f,2.72f,5.47f},
            {8.08f,10.72f,8.08f,10.72f},
            {15.42f,18.04f,15.77f,18.52f},
            {20.07f,22.72f,20.74f,23.57f},
            {24.84f,27.41f,25.82f,28.54f}};
    private int[] funcKey = {
        0,1,3,5,6,8,10,12,13,2,4,7,9,11
    };
    private float imgVirW = 36.16f;
    private float imgVirH = 25.96f;
    private boolean initFlag = false;
    Area Ar;
    node keynode = new node(0,0,-1);


    public KeyboardView(Muti_SurfaceView context, Area imgArea, Intent intent) {
        this.mcontext = context;
        this.Ar = imgArea;
        this.instruType = intent.getIntExtra("instruType",-1);
        this.musicName = intent.getStringExtra("musicName");
    }

    @Override
    public void onInit(int x, int y, int w, int h) {
        super.onInit(x, y, w, h);
        nowKeyPressed = 0;
        Ar = new Area(x,y,w,h);
        bckW = w*BLACKKEYWIDTHRATIO;
        bckHdown = Ar.y+h*BLACKKEYHEIGHTRATIO;
        whiW = w*WHITEKEYWIDThRATIO;
        for (int i=0;i<14;i++) {
            String filename = String.format("pic_kb_r%d.png",i);
            if(TextureManager.getTextures(filename)==-1) {
                TextureManager.loadingTexture(mcontext,29,16);
            }
            instruKeys[i] = new Obj2DRectangle(Ar.x,Ar.y,Ar.width,Ar.height, TextureManager.getTextures(filename),
                    ShaderManager.getShader(2));
            instruKeys[i].setHP(true);
            instruKeys[i].setHP(true);
        }
        //piano
        RESkeyMusic[0] = new int[] {0,R.raw.piano_1, R.raw.piano_2, R.raw.piano_3, R.raw.piano_4, R.raw.piano_5, R.raw.piano_6,
                R.raw.piano_7, R.raw.piano_8, R.raw.piano_9, R.raw.piano_10, R.raw.piano_11, R.raw.piano_12, R.raw.piano_13};
        RESkeyMusic[1] = new int[] {0,R.raw.guitar_1,R.raw.guitar_2,R.raw.guitar_3,R.raw.guitar_4,R.raw.guitar_5,R.raw.guitar_6,
                R.raw.guitar_7,R.raw.guitar_8,R.raw.guitar_9,R.raw.guitar_10,R.raw.guitar_11,R.raw.guitar_12,R.raw.guitar_13};
        RESkeyMusic[2] = new int[] {0,R.raw.kick_1,R.raw.kick_2,R.raw.kick_3,R.raw.kick_4,R.raw.kick_5,R.raw.kick_6,R.raw.kick_7,
                R.raw.kick_8,R.raw.kick_9,R.raw.kick_10,R.raw.kick_11,R.raw.kick_12,R.raw.kick_13};
        RESkeyMusic[3] = new int[] {0,R.raw.bell_1,R.raw.bell_2,R.raw.bell_3,R.raw.bell_4,R.raw.bell_5,R.raw.bell_6,R.raw.bell_7,
                R.raw.bell_8,R.raw.bell_9,R.raw.bell_10,R.raw.bell_11,R.raw.bell_12,R.raw.bell_13};
        for(int i=0;i<5;i++) {                           //按照当前安排位置，计算当前黑键处于屏幕中的位置
            for (int j=0;j<4;j++) {
                X[i][j] = Ar.x+Ar.width* (X[i][j])/imgVirW;
            }
        }
        this.scmanager = new MusicScoreManager(mcontext);
        this.scmanager.onStart();
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
                int ans = -1;
                //检查是否是黑键
                int i;
                float dy = y-Ar.y;
                float H = bckHdown-Ar.y;
                for (i = 0; i < 5; i++) {
                    float x1 = (X[i][2]-X[i][0])/(H)*(y-Ar.y)+X[i][0];
                    float x2 =  (X[i][3]-X[i][1])/(H)*(y-Ar.y)+X[i][1];
                    if (y >= Ar.y && y <= bckHdown && x >=x1 && x <=x2) {
                        ans = i;
                        break;
                    }
                }
                if (i< 5) ans = i + 9;
                else {
                    //检查白键
                    for (i = 0; i < 8; i++)
                        if (SFUtil.isin(x, y, new Area(Ar.x + i * whiW, Ar.y, whiW, Ar.height))) break;
                    if (i < 8) ans = i + 1;
                }
                //点中了琴键 需要产生乐谱并且反馈一个音节
                if(ans!=-1) onKeyPress(ans);
                break;
            case MotionEvent.ACTION_UP:
                //触摸抬起
                // TODO: 2018/6/1  终止音效播放
                //向viewTool中传入一个音节已经完成的信息 停止显示
                //将此音节信息传入产生的乐谱文件
                //scoreManager.onWrite();
                onKeyUp();
                break;
        }
        return true;
    }

    @Override
    public void onDraw() {
        if(!initFlag) {
            initFlag = true;
            onInit((int)Ar.x,(int)Ar.y,(int)Ar.width,(int) Ar.height);
        }
        if(instruKeys[nowKeyPressed]!=null)
            instruKeys[nowKeyPressed].drawSelf();
    }

    public void onKeyPress(int key) {
        this.keynode.st = System.currentTimeMillis();
        this.keynode.key= key;
        nowKeyPressed = key;
        //播放音效 调用不同的特效
        int thisP = funcKey[key];

        mcontext.mcontext.sound.playMusic(RESkeyMusic[instruType][thisP],0);

        //切换显示图片
        //同时需要结合抬起动作
    }
    public void onKeyUp() {
        this.nowKeyPressed = 0;
        long time = System.currentTimeMillis();
        this.keynode.ed = time;
        this.scmanager.onKey(this.keynode.st,this.keynode.ed,this.keynode.key);
    }
    private class node {
        long st,ed;
        int key;
        public node(long st,long ed,int key) {
            this.st = st; this.ed = ed; this.key = key;
        }
    }
}

