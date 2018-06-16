package me.tysheng.xishi.ui.main

import me.tysheng.xishi.data.Album
import me.tysheng.xishi.ui.BasePresenter
import me.tysheng.xishi.ui.BaseView
import me.tysheng.xishi.ui.MainDialogActionListener

/**
 * Created by tysheng
 * Date: 31/3/18 22:35.
 * Email: tyshengsx@gmail.com
 */
interface MainContract {
    interface View : BaseView {
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

    abstract class Presenter : BasePresenter() {
        abstract fun onItemClick(action: MainDialogActionListener)

        abstract fun fetchData(firstTime: Boolean)

    }
}

