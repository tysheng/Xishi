package me.tysheng.xishi.di.component

import dagger.Component
import me.tysheng.xishi.di.module.AlbumModule
import me.tysheng.xishi.di.module.PerActivity
import me.tysheng.xishi.ui.AlbumActivity
import me.tysheng.xishi.ui.AlbumContract

/**
 * Created by tysheng
 * Date: 1/4/18 12:51.
 * Email: tyshengsx@gmail.com
 */
@Component(dependencies = [ApplicationComponent::class], modules = [AlbumModule::class])
@PerActivity
interface AlbumComponent {
    fun presenter(): AlbumContract.Presenter
    fun inject(albumActivity: AlbumActivity)

}