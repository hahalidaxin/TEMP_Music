package com.example.daxinli.tempmusic.thread;

import com.example.daxinli.tempmusic.action.Action;
import com.example.daxinli.tempmusic.action.SlideControl;
import com.example.daxinli.tempmusic.constant.GameData;

/**
 * Created by hahal on 2018/2/26.
 * SlideThread进行滑块操作类的刷帧
 * 根据曲目信息进行刷帧操作
 */

public class MainSlideThread extends Thread {
    private boolean pause = false;
    private boolean flag=true;
    private long start;
    private long end;
    private long runSpan;
    public MainSlideThread() {
        this.setName("MainSideThread");
    }
    @Override
    public void run() {                                                 //每sleepSpan的时间就会对滑块的状态进行更新
        while(flag) {
            if(!pause) {
                start= System.currentTimeMillis();
                if(GameData.viewState==GameData.Game_playing) {
                    Action a=new SlideControl();                        //不停的进行滑块控制类操作
                    synchronized(GameData.lock) {
                        GameData.aq.offer(a);
                    }
                }
                end= System.currentTimeMillis();
                runSpan=end-start;
                if(pause) continue;
                try{
                    if(runSpan<GameData.MainSlideTHSpan)
                        Thread.sleep(GameData.MainSlideTHSpan-runSpan);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setPause(boolean pause) { this.pause = pause; }
    public void setFlag(boolean flag) { this.flag = flag ; }
    public void turnPause() { this.pause = !this.pause; }
}
