package me.tysheng.xishi.utils

import android.databinding.BindingAdapter
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("show")
    fun setShow(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("hide")
    fun setHide(view: View, visible: Boolean) {
        setShow(view, !visible)
    }


    @JvmStatic
    @BindingAdapter("invisible")
    fun setInvisible(view: View, invisible: Boolean) {
        view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("titleConvert")
    fun titleConvert(view: TextView, title: String) {
        val s = if (title.startsWith("20")) title.substring(10).trim({ it <= ' ' }) else title
        view.text = s
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(view: ImageView, url: String) {
        val requestCreator = Picasso.get().load(url)
        requestCreator.into(view)
    }

    @JvmStatic
    @BindingAdapter("timeConvert")
    fun timeConvert(view: TextView, s: String) {
        var s = s
        s = s.substring(0, 10)
        val month = Integer.valueOf(s.substring(5, 7))
        val day = Integer.valueOf(s.substring(8))
        val string = SpannableString("$day/$month")
        string.setSpan(RelativeSizeSpan(1.4f), 0, day.toString().length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        view.text = string
    }

}
