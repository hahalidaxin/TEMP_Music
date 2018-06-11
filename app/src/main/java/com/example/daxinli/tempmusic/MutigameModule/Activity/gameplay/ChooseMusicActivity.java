package com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.Activity.Composition.WaitActivity;
import com.example.daxinli.tempmusic.MutigameModule.Network.ChooseReceiver;
import com.example.daxinli.tempmusic.MutigameModule.Network.NetMsgReceiver;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicItem;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicListAdapter;
import com.example.daxinli.tempmusic.MutigameModule.service.NetworkService;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

import java.util.ArrayList;

//从服务器端获取所有的可用乐谱列表，选择乐谱进入wait界面

public class ChooseMusicActivity extends BaseActivity implements View.OnClickListener {
    private NetworkService.MyBinder myBinder;
    private ChooseReceiver breceiver;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChooseMusicActivity.this.myBinder = (NetworkService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    RecyclerView recyclerView ;
    TextView textMusicNum;
    Intent mintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_music);
        mintent = getIntent();
        initView();
        initRecyclerView();
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(breceiver);
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注册广播//初始化广播接收器
        breceiver = new ChooseReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NetMsgReceiver.NORMAL_AC_ACTION);
        intentFilter.addAction(NetMsgReceiver.WAIT_AC_ACTION);
        registerReceiver(breceiver,intentFilter);
        //初始化service
        Intent intent = new Intent (ChooseMusicActivity.this,NetworkService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);
    }
    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.GrecyclerView);
        textMusicNum = (TextView)findViewById(R.id.text_musicDetectedNum);
    }
    public void initRecyclerView() {
        ArrayList<MusicItem> settingList = new ArrayList<>();
        String msg = mintent.getStringExtra("musicList");
        String[] msgSplits =  msg.split("\\$\\$");  //需要用双斜线对特殊字符进行转义
        textMusicNum.setText(String.format("(嘿ヾ(✿ﾟ▽ﾟ)ノ，我们找到了%d首歌曲：",msgSplits.length));
        for(int i=0;i< msgSplits.length;i++) {
            int x = msgSplits[i].indexOf('-');
            String musicName = msgSplits[i].substring(0,x);
            musicName = musicName.substring(0,musicName.length()-1);
            String musicInfo = msgSplits[i].substring(x+1);
            settingList.add(new MusicItem(i+1,musicName,musicInfo));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MusicListAdapter settingsAdapter = new MusicListAdapter(this,settingList);
        recyclerView.setAdapter(settingsAdapter);
        runLayoutAnimation(recyclerView);
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    public void sendMessage(String filename) {
        myBinder.sendMessage("<#CHOOSEVIEW#>MUSICSELECT#"+filename);
    }
    public void onActivityTrans(String msg) {
        Intent intent = new Intent(ChooseMusicActivity.this, WaitActivity.class);
        intent.putExtra("msg",msg);
        intent.putExtra("connectType","GAMEPLAY");
        intent.putExtra("activityType",mintent.getStringExtra("activityType"));
        //打开wait界面 此时修改teamstate
        myBinder.sendMessage("<#CREATEVIEW#>teamstate#0");
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {

    }
}