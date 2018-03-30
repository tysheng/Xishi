package me.tysheng.xishi.utils.swipeback

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View


/**
 * @author Yrom
 */
class SwipeBackActivityHelper(private val mActivity: Activity) {

    var swipeBackLayout: SwipeBackLayout? = null
        private set

    fun onActivityCreate() {
        mActivity.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mActivity.window.decorView.setBackgroundDrawable(null)
        swipeBackLayout = SwipeBackLayout(mActivity)
        //        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
        //            @Override
        //            public void onScrollStateChange(int state, float scrollPercent) {
        //            }
        //
        //            @Override
        //            public void onEdgeTouch(int edgeFlag) {
        //            }
        //
        //            @Override
        //            public void onScrollOverThreshold() {
        //
        //            }
        //        });
    }

    fun onPostCreate() {
        swipeBackLayout!!.attachToActivity(mActivity)
    }

    fun findViewById(id: Int): View? {
        return if (swipeBackLayout != null) {
            swipeBackLayout!!.findViewById(id)
        } else null
    }
}
