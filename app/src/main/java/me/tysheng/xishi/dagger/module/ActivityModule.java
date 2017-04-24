package me.tysheng.xishi.dagger.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tysheng
 * Date: 2017/4/22 20:47.
 * Email: tyshengsx@gmail.com
 */

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }


    @Provides
    @PerActivity
    public Activity provideActivity() {
        return mActivity;
    }
}
