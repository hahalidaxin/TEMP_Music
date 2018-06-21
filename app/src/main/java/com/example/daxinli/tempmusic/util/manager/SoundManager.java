package com.example.daxinli.tempmusic.util.manager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.daxinli.tempmusic.R;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class SoundManager
{
	public static int[][] RESKeyMusic = {
	{0,R.raw.piano_1, R.raw.piano_2, R.raw.piano_3, R.raw.piano_4, R.raw.piano_5, R.raw.piano_6,
		R.raw.piano_7, R.raw.piano_8, R.raw.piano_9, R.raw.piano_10, R.raw.piano_11, R.raw.piano_12, R.raw.piano_13},
	{0,R.raw.guitar_1,R.raw.guitar_2,R.raw.guitar_3,R.raw.guitar_4,R.raw.guitar_5,R.raw.guitar_6,
		R.raw.guitar_7,R.raw.guitar_8,R.raw.guitar_9,R.raw.guitar_10,R.raw.guitar_11,R.raw.guitar_12,R.raw.guitar_13},
	{0,R.raw.kick_1,R.raw.kick_2,R.raw.kick_3,R.raw.kick_4,R.raw.kick_5,R.raw.kick_6,R.raw.kick_7,
		R.raw.kick_8,R.raw.kick_9,R.raw.kick_10,R.raw.kick_11,R.raw.kick_12,R.raw.kick_13},
	{0,R.raw.bell_1,R.raw.bell_2,R.raw.bell_3,R.raw.bell_4,R.raw.bell_5,R.raw.bell_6,R.raw.bell_7,
		R.raw.bell_8,R.raw.bell_9,R.raw.bell_10,R.raw.bell_11,R.raw.bell_12,R.raw.bell_13}};
	SoundPool sp ;
	HashMap<Integer	,Integer> hm ;
	Activity activity;
	public static boolean IsOn=true;

	public MediaPlayer mp  ;
	public int lastMP;

	public SoundManager(Activity activity)
	{
		this.activity = activity ;
		initSound();
	}

	public void initSound()
	{
		sp = new SoundPool
		(2,
		AudioManager.STREAM_MUSIC,
		100
		);
		hm = new HashMap<Integer, Integer>();
		for(int i=0;i<4;i++) {
			for(int j=1;j<14;j++) {
				hm.put(RESKeyMusic[i][j],sp.load(activity,RESKeyMusic[i][j],1));
			}
		}
}
	public int playMusic(int sound,int loop)
	{
		if(!IsOn) return 0;
        if(sp!=null)
		    return sp.play(hm.get(sound), 1, 1, 1, loop, 1);
        return 0;
	}
    public void stopGameMusic(int streamID)
    {
        sp.pause(streamID);
        sp.stop(streamID);
        //sp.setVolume(sound, 0, 0);
    }

	public static void setOn(boolean on) {
		IsOn = on;
	}
}