package com.example.daxinli.tempmusic.MutigameModule.Activity.Manager;

import android.content.Context;

import com.example.daxinli.tempmusic.MutigameModule.View.Muti_SurfaceView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Daxin Li on 2018/6/1.
 * 用来产生乐谱，接收写入信息
 */

public class MusicScoreManager {
    Muti_SurfaceView mcontext;
    FileOutputStream out;
    BufferedWriter writer;
    StringBuilder musicScore;
    ArrayList<int[3]>

    public MusicScoreManager(Muti_SurfaceView context) {
        onInit();
    }
    public void onInit() {
        musicScore = new StringBuilder();
    }
    public void onWrite(String pitch) {
        musicScore.append(pitch+"\n");
    }
    public void onWrite() {

    }
    public void writeToFile(String filename) {
        out = null;
        writer = null;
        try {
            out = mcontext.mcontext.openFileOutput("score.txt",Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(musicScore.toString());
            writer.flush();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
