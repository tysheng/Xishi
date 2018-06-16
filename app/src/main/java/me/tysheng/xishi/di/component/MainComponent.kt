package me.tysheng.xishi.di.component

import dagger.Component
import me.tysheng.xishi.di.module.MainModule
import me.tysheng.xishi.di.module.PerActivity
import me.tysheng.xishi.ui.main.MainActivity

/**
 * Created by tysheng
 * Date: 31/3/18 23:08.
 * Email: tyshengsx@gmail.com
 */
@Component(dependencies = [ApplicationComponent::class], modules = [MainModule::class])
@PerActivity
interface MainComponent {

    fun inject(activity: MainActivity)
}