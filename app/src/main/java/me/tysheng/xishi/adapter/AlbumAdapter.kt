package me.tysheng.xishi.adapter

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import me.tysheng.xishi.R
import me.tysheng.xishi.bean.Picture
import me.tysheng.xishi.ui.AlbumActivity
import java.util.*
import javax.inject.Inject

/**
 * Created by Sty
 * Date: 16/8/23 20:17.
 */
class AlbumAdapter @Inject
constructor(activity: Activity) : BaseGalleryAdapter<Picture>(ArrayList(), activity) {
    init {
    }


    override fun initOtherView(view: View, position: Int) {


    }

    override fun initPhotoView(photoView: PhotoView, position: Int) {
        photoView.setOnLongClickListener {
            AlertDialog.Builder(mActivity, R.style.BlackDialog)
                    .setItems(arrayOf("保存", "分享", "分享给微信好友")) { dialogInterface, i ->
                        dialogInterface.dismiss()
                        (mActivity as AlbumActivity).saveImageToGallery(position, i)
                    }.show()
            true
        }
        photoView.setOnViewTapListener { view, x, y -> (mActivity as AlbumActivity).hideOrShow() }
    }

    override fun setItemUrl(position: Int): String {
        return mImages[position].url
    }

    override fun setLayoutId(): Int {
        return R.layout.item_album
    }


    override fun setPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}
