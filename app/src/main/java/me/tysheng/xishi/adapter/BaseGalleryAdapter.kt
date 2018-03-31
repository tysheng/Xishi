package me.tysheng.xishi.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import me.tysheng.xishi.R

/**
 * Created by shengtianyang on 16/4/30.
 */
abstract class BaseGalleryAdapter<T>(protected var images: List<T>
                                     , protected var context: Context) : PagerAdapter() {

    var data: List<T>
        get() = images
        set(images) {
            this.images = images
            notifyDataSetChanged()
        }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(setLayoutId(), container, false) as ViewGroup
        val imageView = view.findViewById<View>(R.id.imageView) as PhotoView
        val progressBar = view.findViewById<View>(setProgressBarId()) as ProgressBar
        initOtherView(view, position)
        imageView.minimumScale = 1f
        Picasso.get()
                .load(setItemUrl(position))
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception) {

                    }
                })
        container.addView(view)
        return view
    }

    protected open fun initOtherView(view: View, position: Int){

    }

    protected open fun initPhotoView(photoView: PhotoView, position: Int){

    }

    protected abstract fun setItemUrl(position: Int): String

    protected abstract fun setLayoutId(): Int

    protected open fun setProgressBarId(): Int {
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
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}
