package me.tysheng.xishi.utils

import android.content.Context
import android.support.annotation.IntegerRes
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

/**
 * Created by Sty
 * Date: 16/9/6 09:32.
 */
class ImageLoadHelper(private val mContext: Context) {

    private var mUrl: String? = null
    private var mPlaceholder: Int = 0

    fun placeholder(@IntegerRes placeholder: Int): ImageLoadHelper {
        mPlaceholder = placeholder
        return this
    }

    fun into(target: ImageView) {
        val requestCreator = Picasso.get()
                .load(mUrl)
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder)
        requestCreator.into(target)
    }

    fun into(target: ImageView, callback: Callback) {
        val requestCreator = Picasso.get()
                .load(mUrl)
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder)
        requestCreator.into(target, callback)
    }

    fun load(url: String): ImageLoadHelper {
        mUrl = url
        return this
    }
}
