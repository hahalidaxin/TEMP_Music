package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.manager.SoundManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameActivity extends BaseActivity {
    private static final String TAG = "GameActivity";
    public MySurfaceView mySurfaceView;
    public static SharedPreferences.Editor editor;  //保存上次退出的保留
    public static SharedPreferences sp;
    public SoundManager sound;

    public String musicName;

    public int instruType;          //在surfaceView中需要获取到的instru的类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mySurfaceView = new MySurfaceView(this);
        mySurfaceView.requestFocus();
        mySurfaceView.setFocusableInTouchMode(true);

        sound = new SoundManager(this);
        //加载资源文件
        initScreenData();               //初始化屏幕数据 为后续计算对应点做准备
        loadSettings();

        String filename = getIntent().getStringExtra("musicName");
        musicName = filename;
        if(filename!=null) initIOFile(filename);

        setContentView(mySurfaceView);                          //以SurfaceView作为主界面
    }

    public void exit() {            //暴露退出函数
        this.removeActivity();
    }
    public void initScreenData() {
        //避免状态栏和虚拟按键的干扰 获得真实显示的屏幕信息
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(point);
        Log.d(TAG,"the screen size is "+point.toString());
        Constant.ssr= ScreenScaleUtil.calScale(point.x, point.y);
    }
    public void loadSettings() {
        sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        //获得音效设置
        boolean isMusicOn = sp.getBoolean("MUSICEFFECT",true);      //default Value
        GameData.GameEffect = isMusicOn;
    }
    private void initIOFile(String musicName)  {                         //加载音乐文件       存放在GameData中
        InputStream in=null;
        BufferedReader reader=null;
        StringBuffer tmpScore=new StringBuffer();
        try{
            in = getResources().getAssets().open("text/music/"+musicName);
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            boolean tflag=true;
            while((line=reader.readLine())!=null) {
                if(tflag) {
                    this.instruType = Integer.parseInt(line.trim());
                    tflag=false;
                }
                else
                    tmpScore.append(line+"#");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            synchronized (GameData.lock) {
                GameData.mainMusicScore=new StringBuffer(tmpScore.toString());
            }
            try{
                if(reader!=null) {
                    reader.close();
                }
                if(in!=null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void onDataSaved(Intent intent) {
        //对游戏成绩进行保存
        SharedPreferences.Editor editor = getSharedPreferences("music",MODE_PRIVATE).edit();
        editor.putInt(musicName,intent.getIntExtra("score",-1));
        editor.apply();
    }

}

