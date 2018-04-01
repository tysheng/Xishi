package me.tysheng.xishi.di.component

import dagger.Component
import me.tysheng.xishi.App
import me.tysheng.xishi.di.module.ApplicationModule
import me.tysheng.xishi.net.XishiService
import javax.inject.Singleton

/**
 * Created by tysheng
 * Date: 2017/4/22 17:38.
 * Email: tyshengsx@gmail.com
 */
@Singleton
@Component(modules = [(ApplicationModule::class)])
interface ApplicationComponent {
//    fun instance(): Context
//
    fun service(): XishiService

    fun inject(app: App)
}
