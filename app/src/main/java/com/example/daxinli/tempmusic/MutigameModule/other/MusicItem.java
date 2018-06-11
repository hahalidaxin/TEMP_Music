package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by Daxin Li on 2018/6/10.
 */

public class MusicItem {
    public String musicName;
    public String musicInfo;
    public String imageRes;
    public MusicItem(int imageRes,String musicName,String musicInfo) {
        this.imageRes = Integer.toString(imageRes);
        this.musicInfo = musicInfo;
        this.musicName = musicName;
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
}
