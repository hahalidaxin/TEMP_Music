package com.example.daxinli.tempmusic.view;

import android.util.Log;
import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.BN2DObject;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.thread.ActionThread;
import com.example.daxinli.tempmusic.thread.CreateSlideThread;
import com.example.daxinli.tempmusic.thread.MainSlideThread;
import com.example.daxinli.tempmusic.util.DrawUtil;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/19.
 */

public class GameView extends BaseView {
        private static final String TAG = "GameView";
        public static Object lock = new Object();                 //GameView共享数据的锁
        MySurfaceView father;

        ActionThread actionThread;
        CreateSlideThread createSlideThread;
        MainSlideThread mainSlideThread;

        List<BN2DObject> viewlist=new ArrayList<BN2DObject>();
        ArrayList<MainSlide> tmpSlide = new ArrayList<MainSlide>();
        public static ArrayList<MainSlide> mainSlideArrayList = new ArrayList<MainSlide>();     //声明为全局可用的静态变量
        boolean initFlag = false;
        public static boolean isGameOver = false;
        boolean isThClose = false;

        public GameView(MySurfaceView father) {
            this.father = father;
            initThread();
            initView();
        }

        @Override
        public void initView() {
            //设置Area
            GameData.area_btn_pause = new Area(960,20,120,120);
            GameData.area_pic_rheart = new Area(0,20,120,120);

            //初始化纹理
            TextureManager.loadingTexture(father,0,14);           //加载游戏界面相关图片
            //加载恒参图片
            Area ar = GameData.area_btn_pause;
            GameData.area_btn_pause = ar;
            viewlist.add(new BN2DObject(ar.x,ar.y,ar.width,ar.height
                    ,TextureManager.getTextures("btn_pause_g.png"), ShaderManager.getShader(2)));
            ar = GameData.area_pic_rheart;
            viewlist.add(new BN2DObject(ar.x,ar.y,ar.width,ar.height
                    ,TextureManager.getTextures("pic_rheart_g.png"), ShaderManager.getShader(2)));

            initFlag = true;
        }
        private void initGameData() {
            mainSlideArrayList.clear();
            GameData.aq.clear();
            GameData.GameScore = 0;
            GameData.GameRK = 0;
            GameData.gamerHealth  = GameData.initgamerHealth;

            isGameOver = false;
        }
        private void initThread() {     //开启游戏线程
            createSlideThread = new CreateSlideThread();
            actionThread = new ActionThread();
            mainSlideThread = new MainSlideThread();

            createSlideThread.start();
            actionThread.start();
            mainSlideThread.start();
        }
        private void closeThread() {
            createSlideThread.setFlag(false);
            actionThread.setFlag(false);
            mainSlideThread.setFlag(false);
        }
        public void sorPThread() { //对线程的暂停标志位进行翻转
            createSlideThread.turnPause();
            actionThread.turnPause();
            mainSlideThread.turnPause();
        }
        public void turnToView(int x) {
            switch(x) {
                case 1:
                    //关闭所有线程
                    closeThread();
                    isThClose = true;           //设置线程关闭位
                    //将主界面设置为gameoverView
                    MySurfaceView.curView = MySurfaceView.gameoverView;
                    break;
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent e) {                //需要将返回值设为true 否则不能继续触发move以及up函数
            //对触摸事件进行检查 首先为otherView的控件 其次为滑块
            float x = e.getX();
            float y = e.getY();
            x = Constant.fromRealScreenXToStandardScreenX(x);
            y = Constant.fromRealScreenYToStandardScreenY(y);
            switch(e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(SFUtil.isin(x,y,GameData.area_btn_pause)) {
                        //点击暂停按钮
                        //将按钮的点击间隔强制设为1s 否则会导致createSLideTh时间的不精准
                        if(System.currentTimeMillis()-GameData.gamerestartTime<1000) return true;
                        synchronized (GameData.lock) {
                            GameData.aq.clear();
                        }
                        sorPThread();
                        MySurfaceView.isPause = !MySurfaceView.isPause;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e(TAG, "onTouchEvent: ACTION_UP");
                    break;
            }
            if(!MySurfaceView.isPause) {
                final MotionEvent tmpe = e;
                synchronized (GameView.lock) {
                    for(MainSlide slide:mainSlideArrayList) {
                        if(slide.state==0) {
                            slide.onTouchEvent(tmpe);
                            break;
                        } else if(slide.type!=1) {                     //如果最下方是长滑块的话还应该对触摸事件进行相应
                            slide.onTouchEvent(tmpe);
                        }
                    }
                }
            }
            return true;                //返回值设置为true，因此后续的事件可以继续进行
        }

        @Override
        public void drawView(GL10 gl) {
            if(!initFlag) {
                initView();
                initFlag = true;
            }
            if(isThClose) {     //返回游戏 重新开启游戏线程 清空游戏数据
                initThread();
                initGameData();
                isThClose = false;
            }
            //游戏界面的绘制    背景  游戏辅助 滑块
            //绘制分数
            //注意此处需要进行同步限制 因为修改是有可能同时发生

            long begin = System.currentTimeMillis();
            tmpSlide.clear();
            synchronized (GameView.lock) {
                for (int i = 0; i < GameView.mainSlideArrayList.size();i++) {
                    tmpSlide.add(GameView.mainSlideArrayList.get(i));
                }
            }
            for(int i=0;i<tmpSlide.size();i++) {
                tmpSlide.get(i).drawSelf();
            }
            //view参数固定不动的不需要再次进行重建 应该直接在initView中计算保存在viewlist中
            String str_score = Integer.toString(GameData.GameScore);
            int length = str_score.length();
            float loadX = (GameData.STANDARD_WIDTH-length*GameData.num_W)/2;
            DrawUtil.drawNumber(loadX,20,(float)GameData.num_W,(float)GameData.num_H,GameData.GameScore);
            if(viewlist.size()>0) {
                for(BN2DObject obj:viewlist) {
                    obj.drawSelf();
                }
            }
            //在drawView之前进行检查是否已经gameOver   
            // TODO: 2018/3/22 此处延迟一帧
            if(isGameOver) {
                isGameOver = false;
                turnToView(1);
                return ;
            }
        }

}

