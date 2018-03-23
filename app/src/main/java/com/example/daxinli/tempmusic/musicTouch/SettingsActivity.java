package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.util.LogUtil;
import com.example.daxinli.tempmusic.util.SettingItem;
import com.example.daxinli.tempmusic.util.SettingsAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private List<SettingItem> settingList = new ArrayList<>();
    ActionBar actionBar;
    TextView tv_usernameActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        initToolBar();
        initRecyclerView();
    }

    private static final String TAG = "SettingsActivity";
    public void TurnToActivity(int type) {   //打开游戏帮助Activity
        if(type==1) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if(type==2) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if(type==3) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:                                                     //处理LoginActivity的返回信息
                if(resultCode==RESULT_OK) {                             //如果成功返回 即调用了setResult和finish
                    String result = data.getStringExtra("Result");
                    if (result.length() > 0) {
                        lastUserWToF(result);
                        Log.e(TAG, result);
                        tv_usernameActionBar.setText(result);
                    }
                }
                break;
        }
    }

    private void initToolBar() {
        Toolbar toolbar =(Toolbar) findViewById(R.id.content_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.collapsingToolBarLayout);
        setSupportActionBar(toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.content_imageView);
        CircleImageView headP = (CircleImageView) findViewById(R.id.headP);
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            // TODO: 2018/3/18 用户上一次信息进行处理 加载cache
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //添加标题栏的用户名称
        tv_usernameActionBar = (TextView) findViewById(R.id.login_name_setting);
        String userTitle = lastUserRFromF();
        if(userTitle.length()==0)
            tv_usernameActionBar.setText("尚未登录");
        else
            tv_usernameActionBar.setText(userTitle);

        Glide.with(this).load(R.drawable.myback).into(imageView);
        Glide.with(this).load(R.drawable.touxiang).into(headP);

        headP.setOnClickListener(this);
        tv_usernameActionBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_name_setting:
            case R.id.headP:
                TurnToActivity(3);
                break;
        }
    }

    private void lastUserWToF(String username) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        LogUtil.i(TAG,username);
        try {
            out = openFileOutput(GameData.lastUserFname, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(username);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!=null)
                    writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String lastUserRFromF() {
        BufferedReader reader = null;
        FileInputStream in = null;
        String tmpStr = null;
        try {
            in = openFileInput(GameData.lastUserFname);
            reader = new BufferedReader(new InputStreamReader(in));
            tmpStr = reader.readLine();
        } catch(Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if(reader!=null)
                    reader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        if(tmpStr==null) return "";
        else return tmpStr;
    }

    private void initRecyclerView() {
        settingList.add(new SettingItem(R.drawable.pic_effect,"音效","",3));
        settingList.add(new SettingItem(R.drawable.pic_help,"帮助","",1));
        settingList.add(new SettingItem(R.drawable.pic_about,"关于游戏","",1));
        settingList.add(new SettingItem(R.drawable.pic_edition,"乐动指间","版本1.0.0",2));


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        SettingsAdapter settingsAdapter = new SettingsAdapter(this,settingList);
        recyclerView.setAdapter(settingsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //为ActionBar的返回按钮添加时间监听
        switch(item.getItemId()) {
            case android.R.id.home:
                this.removeActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
