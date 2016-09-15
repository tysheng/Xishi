package me.tysheng.xishi;

import android.app.Application;
import android.content.Context;


/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
public class App extends Application {
    private static Context sContext;

    public static Context get() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }
}
