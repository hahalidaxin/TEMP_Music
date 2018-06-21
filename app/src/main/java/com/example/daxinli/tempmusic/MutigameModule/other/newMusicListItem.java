package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by DaxinLi on 2018/6/21.
 */

public class newMusicListItem {
    public String rank;
    public String musicName;
    public newMusicListItem(String rank,String musicName) {
        this.rank = rank;
        this.musicName = musicName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }
}
