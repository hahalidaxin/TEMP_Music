package com.example.daxinli.tempmusic.thread;


import com.example.daxinli.tempmusic.action.Action;
import com.example.daxinli.tempmusic.constant.GameData;

/**
 * Created by hahal on 2018/2/26.
 * 动作执行类 执行动作队列
 */
public class ActionThread extends Thread {
    private boolean pause = false;
    private boolean flag = true;
    public ActionThread() {
        this.setName("ActionThread");
    }
    @Override
    public void run() {
        while(flag) {
            if(!pause) {
                Action a = null;
                synchronized (GameData.lock) {       //没有间断的执行 查看动作队列 有动作就执行
                    a = GameData.aq.poll();
                }
                if (a != null) {
                    try {
                        a.doAction();
                    } catch (Exception e) {
                        break;                      //发生异常且关闭动作线程
                    }
                }
            }
        }
    }
    public void setPause(boolean pause) { this.pause = pause; }
    public void setFlag(boolean flag) { this.flag = flag ;}
    public void turnPause() { this.pause = !this.pause; }
}
