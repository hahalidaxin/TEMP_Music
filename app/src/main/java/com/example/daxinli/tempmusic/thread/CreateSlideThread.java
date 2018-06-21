package com.example.daxinli.tempmusic.thread;


import android.util.Log;

import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.util.effect.RedHeart.RedHeart;
import com.example.daxinli.tempmusic.util.elseUtil.Pitch;
import com.example.daxinli.tempmusic.view.GameView;

import java.util.Random;

/**
 * Created by hahal on 2018/2/27.
 */

public class CreateSlideThread extends Thread {
    private static final String TAG = "CreateSlideThread";
    public String MusicScore;
    public String[] MsArray;
    private String[] MsPitchInfo;
    private StringBuffer tmpBuffer;

    private Random random=new Random();

    public float createSpan;
    public long nowclockTime;               //记录乐曲演奏的总时间
    private float baseSlideHeight,baseSlideWidth,baseHight,speedRK;                    //需要获取的GameData中的数据
    private int lastRandomInt=0;
    private int curRandomInt;
    private int currentTime,currentPitch,endPitch,loopTimes;
    private Pitch thisPitch;
    private boolean pause = false;
    private boolean flag=true;
    private long attachSpan = 0;        //因暂停原因产生的需要补上的睡眠时间
    private boolean occurRedHeart;

    public int state;               //曲子播放的状态
    private GameView gameView;
    int randomInitRedHeartTime;

    public CreateSlideThread(GameView gameView) {
        this.gameView = gameView;
        this.setName("CreateSlideThread");
    }

    @Override
    public void run() {
        initData();
        createSpan = 10;
        while(flag) {                                                   //createSpan应该根据上一个滑块的长度来计算
            if(!pause) {
                //补足时间
                if(attachSpan!=0) {
                    try {
                        Thread.sleep(attachSpan);
                        attachSpan = 0;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                long start= System.currentTimeMillis();
                //进行文件操作 检查产生滑块
                float rt = GameData.gameSpeed[0]/GameData.gameSpeed[GameData.GameRK];
                if (nowclockTime >= thisPitch.st*rt) {
                    createNewSlide(thisPitch);
                    GameData.gameProgressRatio = (float)currentPitch/endPitch*0.25f+0.25f*(float)loopTimes;
                    if((++currentPitch)==endPitch) {
                        //如果音乐循环结束
                        if((++loopTimes)<4) {
                            currentPitch = 0;               //重新返回第一个音
                            nowclockTime = 0;
                            occurRedHeart = false;
                            synchronized(GameData.lock) {   //游戏难度提升
                                if(GameData.GameRK<GameData.gameSpeed.length-1)
                                    speedRK=GameData.gameSpeed[++GameData.GameRK];
                            }
                        } else {
                            try {
                                Thread.sleep(GameData.sleepSpanPerDiff);
                            } catch(InterruptedException e) {
                                e.printStackTrace();
                            }
                            broadCastGameVictory();
                            return ;
                        }
                        //此处一次播放完成
                        state++;
                        //休眠形成空屏效果
                        try {
                            Thread.sleep(2000);
                            broadCastShowFirework();
                            Thread.sleep(GameData.sleepSpanPerDiff-1000);
                            broadCastSwitchBG();
                            Thread.sleep(1000);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    MsPitchInfo = MsArray[currentPitch].split(" ");                     //获得此音节的音节信息
                    thisPitch = new Pitch(Integer.parseInt(MsPitchInfo[0]), Integer.parseInt(MsPitchInfo[1])
                            , Integer.parseInt(MsPitchInfo[2]));
                }

                //红心的init初始设定
                if(!occurRedHeart && currentPitch>=randomInitRedHeartTime) {
                    occurRedHeart = true;
                    Log.e(TAG, "run: 释放了红心" );
                    int col;
                    while((col=random.nextInt(4))==lastRandomInt) {}
                    float speed = GameData.gameSpeed[GameData.GameRK]*GameData.MainSlideTHSpan;
                    synchronized (gameView.lock) {
                        gameView.redHeart = new RedHeart(col * baseSlideWidth + (GameData.baseSlideWidth-GameData.redHeart_W)/2, -GameData.redHeart_H/2-baseSlideHeight/2,
                                GameData.redHeart_W, GameData.redHeart_H, speed);
                    }
                }

                long end= System.currentTimeMillis();
                if(end-start<createSpan) {
                    try {
                        Thread.sleep((long)createSpan-(end-start));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                nowclockTime += createSpan;
                if(pause) {
                    attachSpan = System.currentTimeMillis()-GameData.gamepauseTime;
                } else attachSpan = 0;
            }
        }
    }
    public void createNewSlide(Pitch tp) {                              //产生一个新的滑块 放入Data的滑块队列
        while ((curRandomInt = random.nextInt(4)) == lastRandomInt) {}
        lastRandomInt=curRandomInt;
        //tp.span代表了滑块所处的类型 因此要求音节以单位化直接反应音节的长短
        int sh =(int) ((tp.ed-tp.st)*GameData.gameSpeed[0]);
        MainSlide slide = new MainSlide(gameView.mactivity,curRandomInt*baseSlideWidth,-sh,
                baseSlideWidth, sh,tp.key,1,0);
        synchronized (GameView.lock) {
            GameView.mainSlideArrayList.add(slide);
        }
    }
    public void initData() {
        synchronized (GameData.lock) {                                              //同步GameData中的游戏数据
            tmpBuffer = new StringBuffer(GameData.mainMusicScore.toString());       //这里获取了乐谱的string
            baseSlideHeight = GameData.baseSlideHeight;
            baseSlideWidth = GameData.baseSlideWidth;
            if(GameData.GameRK!=0) GameData.GameRK = 0;
            speedRK = GameData.gameSpeed[GameData.GameRK];                                       //当前游戏难度的游戏速度
            baseHight = GameData.STANDARD_HIEGHT;

        }
        nowclockTime = 0;
        attachSpan = 0;
        pause = false;
        flag = true;
        state = 0;
        MusicScore=tmpBuffer.toString();
        MsArray=MusicScore.split("#");
        currentTime=-1; currentPitch=0; loopTimes=0;
        endPitch=MsArray.length;
         //根据字符串 解析每一个音节 音调 出现 时长
        MsPitchInfo=MsArray[currentPitch].split(" ");
        thisPitch=new Pitch(Integer.parseInt(MsPitchInfo[0].trim()), Integer.parseInt(MsPitchInfo[1].trim())
                , Integer.parseInt(MsPitchInfo[2].trim()));

        if(MsArray.length>5)
            randomInitRedHeartTime =(random.nextInt(MsArray.length));
        Log.e(TAG, "initData: "+Integer.toString(randomInitRedHeartTime) );
    }

    public void setFlag(boolean flag) { this.flag = flag ; }
    public void turnPause() {
        if(pause) GameData.gamerestartTime = System.currentTimeMillis();
        else GameData.gamepauseTime = System.currentTimeMillis();
        this.pause = !this.pause;
    }
    public void broadCastSwitchBG() { GameView.Message = 2; }
    public void broadCastShowFirework() { GameView.Message = 1; }
    public void broadCastGameVictory() { GameView.Message = 5; }
}