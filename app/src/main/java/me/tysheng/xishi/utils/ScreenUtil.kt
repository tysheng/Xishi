package me.tysheng.xishi.utils

import android.app.Activity
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View

/**
 * Created by Sty
 * Date: 16/9/2 09:11.
 */
object ScreenUtil {
    /**
     * 获取屏幕信息:宽 高等...
     *
     * @param activity activity
     * @return DisplayMetrics
     */
    fun getScreen(activity: Activity): DisplayMetrics {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

    /**
     * 获取 view 在screen中的绝对位置坐标
     *
     * @param view view
     * @return int[]
     */
    fun getLocationInScreen(view: View): IntArray {
        val locations = IntArray(2)
        view.getLocationOnScreen(locations)
        return locations
    }

    /**
     * dp2px
     */
    fun dip2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px2dp
     */
    fun px2dip(pxValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

}
