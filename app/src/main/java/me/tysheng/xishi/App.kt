package me.tysheng.xishi

import android.app.Application
import io.reactivex.plugins.RxJavaPlugins
import me.tysheng.xishi.dagger.component.ApplicationComponent
import me.tysheng.xishi.dagger.component.DaggerApplicationComponent
import me.tysheng.xishi.dagger.module.ApplicationModule
import timber.log.Timber
import timber.log.Timber.DebugTree




/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        applicationComponent.inject(this)
        Timber.plant(DebugTree())
        RxJavaPlugins.setErrorHandler {
            Timber.e(it)
        }
    }
}
