package me.tysheng.xishi.adapter

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.squareup.picasso.Picasso
import me.tysheng.xishi.R
import me.tysheng.xishi.data.Album
import javax.inject.Inject

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
class MainsAdapter @Inject
constructor() : BaseLoadMoreRecyclerViewAdapter<Album>(R.layout.item_mains, arrayListOf()) {
    override fun convert(helper: BaseViewHolder?, item: Album?) {
        helper?.setText(R.id.time, item?.addtime)
        loadImage(helper?.getView(R.id.imageView), item?.url)
        titleConvert(helper?.getView(R.id.title), item?.title)
        timeConvert(helper?.getView(R.id.time), item?.addtime)
    }

    private fun titleConvert(view: TextView?, title: String?) {
        if (title?.isEmpty() == true) {
            return
        }
        title!!
        val s = if (title.startsWith("20")) title.substring(10).trim({ it <= ' ' }) else title
        view?.text = s
    }

    private fun loadImage(view: ImageView?, url: String?) {
        val requestCreator = Picasso.get().load(url)
        requestCreator.into(view)
    }

    private fun timeConvert(view: TextView?, time: String?) {
        if (time?.isEmpty() == true) {
            return
        }
        time!!
        val str = time.substring(0, 10)
        val month = Integer.valueOf(str.substring(5, 7))
        val day = Integer.valueOf(str.substring(8))
        val string = SpannableString("$day/$month")
        string.setSpan(RelativeSizeSpan(1.4f), 0, day.toString().length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        view?.text = string
    }
}
