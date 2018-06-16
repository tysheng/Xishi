package me.tysheng.xishi.ui.album

import android.Manifest
import android.support.v4.view.ViewPager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.R
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.ui.BaseActivity
import me.tysheng.xishi.utils.ImageUtil
import me.tysheng.xishi.utils.RxHelper
import me.tysheng.xishi.utils.SystemUtil

class AlbumPresenter constructor(
        val service: XishiService,
        val view: AlbumContract.View) :
        AlbumContract.Presenter() {
    private var amount = 0
    private var id = 0

    override val viewPagerScrollListener: ViewPager.OnPageChangeListener by lazy {
        object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                view.selected(amount, position)
            }
        }
    }

    override fun setAlbumId(id: Int) {
        this.id = id
    }

    override fun fetchData() {
        service.getDayAlbums(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.picture?.run {
                        amount = size
                        view.setData(this)
                        view.selected(amount, 0)
                    }
                }
                .also {
                    addToSubscription(it)
                }
    }

    override fun saveImageToGallery(activity: BaseActivity, url: String, position: Int) {
        RxPermissions(activity)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (!it) {
                        view.showPermissionDenied()
                    }
                }
                .observeOn(Schedulers.io())
                .filter {
                    it
                }
                .flatMap {
                    ImageUtil.saveImageToGallery(activity, url)
                }
                .compose(RxHelper.ioToMain())
                .subscribe {
                    val appDir = ImageUtil.saveDir
                    val msg = String.format(activity.getString(R.string.picture_has_save_to),
                            appDir.absolutePath)
                    when (position) {
                        0 -> msg.toast()
                        1 -> SystemUtil.shareVia(activity, activity.getString(R.string.share_text), activity.getString(R.string.share_to), it)
                        2 -> ImageUtil.shareImage2Wechat(activity, it)
                    }
                }
                .also {
                    addToSubscription(it)
                }
    }
}