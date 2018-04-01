package me.tysheng.xishi.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.support.v4.view.ViewPager
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.R
import me.tysheng.xishi.data.DayAlbums
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.ext.toast
import me.tysheng.xishi.net.XishiService
import me.tysheng.xishi.utils.ImageUtil
import me.tysheng.xishi.utils.RxHelper
import me.tysheng.xishi.utils.SystemUtil
import me.tysheng.xishi.utils.TySubscriber
import javax.inject.Inject

/**
 * Created by tysheng
 * Date: 1/4/18 12:26.
 * Email: tyshengsx@gmail.com
 */
interface AlbumContract {
    interface View : BaseView {
        fun selected(amount: Int, position: Int)
        fun setData(picture: List<Picture>)
        fun showPermissionDenied()
        fun finish()

    }

    interface Presenter : BasePresenter, ViewPager.OnPageChangeListener {

        fun fetchData()
        fun parseIntent(intent: Intent?)
        fun saveImageToGallery(activity: BaseActivity, url: String, position: Int)

    }
}

class AlbumPresenter @Inject constructor(
        val service: XishiService,
        val view: AlbumContract.View) :
        AlbumContract.Presenter {
    var amount = 0
    private var countForFinish = 0
    var id = 0
    override fun parseIntent(intent: Intent?) {
        id = intent?.getIntExtra(AlbumActivity.KEY_ALBUMS, 1322) ?: 0
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position == amount - 1 && positionOffsetPixels == 0) {
            countForFinish += 1
            if (countForFinish > 8) {
                view.finish()
            }
        } else {
            countForFinish = 0
        }
    }

    override fun onPageSelected(position: Int) {
        view.selected(amount, position)
    }

    override fun fetchData() {
        service.getDayAlbums(id)
                .compose(view.bindUntilDestroy())
                .compose(RxHelper.ioToMain())
                .subscribe(object : TySubscriber<DayAlbums>() {
                    override fun next(t: DayAlbums) {
                        amount = t.picture.size
                        view.setData(t.picture)
                        view.selected(amount, 0)
                    }
                })
    }

    override fun saveImageToGallery(activity: BaseActivity, imgUrl: String, positionInDialog: Int) {
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
                    ImageUtil.saveImageToGallery(activity, imgUrl)
                }
                .compose(RxHelper.ioToMain())
                .subscribe(object : TySubscriber<Uri>() {
                    override fun next(uri: Uri) {
                        val appDir = ImageUtil.saveDir
                        val msg = String.format(activity.getString(R.string.picture_has_save_to),
                                appDir.absolutePath)
                        when (positionInDialog) {
                            0 -> msg.toast()
                            1 -> SystemUtil.shareVia(activity, activity.getString(R.string.share_text), activity.getString(R.string.share_to), uri)
                            2 -> ImageUtil.shareImage2Wechat(activity, uri)
                        }
                    }
                })
    }
}