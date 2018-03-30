package me.tysheng.xishi.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by Sty
 * Date: 16/9/3 11:46.
 */
abstract class StySwipeBackActivity : AppCompatActivity() {
    protected lateinit var mGestureDetector: GestureDetector
    protected var openSwiped = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (openSwiped) {
            mGestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    val deltaX = e2.x - e1.x
                    val deltaY = e2.y - e1.y

                    if (velocityX > SWIPE_TO_FINISH_VELOCITY_HIGH && deltaX > SWIPE_TO_FINISH_DELTA_X_LOW || velocityX > SWIPE_TO_FINISH_VELOCITY_LOW && deltaX > SWIPE_TO_FINISH_DELTA_X_HIGH) {
                        /**
                         * Y 轴上的处理
                         */
                        if (Math.abs(velocityY) < SWIPE_TO_FINISH_VELOCITY_HIGH && Math.abs(deltaY) < SWIPE_TO_FINISH_DELTA_X_HIGH) {
                            finish()
                            return true
                        }

                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (openSwiped) mGestureDetector.onTouchEvent(event) else super.onTouchEvent(event)
    }

    companion object {

        /**
         * 快速滑动
         */
        protected val SWIPE_TO_FINISH_VELOCITY_HIGH = 3000
        protected val SWIPE_TO_FINISH_DELTA_X_LOW = 100f

        /**
         * 慢速滑动
         */
        protected val SWIPE_TO_FINISH_VELOCITY_LOW = 300
        protected val SWIPE_TO_FINISH_DELTA_X_HIGH = 500f
    }
}
