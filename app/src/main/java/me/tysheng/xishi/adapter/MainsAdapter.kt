package me.tysheng.xishi.adapter

import me.tysheng.xishi.R
import me.tysheng.xishi.bean.Album
import me.tysheng.xishi.databinding.ItemMainsBinding
import javax.inject.Inject

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
class MainsAdapter @Inject
constructor() : BaseDataBindingAdapter<Album, ItemMainsBinding>(R.layout.item_mains, null) {

    override fun convert(binding: ItemMainsBinding?, item: Album) {
        binding!!.item = item
    }
}
