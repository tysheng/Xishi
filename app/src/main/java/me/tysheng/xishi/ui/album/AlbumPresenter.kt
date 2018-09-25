package me.tysheng.xishi.ui.album

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.R
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.ext.toShot
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.net.NgService
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.ImageUtil
import me.tysheng.xishi.utils.RxHelper
import me.tysheng.xishi.utils.SystemUtil

class AlbumPresenter constructor(
        private val service: NgService,
        private val xishiService: XishiService) :
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
                .addToSubscription()
    }

    override fun saveImage(activity: AppCompatActivity, url: String) {
        saveImageInternal(activity, url) {
            val appDir = ImageUtil.saveDir
            val msg = String.format(activity.getString(R.string.picture_has_save_to),
                    appDir.absolutePath)
            msg.toast()
        }
    }

    override fun shareImage(activity: AppCompatActivity, url: String) {
        saveImageInternal(activity, url) {
            SystemUtil.shareVia(activity, activity.getString(R.string.share_text), activity.getString(R.string.share_to), it)
        }
    }

    private fun saveImageInternal(activity: AppCompatActivity, url: String, block: (uri: Uri) -> Unit) {
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
                    block(it)
                }
                .addToSubscription()
    }

    override fun bookmarkShot(picture: Picture) {
        xishiService.addShot(picture.toShot())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view.bookmarkSuccess(it)
                }
                .addToSubscription()

    }

}