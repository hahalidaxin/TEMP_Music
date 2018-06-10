package com.example.daxinli.tempmusic.MutigameModule.Activity.gameplay;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.daxinli.tempmusic.MutigameModule.other.MusicItem;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;
import com.example.daxinli.tempmusic.util.SettingsAdapter;

import java.util.ArrayList;

//从服务器端获取所有的可用乐谱列表，选择乐谱进入wait界面

public class ChooseMusicActivity extends BaseActivity implements View.OnClickListener {
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_music);
        initView();
    }
    public void initView() {

    }
    public void initRecyclerView() {
        ArrayList<MusicItem> settingList = new ArrayList<>();
        //根据返回值构造recyclervieww

        recyclerView = (RecyclerView) findViewById(R.id.GrecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SettingsAdapter settingsAdapter = new SettingsAdapter(this,settingList);
        recyclerView.setAdapter(settingsAdapter);
    }
    @Override
    public void onClick(View v) {

    }
}
