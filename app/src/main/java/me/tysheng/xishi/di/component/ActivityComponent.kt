package me.tysheng.xishi.di.component

import android.app.Activity
import dagger.Component
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.di.module.ActivityModule
import me.tysheng.xishi.di.module.PerActivity
import me.tysheng.xishi.ui.AlbumActivity

/**
 * Created by sll on 2016/3/8.
 */
@PerActivity
@Component(dependencies = [(ApplicationComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {
    fun activity(): Activity

    fun inject(activity: AlbumActivity)

    fun inject(adapter: MainsAdapter)
}
