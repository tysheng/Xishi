package me.tysheng.xishi.adapter

import com.github.chrisbanes.photoview.PhotoView
import me.tysheng.xishi.R
import me.tysheng.xishi.bean.Picture
import java.util.*
import javax.inject.Inject

/**
 * Created by Sty
 * Date: 16/8/23 20:17.
 */
class AlbumAdapter @Inject
constructor() : BaseGalleryAdapter<Picture>(ArrayList()) {

    var photoViewListener: PhotoViewListener? = null

    override fun initPhotoView(photoView: PhotoView, position: Int) {
        photoView.setOnLongClickListener {
            photoViewListener?.longClick(data[position])
            true
        }
        photoView.setOnViewTapListener { _, _, _ -> photoViewListener?.tap() }
    }

    override fun setItemUrl(position: Int): String {
        return images[position].url
    }

    override fun setLayoutId(): Int {
        return R.layout.item_album
    }

    override fun setPageTitle(position: Int): CharSequence {
        return position.toString()
    }
}

interface PhotoViewListener {
    fun tap()
    fun longClick(picture: Picture)
}