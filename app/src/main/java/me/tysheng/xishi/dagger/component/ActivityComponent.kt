package me.tysheng.xishi.dagger.component

import android.app.Activity
import dagger.Component
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.dagger.module.ActivityModule
import me.tysheng.xishi.dagger.module.PerActivity
import me.tysheng.xishi.ui.AlbumActivity
import me.tysheng.xishi.ui.MainActivity

/**
 * Created by sll on 2016/3/8.
 */
@PerActivity
@Component(dependencies = [(ApplicationComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun activity(): Activity

    fun inject(activity: MainActivity)

    fun inject(activity: AlbumActivity)

    fun inject(adapter: MainsAdapter)
}
