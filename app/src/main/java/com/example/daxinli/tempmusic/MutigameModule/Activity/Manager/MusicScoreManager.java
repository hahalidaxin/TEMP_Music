package com.example.daxinli.tempmusic.MutigameModule.Activity.Manager;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * Created by Daxin Li on 2018/6/1.
 * 用来产生乐谱，接收写入信息
 */

public class MusicScoreManager {
    FileOutputStream outStream;
    BufferedOutputStream writer;
    StringBuilder musicScore

    public MusicScoreManager() {
        onInit()
    }
    public void onInit() {
        musicScore = new StringBuilder();
    }
    public void onWrite(String pitch) {
        musicScore.append(pitch+"\n")
    }
    public void writeToFile(String filename) {
        outStream = new FileOutputStream(filename);
        writer = new BufferedOutputStream(outStream);
        try {
            writer.write(musicScore.toString());
            writer.flush();
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
