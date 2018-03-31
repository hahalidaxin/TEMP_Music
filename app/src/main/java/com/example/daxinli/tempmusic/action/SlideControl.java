package com.example.daxinli.tempmusic.action;


import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.util.effect.RedHeart.RedHeart;
import com.example.daxinli.tempmusic.view.GameView;

import java.util.ArrayList;

/**
 * Created by hahal on 2018/2/26.
 * 滑块控制类 主要负责产生滑块 滑块的运行 滑块的销毁
 */

public class SlideControl extends Action {
    ArrayList<MainSlide> alTemp = new ArrayList<MainSlide>();//临时滑块列表
    RedHeart redHeart;
    private float baseHeight;

    @Override
    public void doAction() {
        alTemp = new ArrayList<MainSlide>();
        synchronized (GameView.lock) {          //lock位于GameData中 同时只能被一步调用
            for(int i=0;i<GameView.mainSlideArrayList.size();i++) {
                alTemp.add(GameView.mainSlideArrayList.get(i));
            }
            redHeart = GameView.redHeart;
            baseHeight=GameData.STANDARD_HIEGHT;
        }
        if(redHeart!=null) redHeart.go();
        for(int i=0;i<GameView.mainSlideArrayList.size();i++) {
            MainSlide slide = GameView.mainSlideArrayList.get(i);
            slide.go();                                     //滑块进行下滑

            if(slide.Y>baseHeight)
                alTemp.remove(i);
            else {
                if(slide.state==0 && slide.Y+slide.Height-baseHeight>slide.Height/4) { //当滑块进入1/4算作没有触摸
                    synchronized (GameData.lock) {
                        GameData.gamerHealth-=1;
                    }
                    broadCastShowHealth();      //需要提醒主线程显示生命值
                    slide.state=2;
                }
            }
        }
        synchronized (GameView.lock) {                      //删除
            GameView.mainSlideArrayList = new ArrayList<MainSlide>();
            for(int i=0;i<alTemp.size();i++) {
                GameView.mainSlideArrayList.add(alTemp.get(i));
            }
        }
        if (GameData.gamerHealth <= 0) {           //减去生命值
            synchronized (GameData.lock) {
                broadCastGameOver();
            }
        }
    }
    public void broadCastGameOver() {   GameView.Message = 3; }
    public void broadCastShowHealth() { GameView.Message = 4; }
}
