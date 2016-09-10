package me.tysheng.xishi.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Sty
 * Date: 16/9/2 09:11.
 */
public class ScreenUtil {
    /**
     * 获取屏幕信息:宽 高等...
     *
     * @param activity activity
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreen(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 获取 view 在screen中的绝对位置坐标
     *
     * @param view view
     * @return int[]
     */
    public static int[] getLocationInScreen(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        return locations;
    }

    /**
     * dp2px
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
