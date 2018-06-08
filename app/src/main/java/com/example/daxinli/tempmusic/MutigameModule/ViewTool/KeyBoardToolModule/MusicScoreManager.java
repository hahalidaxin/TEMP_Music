package com.example.daxinli.tempmusic.MutigameModule.ViewTool.KeyBoardToolModule;

import android.util.Log;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
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
    String musicname;
    public MusicScoreManager(Muti_SurfaceView context,String musicname) {
        this.mcontext = context;
        this.musicname = musicname;
    }
    public void onStart() {
        long stT = System.currentTimeMillis();
        ndList.add(new node(stT,stT,-1));
    }
    public void onKey(long st,long ed,int key) {
        ndList.add(new node(st-ndList.get(0).st,ed-ndList.get(0).ed,key));
        Log.e(TAG, String.format("onKey: %d %d %d",st,ed,key));
    }
    public String getMusic() {
        StringBuffer builder = new StringBuffer();
        builder.append(String.format("MusicSended#%s#%d#",musicname,ndList.get(ndList.size()-1).ed));
        for(int i=1;i<ndList.size();i++) {
            node nd = ndList.get(i);
            builder.append(String.format("%d %d %d\n",nd.st,nd.ed,nd.key));
        }
        return builder.toString();
    }
}
