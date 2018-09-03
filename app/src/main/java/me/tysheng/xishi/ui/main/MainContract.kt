package me.tysheng.xishi.ui.main

import me.tysheng.xishi.data.Album
import me.tysheng.xishi.ui.AbstractPresenter
import me.tysheng.xishi.ui.BaseView
import me.tysheng.xishi.ui.MainDialogAction

/**
 * Created by tysheng
 * Date: 31/3/18 22:35.
 * Email: tyshengsx@gmail.com
 */
interface MainContract {
    interface View : BaseView<MainContract.Presenter> {
        fun stopRefresh()
        fun onEnd()
        fun onError()
        fun setNewData(album: List<Album>)
        fun addData(album: List<Album>)
        fun loadMoreComplete()
        fun showAlipayFail()
        fun copyEmailAddress()
        fun setDayNightMode()
    }

    abstract class Presenter : AbstractPresenter<MainContract.View, MainContract.Presenter>() {
        override lateinit var view: MainContract.View
        abstract fun onItemClick(action: MainDialogAction)

        abstract fun fetchData(firstTime: Boolean)

    }
}

