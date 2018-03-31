package me.tysheng.xishi.di.component

import android.support.v4.app.Fragment
import dagger.Component
import me.tysheng.xishi.App
import me.tysheng.xishi.ui.BaseActivity
import me.tysheng.xishi.di.module.ApplicationModule
import me.tysheng.xishi.net.XishiService
import javax.inject.Singleton

/**
 * Created by tysheng
 * Date: 2017/4/22 17:38.
 * Email: tyshengsx@gmail.com
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
//    fun instance(): Context
//
    fun service(): XishiService

    fun inject(activity: BaseActivity)

    fun inject(app: App)

    fun inject(fragment: Fragment)
}
