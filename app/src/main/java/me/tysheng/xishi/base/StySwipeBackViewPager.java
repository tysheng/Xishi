package me.tysheng.xishi.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Sty
 * Date: 16/9/3 12:40.
 */
public class StySwipeBackViewPager extends ViewPager {
    public StySwipeBackViewPager(Context context) {
        super(context);
    }

    public StySwipeBackViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean swipe;
    float x_tmp1, x_tmp2;
    private boolean b;

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        swipe = position == 0 && offsetPixels == 0;
        super.onPageScrolled(position, offset, offsetPixels);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipe) {
            float x = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x_tmp1 = x;
                    break;
                case MotionEvent.ACTION_UP:
                    x_tmp2 = x;
                    b = x_tmp2 - x_tmp1 > 8;
                    break;
            }
        }
        return !b && super.onTouchEvent(event);
    }

}
