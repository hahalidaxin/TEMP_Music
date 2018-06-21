package com.example.daxinli.tempmusic.MutigameModule.other;

/**
 * Created by DaxinLi on 2018/6/21.
 */

public class MusicScoreItem {
    public String musicName;
    public int musicScore;
    public int rank;
    public MusicScoreItem(int rank,String musicName, int musicScore) {
        this.rank = rank;
        this.musicName = musicName;
        this.musicScore = musicScore;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public int getMusicScore() {
        return musicScore;
    }

    public void setMusicScore(int musicScore) {
        this.musicScore = musicScore;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
