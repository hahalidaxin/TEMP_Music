package com.example.daxinli.tempmusic.view;

import android.view.MotionEvent;

import com.example.daxinli.tempmusic.MySurfaceView;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.Background;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.object.Obj2DRectangle;
import com.example.daxinli.tempmusic.thread.ActionThread;
import com.example.daxinli.tempmusic.thread.CreateSlideThread;
import com.example.daxinli.tempmusic.thread.MainSlideThread;
import com.example.daxinli.tempmusic.util.SFUtil;
import com.example.daxinli.tempmusic.util.effect.ElseEffect.DrawScore;
import com.example.daxinli.tempmusic.util.effect.RedHeart.RedHeart;
import com.example.daxinli.tempmusic.util.effect.TriangleFirework.tri_ParticleSystem;
import com.example.daxinli.tempmusic.util.elseUtil.Area;
import com.example.daxinli.tempmusic.util.manager.ShaderManager;
import com.example.daxinli.tempmusic.util.manager.TextureManager;
import com.example.daxinli.tempmusic.util.screenscale.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daxin Li on 2018/3/19.
 */

public class GameView extends BaseView {
        private static final String TAG = "GameView";
        public static Object lock = new Object();                 //GameView共享数据的锁
        MySurfaceView father;

        public ActionThread actionThread;               //将线程暴露给GameActivity
        public CreateSlideThread createSlideThread;
        public MainSlideThread mainSlideThread;

        List<Obj2DRectangle> viewlist=new ArrayList<Obj2DRectangle>();
        ArrayList<MainSlide> tmpSlide = new ArrayList<MainSlide>();
        public static ArrayList<MainSlide> mainSlideArrayList = new ArrayList<MainSlide>();     //声明为全局可用的静态变量

        DrawScore scoreDraw;
        public static RedHeart redHeart;            //暴露出来 需要在MainSlideTH中进行更新
        public static RedHeart redHeartShowHealth;
        public Background background ;
        public tri_ParticleSystem trisys;
        Random random = new Random();

        public static int Message=0;          //
        boolean initFlag = false;
        public boolean isThClose = false;


        long lastTime=0;

        public GameView(MySurfaceView father) {
            this.father = father;
            initView();
            initThread();
            initGameData();
        }

