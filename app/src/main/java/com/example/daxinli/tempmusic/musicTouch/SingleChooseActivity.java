package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import com.example.daxinli.tempmusic.MutigameModule.other.MusicItem;
import com.example.daxinli.tempmusic.MutigameModule.other.MusicListAdapter;
import com.example.daxinli.tempmusic.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;

public class SingleChooseActivity extends BaseActivity {

    RecyclerView recyclerView;
    TextView textMusicNum;
    private static final String TAG = "SingleChooseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_choose);
        startFindLocalMusic();
        initView();
        initRecyclerView();
    }

    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.GrecyclerView_singleChoose);
        textMusicNum = (TextView)findViewById(R.id.text_musicDetectedNum_singleChoose);
    }

    public String[] fileList;
    public void startFindLocalMusic()  {
        InputStream in=null;
        BufferedReader reader=null;
        StringBuffer tmpScore=new StringBuffer();
        AssetManager manager = this.getAssets();
        try {
            fileList = manager.list("text/music");
            int musicCnt = 0;
        } catch(Exception e) {
            Log.e(TAG, "startFindLocalMusic: error"  );
            e.printStackTrace();
        }
    }
    public void initRecyclerView() {
        ArrayList<MusicItem> settingList = new ArrayList<>();
        //String msg = mintent.getStringExtra("musicList");
        textMusicNum.setText(String.format("(嘿ヾ(✿ﾟ▽ﾟ)ノ，我们找到了%d首歌曲：",fileList.length));
        for(int i=0;i< fileList.length;i++) {
            settingList.add(new MusicItem(i+1,fileList[i],"",new ArrayList<Integer>()));
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
    public void onActivityTrans(String filename) {
        //点击Adapter之后进行activity之间的切换
        Intent intent = new Intent(SingleChooseActivity.this,GameActivity.class);
        intent.putExtra("musicName",filename);
        startActivity(intent);
    }
}
