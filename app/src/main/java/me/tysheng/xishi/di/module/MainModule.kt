package me.tysheng.xishi.di.module

import dagger.Module
import dagger.Provides
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.ui.MainContract
import me.tysheng.xishi.ui.MainPresenter

/**
 * Created by tysheng
 * Date: 31/3/18 23:02.
 * Email: tyshengsx@gmail.com
 */

@Module
class MainModule(val view: MainContract.View) {
    @Provides
    @PerActivity
    fun provideView() = view

    @Provides
    @PerActivity
    fun providePresenter(view: MainContract.View, service: XishiService): MainContract.Presenter = MainPresenter(view, service)
}