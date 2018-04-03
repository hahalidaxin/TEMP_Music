package com.example.daxinli.tempmusic.util.manager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.musicTouch.WelcomeActivity;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class SoundManager
{
	public static int BACKGROUND_MUSIC=0;
	public static int[] PIANO_PITCH = {0,
            1,2,3,4,5,6,7,
            8,9,10,11,12,13,14,
	};

	SoundPool sp ;
	HashMap<Integer	,Integer> hm ;
	Activity activity;

	public MediaPlayer mp  ;

	public SoundManager(Activity activity)
	{
		this.activity = activity  ;
		initSound();
	}

	
	public void initSound()
	{
		sp = new SoundPool
		(1,
		AudioManager.STREAM_MUSIC, 
		100
		);
		hm = new HashMap<Integer, Integer>();
		hm.put(PIANO_PITCH[1], sp.load(activity, R.raw.p_1 , 1));
		hm.put(PIANO_PITCH[2], sp.load(activity, R.raw.p_2 , 1));
		hm.put(PIANO_PITCH[3], sp.load(activity, R.raw.p_3 , 1));
		hm.put(PIANO_PITCH[4], sp.load(activity, R.raw.p_4 , 1));
		hm.put(PIANO_PITCH[5], sp.load(activity, R.raw.p_5 , 1));
		hm.put(PIANO_PITCH[6], sp.load(activity, R.raw.p_6 , 1));
		hm.put(PIANO_PITCH[7], sp.load(activity, R.raw.p_7 , 1));
        hm.put(PIANO_PITCH[8], sp.load(activity, R.raw.p_1_2 , 1));
        hm.put(PIANO_PITCH[9], sp.load(activity, R.raw.p_2_2, 1));
        hm.put(PIANO_PITCH[10], sp.load(activity, R.raw.p_3_2, 1));
        hm.put(PIANO_PITCH[11], sp.load(activity, R.raw.p_4_2, 1));
        hm.put(PIANO_PITCH[12], sp.load(activity, R.raw.p_5_2, 1));
        hm.put(PIANO_PITCH[13], sp.load(activity, R.raw.p_6_2, 1));
        hm.put(PIANO_PITCH[14], sp.load(activity, R.raw.p_7_2, 1));
		hm.put(BACKGROUND_MUSIC, sp.load(activity, R.raw.background , 1));
	}
	public void playBackGroundMusic(Activity ac,int Id)
	{
		if(!GameData.GameEffect) return ;
		if(WelcomeActivity.sound.mp!=null) {
			WelcomeActivity.sound.mp.pause();
			WelcomeActivity.sound.mp=null;
		}
		if(WelcomeActivity.sound.mp==null)
		{
			WelcomeActivity.sound.mp =  MediaPlayer.create(ac,Id);
			WelcomeActivity.sound.mp.setVolume(0.2f, 0.2f);
			WelcomeActivity.sound.mp.setLooping(true);
			WelcomeActivity.sound.mp.start();
		}
	}
	public void playMusic(int sound,int loop)
	{
		if(!GameData.GameEffect) return ;
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