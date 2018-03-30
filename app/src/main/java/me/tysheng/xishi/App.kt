package me.tysheng.xishi

import android.app.Application

import me.tysheng.xishi.dagger.component.ApplicationComponent
import me.tysheng.xishi.dagger.component.DaggerApplicationComponent
import me.tysheng.xishi.dagger.module.ApplicationModule


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
    }
}