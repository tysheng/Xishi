package me.tysheng.xishi.ui

import android.app.Activity
import android.text.TextUtils
import me.tysheng.xishi.BuildConfig
import me.tysheng.xishi.Constants
import me.tysheng.xishi.data.Album
import me.tysheng.xishi.data.Mains
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.AlipayZeroSdk
import me.tysheng.xishi.utils.RxHelper
import me.tysheng.xishi.utils.SystemUtil
import me.tysheng.xishi.utils.TySubscriber

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

    interface Presenter {
        fun onItemClick(position: Int, activity: Activity)

        fun fetchData(firstTime: Boolean)
    }
}

class MainPresenter constructor(
        val view: MainContract.View,
        val service: XishiService
) : MainContract.Presenter {
    private var page = 1
    override fun fetchData(firstTime: Boolean) {
        if (firstTime) {
            page = 1
        } else {
            page++
        }
        service.getMains(page)
                .compose(view.bindUntilDestroy())
                .compose(RxHelper.ioToMain())
                .doOnTerminate {

                    view.stopRefresh()
                }
                .subscribe(object : TySubscriber<Mains>() {
                    override fun onError(e: Throwable) {
                        super.onError(e)
                        if (TextUtils.equals("HTTP 404 Not Found", e.message)) {
                            view.onEnd()
                        } else {
                            view.onError()
                        }
                    }

                    override fun next(t: Mains) {
                        if (firstTime) {
                            view.setNewData(t.album)
                        } else {
                            view.addData(t.album)
                        }
                        view.loadMoreComplete()
                    }
                })
    }

    override fun onItemClick(position: Int, activity: Activity) {
        when (position) {
            0 -> SystemUtil.sendEmail(activity)
            1 -> SystemUtil.shareAppShop(activity, BuildConfig.APPLICATION_ID)
            2 -> if (AlipayZeroSdk.hasInstalledAlipayClient(activity)) {
                if (!AlipayZeroSdk.startAlipayClient(activity, Constants.AliPayCode)) {
                    view.showAlipayFail()
                }
            } else {
                view.showAlipayFail()
            }
            3 -> view.copyEmailAddress()
            4 -> view.setDayNightMode()
        }
    }
}