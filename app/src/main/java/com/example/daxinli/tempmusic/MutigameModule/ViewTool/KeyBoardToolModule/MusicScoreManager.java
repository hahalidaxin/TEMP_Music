package com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule;

import android.content.Context;
import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Daxin Li on 2018/6/1.
 * 用来产生乐谱，接收写入信息
 */

public class MusicScoreManager {
    private static final String TAG = "MusicScoreManager";
    Muti_SurfaceView mcontext;
    FileOutputStream out;
    BufferedWriter writer;
    StringBuilder musicScore;
    public class node {
        long st,ed;
        int key;
        public node(long st,long ed,int key) {
            this.st = st; this.ed = ed; this.key = key;
        }
    }
    ArrayList<node> ndList = new ArrayList<>();
    int instruType;

    public MusicScoreManager(Muti_SurfaceView context,int instruType) {
        this.mcontext = context;
        this.instruType = instruType;
    }
    public void onStart() {
        long stT = System.currentTimeMillis();
        ndList.add(new node(stT,stT,-1));
    }
    public void onKey(long st,long ed,int key) {
        ndList.add(new node(st-ndList.get(0).st,ed-ndList.get(0).ed,key));
        Log.e(TAG, String.format("onKey: %d %d %d",st,ed,key));
    }
    public void onMusicOver() {
        //将ndList中转化为文件信息
        out = null;
        writer = null;
        try {
            out = mcontext.mcontext.openFileOutput("score.txt", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            for(int i=0;i<ndList.size();i++) {
                node nd = ndList.get(i);
                writer.write(String.format("%d %d %d",nd.st,nd.ed,nd.key));
            }
            writer.flush();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
