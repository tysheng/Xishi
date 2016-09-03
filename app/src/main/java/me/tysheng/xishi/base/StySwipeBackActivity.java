package me.tysheng.xishi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Sty
 * Date: 16/9/3 11:46.
 */
public abstract class StySwipeBackActivity extends AppCompatActivity {
    protected GestureDetector mGestureDetector;

    /**
     * 快速滑动
     */
    protected static final int SWIPE_TO_FINISH_VELOCITY_HIGH = 3000;
    protected static final float SWIPE_TO_FINISH_DELTA_X_LOW = 100;

    /**
     * 慢速滑动
     */
    protected static final int SWIPE_TO_FINISH_VELOCITY_LOW = 300;
    protected static final float SWIPE_TO_FINISH_DELTA_X_HIGH = 500;
    protected boolean openSwiped = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (openSwiped){
            mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float deltaX = e2.getX() - e1.getX();
                    float deltaY = e2.getY() - e1.getY();

                    if ((velocityX > SWIPE_TO_FINISH_VELOCITY_HIGH && deltaX > SWIPE_TO_FINISH_DELTA_X_LOW) ||
                            (velocityX > SWIPE_TO_FINISH_VELOCITY_LOW && deltaX > SWIPE_TO_FINISH_DELTA_X_HIGH)) {
                        /**
                         * Y 轴上的处理
                         */
                        if (Math.abs(velocityY)<SWIPE_TO_FINISH_VELOCITY_HIGH&&Math.abs(deltaY)<SWIPE_TO_FINISH_DELTA_X_HIGH){
                            finish();
                            return true;
                        }

                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (openSwiped)
            return mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
