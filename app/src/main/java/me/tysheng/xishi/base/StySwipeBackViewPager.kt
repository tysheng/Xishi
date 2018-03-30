package me.tysheng.xishi.base

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Sty
 * Date: 16/9/3 12:40.
 */
class StySwipeBackViewPager : ViewPager {

    private var swipe: Boolean = false
    internal var x_tmp1: Float = 0.toFloat()
    internal var x_tmp2: Float = 0.toFloat()
    private var b: Boolean = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
        swipe = position == 0 && offsetPixels == 0
        super.onPageScrolled(position, offset, offsetPixels)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (swipe) {
            val x = event.x
            when (event.action) {
                MotionEvent.ACTION_DOWN -> x_tmp1 = x
                MotionEvent.ACTION_UP -> {
                    x_tmp2 = x
                    b = x_tmp2 - x_tmp1 > 8
                }
            }
        }
        return !b && super.onTouchEvent(event)
    }

}
