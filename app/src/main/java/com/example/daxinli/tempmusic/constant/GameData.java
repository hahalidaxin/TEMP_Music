package com.example.daxinli.tempmusic.constant;

import com.example.daxinli.tempmusic.action.Action;
import com.example.daxinli.tempmusic.util.elseUtil.Area;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hahal on 2018/2/25.
 * 游戏数据的存储-临时数据
 */
public class GameData {
    //-----------同步相关
    public static Object lock = new Object();  //用于同步方法的对象
    public static boolean lockNetworkThread = false;  //用于等待网络请求的锁
    //----------文件操作相关
    public static String lastUserFname = "userData";
    //----------服务器相关信息
    public static final String serverIP = "172.20.74.1";
    public static final int sertVerPort = 9999;
    //-----------用户登录相关
    public static String login_InfoQ = null;
    public static String register_infoQ = null;
    //----------设备硬件信息
    public static final int STANDARD_WIDTH = 1080;
    public static final int STANDARD_HIEGHT = 1920;
    public static int REAL_WIDTH;             //实际屏幕的宽高
    public static int REAL_HEIGHT;

    //-----------游戏界面信息
    public static final int Game_load = 1;      //加载界面编号
    public static final int Game_menu = 2;      //游戏菜单界面编号
    public static final int Game_playing = 3;   //游戏开始界面编号
    public static final int Game_pause = 4;     //游戏暂停编号
    public static final int Game_select = 5;    //选择关卡编号
    public static final int Game_over = 6;      //游戏失败界面
    public static int viewState;                //游戏所处界面/状态编号

    //----------游戏设置参数
    public static boolean GameEffect = true;     //是否开启游戏音效 ---------需要加载修改之类

    //-----------游戏欢迎界面数据
    public static final long ani_Span = 1000;

    //-----------游戏Playing界面数据
    public static final int MainSlideTHSpan=2;                      //与刷帧时间一致

    public static Queue<Action> aq=new LinkedList<Action>();         //动作队列ActionQueue
    public static int GameRK = 0;                                   //游戏难度
    public static final float [] gameSpeed = {1.2f,1.5f,2.0f,2.4f};                  //不同游戏难度对应的速度
    public static final float [] slideAnim1Radius = {30.0f,35.0f,40.0f,50.0f};
    public static StringBuffer mainMusicScore;                   //游戏的乐谱信息
    public static String fileMusicScoreName;                    //乐谱的文件名
    public static String MusicName;                             //乐曲名称
    public static float baseSlideHeight = STANDARD_HIEGHT/4;       //默认大小方块的高
    public static float baseSlideWidth = STANDARD_WIDTH/4;       //默认大小方块的宽
    public static final float[] SlideHeight = {0,480,960,1440,1536};    //不同类型的滑块的默认长度
    public static long gamepauseTime;               //游戏的暂停时间用来保持createSlideTh的节奏
    public static long gamerestartTime;
    public static int slideT1score = 1;             //点击小滑块获得的分数
    public static int slideT2score = 2;             //点击长滑块获得的分数
    public static int sleepSpanPerDiff = 4000;      //各难度之间需要休眠的时间
    public static long refreshFrameTime ;                   //GameView刷新一帧需要的时间
    public static float gameProgressRatio;

    //-------------游戏工具类数据存储
    public static final int initgamerHealth=1;
    public static int GameScore=0;                        //游戏分数
    public static int gamerHealth=initgamerHealth;                      //玩家生命值

    //-------------游戏界面信息UI位置存储
    public static Area area_btn_pause;
    public static Area area_btn_restart;
    public static Area area_btn_exit;
    public static Area area_pic_rheart;
    //------------游戏图片数据的保存
    public static int num_W = 128;
    public static int num_H = 128;
    public static int btnExit_W = 640;
    public static int btnExit_H = 176;
    public static float redHeart_W = 250;
    public static float redHeart_H = 250;
}