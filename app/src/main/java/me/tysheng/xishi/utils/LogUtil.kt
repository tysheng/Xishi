package me.tysheng.xishi.utils

import android.util.Log

import me.tysheng.xishi.BuildConfig

/**
 * Created by Sty
 * Date: 16/8/27 13:09.
 */
object LogUtil {
    private val TAG = "sty"
    private val Debug = BuildConfig.DEBUG

    fun d(s: String?) {
        if (Debug) {
            Log.d(TAG, s)
        }
    }
}
