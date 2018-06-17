package com.example.daxinli.tempmusic.MutigameModule.Activity.Composition;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.MutigameModule.Activity.CreateAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.EnterAHomeActivity;
import com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay.MutiPlayActivity;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.WaitACReceiver;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.util.effect.pathviewer.PathSurfaceView;

import de.hdodenhof.circleimageview.CircleImageView;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class WaitActivity extends BaseActivity implements View.OnClickListener {
    public static final int CONNECT_GAMEPlAY = 2221;
    public static final int CONNECT_COMPOSE  = 1113;
    public static final int TYPE_LEADER = 0;
    private static final String TAG = "WaitOtherPeopleActivity";
    Button btn_startGame;
    Button btn_sendDanmu;
    TextView text_teamateLinked;
    EditText editText_Danmu;
    EditText editText_MusicName;
    EditText editText_BPM;
    LinearLayout lilayout;

    public final String[] instruNametoShow = { "钢琴","吉他","打击","响铃" };
    int[] RID_imgInstru = {R.id.img_Instru1_wait1,R.id.img_Instru2_wait1,
            R.id.img_Instru3_wait1,R.id.img_Instru4_wait1};
    int[] RID_liInstru = {R.id.li_instru0,R.id.li_instru1,R.id.li_instru2,R.id.li_instru3};
    int[] RID_textInstri =  {R.id.text_instruSelect1_wait1,R.id.text_instruSelect2_wait1,
            R.id.text_instruSelect3_wait1,R.id.text_instruSelect4_wait1};
    int[] RDRAW_img={R.drawable.pic_instru1_p0,R.drawable.pic_instru1_p1,
            R.drawable.pic_instru2_p0,R.drawable.pic_instru2_p1,
            R.drawable.pic_instru3_p0,R.drawable.pic_instru3_p1,
            R.drawable.pic_instru4_p0,R.drawable.pic_instru4_p1};
    CircleImageView[] img_stru= new CircleImageView[4];
    TextView[] text_instru = new TextView[4];

    private NetworkService.MyBinder myBinder;
    private WaitACReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WaitActivity.this.myBinder = (NetworkService.MyBinder)service;
            myBinder.sendMessage(String.format("<#WAITVIEW#>INSTRUNUMLIMIT#%d",instruNumLimit));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private boolean showDanmaku;
    private DanmakuContext danmakuContext;
    private DanmakuView danmakuView;
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    Intent mintent;
    public int mateType;
    public int clockID;
    public int sessionID;
    public int connectType;
    public int instruNumLimit=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mintent = getIntent();
        mateType = mintent.getIntExtra("activityType",-1);
        clockID = mintent.getIntExtra("clockID",-1);
        sessionID = mintent.getIntExtra("sessionID",-1);
        connectType = mintent.getIntExtra("connectType",-1);
        setContentView(R.layout.activity_wait1);

        initPathView();
        initView();
    }
    void initView() {
        btn_startGame = (Button) findViewById(R.id.btn_leader_startgame);
        text_teamateLinked = (TextView) findViewById(R.id.text_amout_peoplein);
        btn_sendDanmu = (Button) findViewById(R.id.btn_danmusend_createhome);
        editText_Danmu = (EditText) findViewById(R.id.edittext_danmu_createhome);
        danmakuView = (DanmakuView) findViewById(R.id.danmakuview_leader);
        editText_BPM = (EditText) findViewById(R.id.edittext_bpm);
        editText_MusicName = (EditText) findViewById(R.id.edittext_musicname);
        lilayout = (LinearLayout) findViewById(R.id.Lilayout_tohide);
        btn_startGame.setOnClickListener(this);
        btn_sendDanmu.setOnClickListener(this);
        if(connectType==CONNECT_COMPOSE) {
            if (mateType == 1) {
                lilayout.setVisibility(View.INVISIBLE);
                btn_startGame.setVisibility(View.INVISIBLE);
            }
        } else if(connectType==CONNECT_GAMEPlAY) {
            lilayout.setVisibility(View.INVISIBLE);
            String[] strs = mintent.getStringExtra("InstruNum").split("\\$\\$");
            boolean[] flag = new boolean[4];
            instruNumLimit = strs.length;
            for(int i=0;i<strs.length;i++) {
                flag[Integer.parseInt(strs[i].trim())] = true;
            }
            for(int i=0;i<4;i++) if(!flag[i]) {
                ((LinearLayout)findViewById(RID_liInstru[i])).setVisibility(View.INVISIBLE);
            }
        }
        btn_sendDanmu.setOnClickListener(this);
        //通过提前设计变量可以进行统一的设计
        for(int i=0;i<4;i++) {
            img_stru[i] = (CircleImageView) findViewById(RID_imgInstru[i]);
            img_stru[i].setOnClickListener(this);
            Glide.with(this).load(RDRAW_img[i*2]).into(img_stru[i]);
            text_instru[i] = (TextView) findViewById(RID_textInstri[i]);
            text_instru[i].setText("");
        }
        Glide.with(this).load(R.drawable.text_danmuliaotian).into((ImageView)findViewById(R.id.wait_titleDanmu));
        Glide.with(this).load(R.drawable.pic_liaotianshi).into((ImageView)findViewById(R.id.wait_picliaotian));
        //danmaku的初始化基本操作
       // netMsgSender = new NetMsgSender(this);
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                danmakuView.start();
                showDanmaku = true;
                //generateSomeDanmaku();
            }
            @Override
            public void updateTimer(DanmakuTimer timer) {

            }
            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }
            @Override
            public void drawingFinished() {
            }
        });
        danmakuContext = DanmakuContext.create();
        danmakuView.prepare(parser,danmakuContext);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_leader_startgame:
                if(connectType==CONNECT_COMPOSE) {
                    myBinder.sendMessage("<#WAITVIEW#>STARTGAME#" + editText_MusicName.getText().toString() + "#"
                            + editText_BPM.getText().toString());
                } else if(connectType==CONNECT_GAMEPlAY) {
                    int x = -1;
                    for(int i=0;i<4;i++) if(text_instru[i].getText().toString().contains("You")) {
                        x=i; break;
                    }
                    myBinder.sendMessage(String.format("<#WAITVIEW#>STARTGAME&MUSIC#%d",x));
                }
                break;
            case R.id.btn_danmusend_createhome:
                HideKeyboard(editText_Danmu);
                String requestCode = editText_Danmu.getText().toString();
                myBinder.sendMessage("<#DANMAKU#>"+Integer.toString(clockID)+"#"+requestCode);
                editText_Danmu.setText("");
                break;
        }
        for(int i=0;i<4;i++) {
            if(v.getId()==RID_imgInstru[i]) {
                //当点击按钮之后 向服务进行申请点击事件 由服务器负责判断是否能够选择当前instru
                myBinder.sendMessage(String.format("<#WAITVIEW#>%d#INSTRU%d#SELECT",clockID,i));
            }
        }
    }
    //显示虚拟键盘
    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }
    //隐藏虚拟键盘
    public static void HideKeyboard(View v){
        InputMethodManager imm = ( InputMethodManager) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0 );
        }
    }
    public void onActivityTrans(int type,String extralInfo) {
        //进行activity之间的切换
        Intent intent = null;
        switch(type) {
            case 0:
                if(connectType==CONNECT_COMPOSE) {
                    //开始游戏
                    int instruType = -1;
                    for (int i = 0; i < 4; i++)
                        if (text_instru[i].getText().toString().contains("You")) {
                            instruType = i;
                            break;
                        }

                    intent = new Intent(WaitActivity.this, CompositionActivity.class);
                    intent.putExtra("activityType", mateType);
                    intent.putExtra("instruType", instruType);
                    intent.putExtra("clockID", clockID);
                    intent.putExtra("sessionID", sessionID);
                    if (this.mateType == 0) {
                        intent.putExtra("musicName", editText_MusicName.getText().toString());
                        intent.putExtra("BPM", Integer.parseInt(editText_BPM.getText().toString().trim()));
                    } else {
                        String[] strs = extralInfo.split("#");
                        intent.putExtra("musicName", strs[0]);
                        intent.putExtra("BPM", Integer.parseInt(strs[1].trim()));
                    }
                    startActivity(intent);
                } else if(connectType==CONNECT_GAMEPlAY) {
                    //启动多人游戏
                    intent = new Intent(WaitActivity.this,MutiPlayActivity.class);
                    intent.putExtra("musicScore",extralInfo);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(mateType==0) {
                ShowAlertDialog("黄鹤大人","您真的要解散该房间吗？",0);
            } else {
                ShowAlertDialog(">_<","您是否确定退出当前房间",0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(danmakuView!=null && danmakuView.isPrepared()) {
            danmakuView.pause();
        }

        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(danmakuView!=null && danmakuView.isPrepared() && danmakuView.isPaused()) {
            danmakuView.resume();
        }
        //注册广播//初始化广播接收器
        breceiver = new WaitACReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.WAIT_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (WaitActivity.this,NetworkService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showDanmaku = false;
        if(danmakuView != null) {
            danmakuView.release();
            danmakuView = null;
        }
    }

    AlertDialog.Builder builder;            //用来处理多个builder重叠出现的情况
    public void ShowAlertDialog(final String tititle,final String msg,final int type) {     //用户想要退出显示警告信息框
        if(builder!=null) return ;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder = new AlertDialog.Builder(WaitActivity.this);
                builder.setTitle(tititle);
                builder.setMessage(msg);
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        if(mateType ==0) {
                            intent = new Intent(WaitActivity.this, CreateAHomeActivity.class);
                        } else if(mateType==1) {
                            intent = new Intent(WaitActivity.this, EnterAHomeActivity.class);
                        }
                        if(type==0) {
                            myBinder.sendMessage("<#EXIT#>");
                            startActivity(intent);
                            //WaitActivity.this.removeActivity();
                        } else if(type==1) {
                            startActivity(intent);
                        }else if(type==3) {
                            //尝试进行重新连接service
                            myBinder.restartNetThread();
                            startActivity(intent);
                        }else if(type==4) {

                        }
                        builder = null;
                    }
                });
                builder.show();
            }
        });

    }
    public void addDanmaku(final String content,final boolean withBorder) {    //添加一条弹幕
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
                danmaku.text = content;
                danmaku.padding = 5;
                danmaku.textSize = sp2px(50);      //有待修改
                danmaku.textColor = Color.WHITE;
                danmaku.setTime(danmakuView.getCurrentTime());
                if (withBorder) {
                    danmaku.borderColor = Color.GREEN;
                }
                danmakuView.addDanmaku(danmaku);
            }
        });
    }
    public void setNumbertoShow(final int number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WaitActivity.this.text_teamateLinked.setText(
                        String.format("找呀找呀找胖友...(%d)",number));
            }
        });
    }
    private float sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale*0.5f);
    }
    public int getclockID() {
        return this.clockID;
    }
    public void setInstruSelect(final int type,final int state,final int flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //设置当前的选择状态
                Glide.with(WaitActivity.this).load(RDRAW_img[type*2+state]).into(img_stru[type]);
                //img_stru[type].setImageResource(RDRAW_img[type*2+state]);
                String text = state==0? "":instruNametoShow[type];
                if(flag==1 && state==1) text=text+"(You)";
                text_instru[type].setText(text);
            }
        });
    }


    PathSurfaceView pathSurfaceView;
    private void initPathView() {
        pathSurfaceView = (PathSurfaceView)findViewById(R.id.wait_pathView);
        pathSurfaceView.initData("text/bliLine.txt");
        pathSurfaceView.startPathviewThread(this);
        pathSurfaceView.setRefreshSpan(1000L);
    }
}
