package com.example.daxinli.tempmusic.util.manager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;

import java.util.HashMap;

@SuppressLint("UseSparseArrays")
public class SoundManager
{
	SoundPool sp ;
	HashMap<Integer	,Integer> hm ;
	Activity activity;

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
		hm.put(1, sp.load(activity, R.raw.piano_1 , 1));
		hm.put(2, sp.load(activity, R.raw.piano_2 , 1));
		hm.put(3, sp.load(activity, R.raw.piano_3  , 1));
		hm.put(4, sp.load(activity, R.raw.piano_4  , 1));
		hm.put(5, sp.load(activity, R.raw.piano_5 , 1));
		hm.put(6, sp.load(activity, R.raw.piano_6 , 1));
		hm.put(7, sp.load(activity, R.raw.piano_7 , 1));
        hm.put(8, sp.load(activity, R.raw.piano_8 , 1));
        hm.put(9, sp.load(activity, R.raw.piano_9 , 1));
        hm.put(10, sp.load(activity, R.raw.piano_10 , 1));
        hm.put(11, sp.load(activity, R.raw.piano_11 , 1));
        hm.put(12, sp.load(activity, R.raw.piano_12 , 1));
        hm.put(13, sp.load(activity, R.raw.piano_13 , 1));
}
	public int playMusic(int sound,int loop)
	{
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

	public void playMediaMusic(Activity ac, int id,boolean loop) {
		if(!GameData.GameEffect) return ;
		if(mp!=null) {
			mp.pause();
			if(id != lastMP)
				mp=null;
		}
		if(mp==null)
		{
			mp = MediaPlayer.create(ac, id);
			mp.setVolume(1.0f, 1.0f);
			lastMP = id;

		}
		mp.setLooping(loop);
		mp.start();
	}
	public void stopMediaMusic() {
		if(mp!=null && mp.isPlaying()) {
			try {
				mp.stop();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}