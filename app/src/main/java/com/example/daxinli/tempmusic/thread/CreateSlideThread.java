package com.example.daxinli.tempmusic.thread;


import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.object.MainSlide;
import com.example.daxinli.tempmusic.util.elseUtil.item;
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
    private float lastSlideHeight;
    private float baseSlideHeight,baseSlideWidth,baseHight,speedRK;                    //需要获取的GameData中的数据
    private int lastRandomInt=0;
    private int curRandomInt;
    private int currentTime,currentPitch,endPitch,loopTimes;
    private item thisPitch;
    private boolean pause = false;
    private boolean flag=true;
    private long attachSpan = 0;        //因暂停原因产生的需要补上的睡眠时间


    public CreateSlideThread() {
        this.setName("CreateSlideThread");
    }

    @Override
    public void run() {
        init();
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
                createSpan= GameData.SlideHeight[1]/speedRK;         //创建滑块的时间单位 每时间单位的时长 - 根据游戏难度改变
                //进行文件操作 检查产生滑块
                if (++currentTime == thisPitch.occur) {
                    createNewSlide(thisPitch);
                    if((++currentPitch)==endPitch) {
                        //如果音乐循环结束
                        if((++loopTimes)<3) {
                            currentPitch = 0;               //重新返回第一个音
                            currentTime = -1;
                            synchronized(GameData.lock) {   //游戏难度提升
                                speedRK=GameData.gameSpeed[++GameData.GameRK];
                            }
                        } else {
                            // TODO: 2018/3/1 游戏胜利的处理
                            currentPitch=0;
                            currentTime=-1;
                        }
                        //休眠2s 形成空屏的效果
                        try {
                            Thread.sleep(2000);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    MsPitchInfo = MsArray[currentPitch].split(" ");                     //获得此音节的音节信息
                    thisPitch = new item(Integer.parseInt(MsPitchInfo[0]), Integer.parseInt(MsPitchInfo[1])
                            , Integer.parseInt(MsPitchInfo[2]));
                }

                long end= System.currentTimeMillis();
                if(end-start<createSpan) {
                    try {
                        Thread.sleep((long)createSpan-(end-start));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(pause) {
                    attachSpan = System.currentTimeMillis()-GameData.gamepauseTime;
                } else attachSpan = 0;
            }
        }
    }
    public void createNewSlide(item tp) {                              //产生一个新的滑块 放入Data的滑块队列
        while ((curRandomInt = random.nextInt(4)) == lastRandomInt) {}
        lastRandomInt=curRandomInt;
        //tp.span代表了滑块所处的类型 因此要求音节以单位化直接反应音节的长短
        MainSlide slide = new MainSlide(curRandomInt*baseSlideWidth,-GameData.SlideHeight[tp.span],
                baseSlideWidth, GameData.SlideHeight[tp.span],tp.pitch,tp.span,"paino");
        synchronized (GameView.lock) {
            GameView.mainSlideArrayList.add(slide);
        }
        lastSlideHeight = GameData.SlideHeight[tp.span];       //获得上一个滑块的长度
    }
    public void init() {
        synchronized (GameData.lock) {                                              //同步GameData中的游戏数据
            tmpBuffer = new StringBuffer(GameData.mainMusicScore.toString());
            baseSlideHeight = GameData.baseSlideHeight;
            baseSlideWidth = GameData.baseSlideWidth;
            speedRK = GameData.gameSpeed[GameData.GameRK];                                       //当前游戏难度的游戏速度
            baseHight = GameData.STANDARD_HIEGHT;
        }
        MusicScore=tmpBuffer.toString();
        MsArray=MusicScore.split("#");
        currentTime=-1;currentPitch=0;loopTimes=0;
        endPitch=MsArray.length;
         //根据字符串 解析每一个音节 音调 出现 时长
        MsPitchInfo=MsArray[currentPitch].split(" ");
        thisPitch=new item(Integer.parseInt(MsPitchInfo[0]), Integer.parseInt(MsPitchInfo[1])
                , Integer.parseInt(MsPitchInfo[2]));

        lastSlideHeight = baseSlideHeight;
    }

    public void Pause() { this.pause = true; }
    public void Resume() { this.pause = false; }
    public void setFlag(boolean flag) { this.flag = flag ;}
    public void turnPause() {
        if(pause) GameData.gamerestartTime = System.currentTimeMillis();
        else GameData.gamepauseTime = System.currentTimeMillis();
        this.pause = !this.pause;
    }
}