        @Override
        public void initView() {
            //设置Area
            GameData.area_btn_pause = new Area(0,20,120,120);
            //初始化纹理
            TextureManager.loadingTexture(father,0,29);           //加载游戏界面相关图片
            scoreDraw = new DrawScore();
            background = new Background();
            //加载恒参图片)
            Area ar = GameData.area_btn_pause;
            GameData.area_btn_pause = ar;
            viewlist.add(new Obj2DRectangle(ar.x,ar.y,ar.width,ar.height
                    ,TextureManager.getTextures("btn_pause_g.png"), ShaderManager.getShader(2)));
            viewlist.add(new Obj2DRectangle(ar.x,ar.y,ar.width,ar.height
                    ,TextureManager.getTextures("btn_start_g.png"), ShaderManager.getShader(2)));
            initFlag = true;
        }
        private void initGameData() {
            mainSlideArrayList = new ArrayList<MainSlide>();
            redHeart = null;
            background = new Background();
            trisys = null;
            Message = 0;
            GameData.aq.clear();
            GameData.GameScore = 0;
            GameData.GameRK = 0;
            GameData.gamerHealth=GameData.initgamerHealth;
        }
        private void initThread() {     //开启游戏线程
            createSlideThread = new CreateSlideThread(this);
            actionThread = new ActionThread();
            mainSlideThread = new MainSlideThread();

            createSlideThread.start();
            actionThread.start();
            mainSlideThread.start();
        }
        public void closeThread() {
            if(createSlideThread!=null) createSlideThread.setFlag(false);
            if(actionThread!=null) actionThread.setFlag(false);
            if(mainSlideThread!=null) mainSlideThread.setFlag(false);
            createSlideThread.interrupt();          //解决线程阻塞问题 抛出异常 继续进行
            actionThread.interrupt();
            mainSlideThread.interrupt();
            isThClose = true;
        }
        public void sorPThread() { //对线程的暂停标志位进行翻转
            createSlideThread.turnPause();
            actionThread.turnPause();
            mainSlideThread.turnPause();
        }
        public void turnToView(int x) {
            //关闭所有线程
            closeThread();
            isThClose = true;           //设置线程关闭位
            switch(x) {
                case 1:
                    //将主界面设置为gameoverView
                    MySurfaceView.curView = MySurfaceView.gameoverView;
                    break;
                case 2:
                    MySurfaceView.curView = MySurfaceView.gameVictoryView;
                    break;
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent e) {                //需要将返回值设为true 否则不能继续触发move以及up函数
            //long lastTime = System.currentTimeMillis();
            //对触摸事e进行检查 首先为otherView的控件 其次为滑块
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
                    break;
            }
            if(redHeart!=null) redHeart.onTouch(e);                                         //小红心设计触摸事件
            if(!MySurfaceView.isPause) {
                final MotionEvent tmpe = e;
                synchronized (GameView.lock) {
                    for(MainSlide slide:mainSlideArrayList) {
                        if(slide.state==0) {
                            slide.onTouchEvent(tmpe);
                            if(slide.getIsGetScore()) {
                                scoreDraw.runAnim();
                            }
                            if(slide.type == 2) {
                                slide.getScoreDraw(scoreDraw);
                            }
                            break;
                        } else if(slide.type!=1) {                     //如果最下方是长滑块的话还应该对触摸事件进行相应
                            slide.onTouchEvent(tmpe);                 //长滑块的动画触发放在内部执行 不在考虑
                        }
                    }
                }
            }
            return true;                //返回值设置为true，因此后续的事件可以继续进行
        }
        public void restartInitOp() {       //暴露一个初始化函数 由外部调用
            initGameData();
            initThread();
            isThClose = false;
        }
        @Override
        public void drawView(GL10 gl) {
            long begin = System.currentTimeMillis();
            if(!initFlag) {
                initView();
                initFlag = true;
            }
            //捕捉来自其他线程的Message  进行UI界面的相应更新
            switch(Message) {
                case 0:
                    break;
                case 1:
                    //播放烟花特效
                    int randomInt = random.nextInt(2)+1;
                    trisys = new tri_ParticleSystem(randomInt,540,1800);
                    Message = 0;
                    break;
                case 2:
                    //切换游戏背景
                    background.switchBG();
                    Message = 0;
                    break;
                case 4:
                    //显示生命值
                    if(redHeartShowHealth==null) redHeartShowHealth = new RedHeart(GameData.redHeart_W,GameData.redHeart_H);
                    else if(redHeartShowHealth.getIsDead()) { redHeartShowHealth.restart(); }
                    Message = 0;
                    break;
                case 5:
                    //游戏胜利
                    turnToView(2);
                    break;
            }

            //游戏背景类
            background.drawSelf();
            if(trisys!=null) {
                if(trisys.isSysEnd()) {
                    trisys = null;
                } else{
                    trisys.drawSelf();
                }
            }
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
            scoreDraw.drawSelf(GameData.GameScore);
            if(redHeart!=null && !redHeart.getIsDead()) {
                redHeart.drawSelf();
            }
            if(redHeartShowHealth!=null && !redHeartShowHealth.getIsDead()) {
                redHeartShowHealth.drawSelf();
            }
            if(MySurfaceView.isPause) {
                viewlist.get(1).drawSelf();
            } else {
                viewlist.get(0).drawSelf();
            }
            synchronized (GameData.lock) {
                GameData.refreshFrameTime = System.currentTimeMillis()-begin;
            }
            // TODO: 2018/3/29 这里可以添加一部分的保留措施
            //游戏结束时保留最后一帧 这样不会显得太突兀
            if(Message == 3) {
                //游戏结束
                turnToView(1);
                Message = 0;
                return;
            }
        }

}