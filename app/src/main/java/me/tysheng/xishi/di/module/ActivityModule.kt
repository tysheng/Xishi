package me.tysheng.xishi.di.module

import android.app.Activity

import dagger.Module
import dagger.Provides
import me.tysheng.xishi.ui.BaseActivity

/**
 * Created by tysheng
 * Date: 2017/4/22 20:47.
 * Email: tyshengsx@gmail.com
 */

@Module
class ActivityModule(private val activity: BaseActivity) {

    @Provides
    @PerActivity
    fun provideActivity(): Activity {
        return activity
    }
}
