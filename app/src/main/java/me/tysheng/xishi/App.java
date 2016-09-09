package me.tysheng.xishi;

import android.app.Application;
import android.content.Context;

import me.tysheng.xishi.utils.fastcache.FastCache;


/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        try {
            FastCache.init(this, 1024 * 50); //in bytes
        } catch (Exception e) {
            //failure
        }
    }

    private static Context sContext;

    public static Context get() {
        return sContext;
    }
}
