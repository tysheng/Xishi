package me.tysheng.xishi.di.module

import dagger.Module
import dagger.Provides
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.ui.album.AlbumContract
import me.tysheng.xishi.ui.album.AlbumPresenter

/**
 * Created by tysheng
 * Date: 1/4/18 12:52.
 * Email: tyshengsx@gmail.com
 */
@Module
class AlbumModule(val view: AlbumContract.View) {
    @Provides
    @PerActivity
    fun providePresenter(service: XishiService): AlbumContract.Presenter =
            AlbumPresenter(service, view)
}