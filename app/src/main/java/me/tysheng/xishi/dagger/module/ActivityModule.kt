package me.tysheng.xishi.dagger.module

import android.app.Activity

import dagger.Module
import dagger.Provides

/**
 * Created by tysheng
 * Date: 2017/4/22 20:47.
 * Email: tyshengsx@gmail.com
 */

@Module
class ActivityModule(private val mActivity: Activity) {


    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return mActivity
    }
}
