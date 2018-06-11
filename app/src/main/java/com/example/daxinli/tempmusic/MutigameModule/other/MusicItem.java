package com.example.daxinli.tempmusic.MutigameModule.other;

import java.util.ArrayList;

/**
 * Created by Daxin Li on 2018/6/10.
 */

public class MusicItem {
    public String musicName;
    public String musicInfo;
    public String imageRes;
    public ArrayList<Integer> instrus = new ArrayList<>();
    public MusicItem(int imageRes,String musicName,String musicInfo,ArrayList<Integer> instru) {
        this.imageRes = Integer.toString(imageRes);
        this.musicInfo = musicInfo;
        this.musicName = musicName;
        for(int in:instru) { instrus.add(in); }
    }
    public String getMusicName() {
        return musicName;
    }
    public String getMusicInfo() {
        return musicInfo;
    }
    public String getImageRes() {
        return imageRes;
    }
    public ArrayList<Integer> getInstrus() {
        return this.instrus;
    }
}
