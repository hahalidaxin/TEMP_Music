package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

public class CreateHomeActivtiy extends BaseActivity implements View.OnClickListener{
    Button btn_startGame;
    TextView text_teamateLinked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_home_activtiy);

        initView();
    }
    void initView() {
        btn_startGame = (Button) findViewById(R.id.btn_leader_startgame);
        text_teamateLinked = (TextView) findViewById(R.id.text_amout_peoplein);

        btn_startGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_leader_startgame:
                //leader决定开始游戏 开启一个activity activity用opengles实现

                break;
        }
    }
}
