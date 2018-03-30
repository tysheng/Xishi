package me.tysheng.xishi.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import me.tysheng.xishi.R
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by shengtianyang on 16/4/30.
 */
abstract class BaseGalleryAdapter<T>(protected var mImages: List<T>, protected var mActivity: Activity) : PagerAdapter() {

    var data: List<T>
        get() = mImages
        set(images) {
            mImages = images
            notifyDataSetChanged()
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(mActivity).inflate(setLayoutId(), container, false) as ViewGroup
        val mAttacher: PhotoViewAttacher
        val imageView = view.findViewById<View>(R.id.imageView) as ImageView
        val progressBar = view.findViewById<View>(setProgressBarId()) as ProgressBar
        initOtherView(view, position)
        mAttacher = PhotoViewAttacher(imageView)
        mAttacher.scaleType = ImageView.ScaleType.FIT_CENTER
        mAttacher.minimumScale = 1f
        initAttacher(mAttacher, position)
        Picasso.get()
                .load(setItemUrl(position))
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                        mAttacher.update()
                    }

                    override fun onError(e: Exception) {

                    }
                })
        container.addView(view)
        return view
    }

    protected abstract fun initOtherView(view: View, position: Int)

    protected abstract fun initAttacher(attacher: PhotoViewAttacher, position: Int)

    protected abstract fun setItemUrl(position: Int): String

    protected abstract fun setLayoutId(): Int

    protected fun setProgressBarId(): Int {
        return R.id.progressBar
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return setPageTitle(position)
    }

    protected abstract fun setPageTitle(position: Int): CharSequence

    override fun getCount(): Int {
        return mImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}
