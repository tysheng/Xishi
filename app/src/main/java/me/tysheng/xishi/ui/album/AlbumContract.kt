package me.tysheng.xishi.ui.album

import android.support.v4.view.ViewPager
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.ui.AbstractPresenter
import me.tysheng.xishi.ui.BaseActivity
import me.tysheng.xishi.ui.BaseView

/**
 * Created by tysheng
 * Date: 1/4/18 12:26.
 * Email: tyshengsx@gmail.com
 */
interface AlbumContract {
    interface View : BaseView<Presenter> {
        fun selected(amount: Int, position: Int)
        fun setData(picture: List<Picture>)
        fun showPermissionDenied()
    }

    abstract class Presenter : AbstractPresenter<View, Presenter>() {

        override lateinit var view: View
        abstract fun fetchData()
        abstract fun saveImageToGallery(activity: BaseActivity, url: String, position: Int)

        abstract fun setAlbumId(id: Int)
        abstract val viewPagerScrollListener: ViewPager.OnPageChangeListener
    }
}

