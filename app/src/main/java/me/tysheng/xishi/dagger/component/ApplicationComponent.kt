package me.tysheng.xishi.dagger.component

import android.support.v4.app.Fragment
import dagger.Component
import me.tysheng.xishi.App
import me.tysheng.xishi.base.BaseActivity
import me.tysheng.xishi.dagger.module.ApplicationModule
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
