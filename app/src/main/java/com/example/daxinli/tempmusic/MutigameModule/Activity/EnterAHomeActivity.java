package com.example.daxinli.tempmusic.MutigameModule.Activity;

import android.os.Bundle;
import android.view.View;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.BaseActivity;

public class EnterAHomeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_ahome);

        initView();
    }
    public void initView() {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        }
    }
}
