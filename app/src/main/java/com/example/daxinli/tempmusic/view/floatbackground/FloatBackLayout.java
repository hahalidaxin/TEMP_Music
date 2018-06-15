package com.example.daxinli.tempmusic.view.floatbackground;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.daxinli.tempmusic.util.SFUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DaxinLi on 2018/6/14.
 */

public class FloatBackLayout extends FrameLayout {
    private static final String TAG = "FloatBackLayout";
    private static final float DELTAX = 100;

    public static final long DELAY = 26;            //刷新一帧的间隔时长

    List<FloatObject> floats = new ArrayList<>();
    private int RID_background=-1;
    private Bitmap pic_background;
    Context mcontext;

    public FloatBackLayout(Context context) {
        super(context);
        this.mcontext = context;
    }

    public FloatBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // call onDraw in ViewGroup
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initFloatObject(w, h);
        }
    }

    private float lastX,lastY,deltaX,deltaY;
    private long lastDownTime,downTimeSpan;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //实现背景图片可以被拖拽的效果
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX=x; lastY=y;
                lastDownTime = System.currentTimeMillis();
                for(FloatObject ftoj:floats)
                    if(SFUtil.distance(x,y,ftoj.x+ftoj.objWidth/2,ftoj.y+ftoj.objHeight/2)<DELTAX) {
                        ftoj.alpha = 255;
                    }
                break;
            case MotionEvent.ACTION_UP:
                for(FloatObject oj:floats) if(oj.status==FloatObject.DRAG) {
                    oj.setStatus(FloatObject.THROW);
                    oj.deltaX = deltaX/downTimeSpan*DELAY; oj.deltaY = deltaY/downTimeSpan*DELAY;   //计算每次刷新应该移动的位置差
                }
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = x-lastX; deltaY=y-lastY;
                for(FloatObject ftoj:floats)
                    if(SFUtil.distance(x,y,ftoj.x+ftoj.objWidth/2,ftoj.y+ftoj.objHeight/2)<DELTAX) {
                        ftoj.alpha = 255;
                        ftoj.setStatus(FloatObject.DRAG);
                        ftoj.setXY(ftoj.x+deltaX,ftoj.y+deltaY);
                    }
                lastX = x; lastY = y;
                downTimeSpan = System.currentTimeMillis()-lastDownTime;
                lastDownTime = System.currentTimeMillis();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if(this.RID_background!=-1) {   //首先绘制背景层
            canvas.drawBitmap(pic_background,0,0,paint);
        }
        for (FloatObject floatObject : floats) {
            floatObject.drawFloatItem(canvas);
        }
        // 隔一段时间重绘一次, 动画效果
        getHandler().postDelayed(runnable, DELAY);

    }

    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
            // 控制帧数
        }
    };

    public void startFloat() {
        for (FloatObject floatObject : floats) {
            floatObject.setStatus(FloatObject.START);
        }
    }

    public void endFloat() {
        for (FloatObject floatObject : floats) {
            floatObject.setStatus(FloatObject.END);
        }
    }

    public void addFloatView(FloatObject floatObject) {
        if (floatObject == null) {
            return;
        }
        floats.add(floatObject);
    }

    public void initFloatObject(int width, int height) {
        for (FloatObject floatObject : floats) {
            int x = (int) (floatObject.posX * width);
            int y = (int) (floatObject.posY * height);
            floatObject.init(x, y, width, height);
        }
    }
    public void setBackGround(Activity activity, int res) {
        this.RID_background = res;
        Bitmap bitmap = ((BitmapDrawable) activity.getResources().getDrawable(RID_background))
                .getBitmap();
        pic_background = SFUtil.resizeBitmap(bitmap,1080,1920);
    }
}