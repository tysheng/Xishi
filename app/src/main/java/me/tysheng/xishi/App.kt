package me.tysheng.xishi

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins
import me.tysheng.xishi.di.appModule
import me.tysheng.xishi.di.funcModules
import org.koin.android.ext.android.startKoin
import timber.log.Timber


/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        RxJavaPlugins.setErrorHandler {
            Timber.e(it)
        }
        val modules = listOf(appModule, funcModules)

        startKoin(this, modules)

    }

    companion object {
        lateinit var instance: App
    }
}
