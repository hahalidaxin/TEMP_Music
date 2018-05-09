package com.example.daxinli.tempmusic.MutigameModule.ViewTool.RhythmToolModule;

/**
 * Created by Daxin Li on 2018/5/9.
 */

public class RhythmToolThread extends Thread {
    private long lastTime=0;
    private boolean flag = true;
    private RhythmTool mrhythmTool;
    public RhythmToolThread(RhythmTool rhythmTool) {
        mrhythmTool = rhythmTool;
    }
    @Override
    public void run() {
        while(flag) {
            synchronized(mrhythmTool) {
                mrhythmTool.ptMg.go();
            }
            if(System.currentTimeMillis()-lastTime<RhythmTool.refreshTimeSpan) {
                try {
                    Thread.sleep(RhythmTool.refreshTimeSpan-(System.currentTimeMillis()-lastTime));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
