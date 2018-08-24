package me.tysheng.xishi.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import me.tysheng.xishi.BuildConfig
import me.tysheng.xishi.Constants
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.ui.*
import me.tysheng.xishi.utils.AlipayZeroSdk
import me.tysheng.xishi.utils.SystemUtil

class MainPresenter constructor(
        private val service: XishiService
) : MainContract.Presenter() {

    private var page = 1
    override fun fetchData(firstTime: Boolean) {
        if (firstTime) {
            page = 1
        } else {
            page++
        }
        service.getMains(page)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate {
                    view.stopRefresh()
                }
                .doOnError {
                    if (HTTP_404 == it.message) {
                        view.onEnd()
                    } else {
                        view.onError()
                    }
                }
                .subscribe {
                    it.album?.run {
                        if (firstTime) {
                            view.setNewData(this)
                        } else {
                            view.addData(this)
                        }
                    }
                    view.loadMoreComplete()
                }
                .also {
                    addToSubscription(it)
                }
    }

    override fun onItemClick(action: MainDialogActionListener) {
        when (action) {
            is SendEmail -> SystemUtil.sendEmail(action.activity)
            is ShareToStore -> SystemUtil.shareAppShop(action.activity, BuildConfig.APPLICATION_ID)
            is JumpToAlipay -> if (AlipayZeroSdk.hasInstalledAlipayClient(action.activity)) {
                if (!AlipayZeroSdk.startAlipayClient(action.activity, Constants.AliPayCode)) {
                    view.showAlipayFail()
                }
            } else {
                view.showAlipayFail()
            }
            is CopyEmail -> view.copyEmailAddress()
            is SwitchDayNightMode -> view.setDayNightMode()
        }
    }

    companion object {
        private const val HTTP_404 = "HTTP 404 Not Found"

    }
}