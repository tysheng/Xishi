package me.tysheng.xishi

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins
import me.tysheng.xishi.dagger.component.ApplicationComponent
import me.tysheng.xishi.dagger.component.DaggerApplicationComponent
import me.tysheng.xishi.dagger.module.ApplicationModule
import timber.log.Timber


/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
class App : Application() {

    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        applicationComponent.inject(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        RxJavaPlugins.setErrorHandler {
            Timber.e(it)
        }
    }

    companion object {
         lateinit var instance: App
    }
}
