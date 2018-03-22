package com.example.daxinli.tempmusic.util.manager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.musicTouch.GameActivity;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class SoundManager
{
	public static int BACKGROUND_MUSIC=0;
	public static int PIANO_PITCH_C1=1;
	public static int PIANO_PITCH_C2=2;
	public static int PIANO_PITCH_C3=3;
	public static int PIANO_PITCH_C4=4;
	public static int PIANO_PITCH_C5=5;
	public static int PIANO_PITCH_C6=6;
	public static int PIANO_PITCH_C7=7;
	SoundPool sp ;
	HashMap<Integer	,Integer> hm ;
	GameActivity activity;

	public MediaPlayer mp  ;

	public SoundManager(GameActivity activity)
	{
		this.activity = activity  ;
		initSound();
	}

	
	public void initSound()
	{
		sp = new SoundPool
		(4, 
		AudioManager.STREAM_MUSIC, 
		100
		);
		hm = new HashMap<Integer, Integer>();
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_1 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_2 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_3 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_4 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_5 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_6 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.p_7 , 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.background , 1));
	}
	public void playBackGroundMusic(Activity ac,int Id)
	{
		if(GameActivity.sound.mp!=null){
			GameActivity.sound.mp.pause();
			GameActivity.sound.mp=null;
		}
		if(GameActivity.sound.mp==null)
		{
			GameActivity.sound.mp =  MediaPlayer.create(ac,Id);
			GameActivity.sound.mp.setVolume(0.2f, 0.2f);
			GameActivity.sound.mp.setLooping(true);
			GameActivity.sound.mp.start();
		}
	}
	public void playMusic(int sound,int loop)
	{
		@SuppressWarnings("static-access")
		AudioManager am = (AudioManager)activity.getSystemService(activity.AUDIO_SERVICE);
		float steamVolumCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC)  ;
		float steamVolumMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)  ;
		float volum = steamVolumCurrent/steamVolumMax  ;
		sp.play(hm.get(sound), volum, volum, 1	, loop, 1)  ;
	}
	

	long preTimeStamp=0;
	public void playGameMusic(int sound,int loop)
	{
		long currTimeStamp=System.nanoTime();
		if(currTimeStamp-preTimeStamp<500000000L)
		{
			return;
		}
		preTimeStamp=currTimeStamp;
		@SuppressWarnings("static-access")
		AudioManager am = (AudioManager)activity.getSystemService(activity.AUDIO_SERVICE);
		float steamVolumCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC)  ;
		float steamVolumMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)  ;
		float volum = steamVolumCurrent/steamVolumMax  ;
		sp.play(
				hm.get(sound),
				volum,
				volum,
				1	,
				loop,
				1
				);
	}	
	
	public void stopGameMusic(int sound)
	{
		sp.pause(sound);
		sp.stop(sound);
		sp.setVolume(sound, 0, 0);
	}
}

