package me.tysheng.xishi.di.module

import android.app.Activity

import dagger.Module
import dagger.Provides

/**
 * Created by tysheng
 * Date: 2017/4/22 20:47.
 * Email: tyshengsx@gmail.com
 */

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return activity
    }
}