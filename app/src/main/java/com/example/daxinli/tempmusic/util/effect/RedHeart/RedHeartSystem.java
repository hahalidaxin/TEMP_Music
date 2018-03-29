package com.example.daxinli.tempmusic.util.effect.RedHeart;

import java.util.Random;

/**
 * Created by Daxin Li on 2018/3/28.
 */

public class RedHeartSystem {
    RedHeart redheart;
    int randomPauseTime;
    int pauseTime ;
    Random random = new Random();
    public RedHeartSystem(float x,float y,float width,float height,float speed) {
        redheart = new RedHeart(x,y,width,height,speed);
        randomPauseTime = random.nextInt(10)+20;
        pauseTime = 0;
    }
    public void drawSelf() {
        if(!redheart.getIsDead()) {
            if(++pauseTime >= randomPauseTime) {
                redheart.drawSelf();
            }
        }
    }
    public void showHealth() {
        redheart.showHealth();
    }
}
