package me.tysheng.xishi.di

import me.tysheng.xishi.adapter.AlbumAdapter
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.net.XishiRetrofit
import me.tysheng.xishi.ui.album.AlbumContract
import me.tysheng.xishi.ui.album.AlbumPresenter
import me.tysheng.xishi.ui.main.MainContract
import me.tysheng.xishi.ui.main.MainPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * Created by tysheng
 * Date: 24/8/18 11:52 AM.
 * Email: tyshengsx@gmail.com
 */
object ModuleName{
    const val ALBUM="album"
    const val MAIN="main"
}
val appModule = module {
    single { XishiRetrofit(androidContext()).get() }
}

val funcModules = module {
    module(ModuleName.ALBUM) {
        single { AlbumPresenter(get()) as AlbumContract.Presenter }
        factory { AlbumAdapter() }
    }
    module(ModuleName.MAIN) {
        single { MainPresenter(get()) as MainContract.Presenter }
        factory { MainsAdapter() }
    }
}

