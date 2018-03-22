package com.example.daxinli.tempmusic.musicTouch;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Daxin Li on 2018/3/7.
 */

public class myApplication extends Application {
    private ArrayList<Activity> acList;

    public void onCreate() {
        super.onCreate();
        acList = new ArrayList<Activity>();
    }
    public void addActivity(Activity activity) {
        if(!acList.contains(activity))
            acList.add(activity);
    }
    public void removeActivity(Activity activity) {
        if(acList.contains(activity)) {
            acList.remove(activity);
            activity.finish();
        }
    }
    public void RemoveAll() {
        for(Activity activity:acList) {
            if(activity!=null) activity.finish();
        }
    }
}
