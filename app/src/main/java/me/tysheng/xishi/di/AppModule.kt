package me.tysheng.xishi.di

import me.tysheng.xishi.adapter.AlbumAdapter
import me.tysheng.xishi.adapter.MainsAdapter
import me.tysheng.xishi.module.album.AlbumContract
import me.tysheng.xishi.module.album.AlbumPresenter
import me.tysheng.xishi.module.main.MainContract
import me.tysheng.xishi.module.main.MainPresenter
import me.tysheng.xishi.net.XishiRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * Created by tysheng
 * Date: 24/8/18 11:52 AM.
 * Email: tyshengsx@gmail.com
 */
object ModuleName {
    const val ALBUM = "album"
    const val MAIN = "main"
}

val appModule = module(createOnStart = false) {
    single { XishiRetrofit(androidContext()) }
    single { get<XishiRetrofit>().createNgService() }
    single { get<XishiRetrofit>().createXishiService() }
}

val funcModules = module(createOnStart = false) {
    module(ModuleName.ALBUM) {
        single { AlbumPresenter(get(),get()) as AlbumContract.Presenter }
        factory { AlbumAdapter() }
    }
    module(ModuleName.MAIN) {
        single { MainPresenter(get(), get()) as MainContract.Presenter }
        factory { MainsAdapter() }
    }
}

