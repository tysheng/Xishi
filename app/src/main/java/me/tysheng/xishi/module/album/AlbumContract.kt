package me.tysheng.xishi.module.album

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.net.data.CommonResponse
import me.tysheng.xishi.module.AbstractPresenter
import me.tysheng.xishi.module.BaseView

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
        fun bookmarkSuccess(it: CommonResponse<Any>)
    }

    abstract class Presenter : AbstractPresenter<View, Presenter>() {

        override lateinit var view: View
        abstract fun fetchData()

        abstract fun setAlbumId(id: Int)
        abstract fun saveImage(activity: AppCompatActivity, url: String)
        abstract fun shareImage(activity: AppCompatActivity, url: String)
        abstract fun bookmarkShot(picture: Picture)

        abstract val viewPagerScrollListener: ViewPager.OnPageChangeListener
    }
}

