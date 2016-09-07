package me.tysheng.xishi.utils;

import android.util.Log;

/**
 * Created by Sty
 * Date: 16/8/27 13:09.
 */
public class LogUtil {
    private static boolean Debug = true;
    private static final String TAG = "sty";
    public static void d(String s){
        if (Debug){
            Log.d(TAG,s);
        }
    }
}
