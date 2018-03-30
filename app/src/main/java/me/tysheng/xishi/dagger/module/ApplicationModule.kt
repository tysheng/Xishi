package me.tysheng.xishi.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import me.tysheng.xishi.net.XishiRetrofit
import me.tysheng.xishi.net.XishiService
import javax.inject.Singleton

/**
 * Created by tysheng
 * Date: 2017/4/22 17:32.
 * Email: tyshengsx@gmail.com
 */
@Module
open class ApplicationModule(private val context: Application) {

    @Provides
    @Singleton
    open fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @Singleton
    open fun provideXishiService(context: Context): XishiService {
        return XishiRetrofit(context).get()
    }

}
