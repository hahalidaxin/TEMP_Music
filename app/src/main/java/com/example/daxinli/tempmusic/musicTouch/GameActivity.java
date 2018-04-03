package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.screenscale.Constant;
import com.example.daxinli.tempmusic.util.screenscale.ScreenScaleUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameActivity extends BaseActivity {
    public MySurfaceView mySurfaceView;
    public static SharedPreferences.Editor editor;  //保存上次退出的保留
    public static SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mySurfaceView = new MySurfaceView(this);
        mySurfaceView.requestFocus();
        mySurfaceView.setFocusableInTouchMode(true);

        //加载资源文件
        initScreenData();               //初始化屏幕数据 为后续计算对应点做准备
        loadSettings();
        initIOFile();

        setContentView(mySurfaceView);                          //以SurfaceView作为主界面
    }

    public void exit() {            //暴露退出函数
        this.removeActivity();
    }
    public void initScreenData() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr= ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
        //System.out.println(Integer.toString(dm.widthPixels)+" "+Integer.toString(dm.heightPixels));
    }
    public void loadSettings() {
        sp = this.getSharedPreferences("settings", Context.MODE_PRIVATE);
        editor = sp.edit();
        //获得音效设置
        boolean isMusicOn = sp.getBoolean("MUSICEFFECT",true);      //default Value
        GameData.GameEffect = isMusicOn;
    }
    private void initIOFile()  {                         //加载音乐文件       存放在GameData中
        InputStream in=null;
        BufferedReader reader=null;
        StringBuffer tmpScore=new StringBuffer();
        try{
            in = getResources().getAssets().open("text/musicscore.txt");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            boolean tflag=true;
            while((line=reader.readLine())!=null) {
                if(tflag) {
                    synchronized (GameData.lock) {
                        GameData.MusicName=line;          //第一行 同步音乐名称
                    }
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

}

