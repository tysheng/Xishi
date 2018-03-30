package me.tysheng.xishi.inter

import android.support.v7.util.DiffUtil
import android.text.TextUtils

import java.util.ArrayList

import me.tysheng.xishi.bean.Album

/**
 * Created by Sty
 * Date: 16/9/21 13:18.
 */

class DataCallback(private val oldList: ArrayList<Album>, private val newList: ArrayList<Album>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return TextUtils.equals(oldList[oldItemPosition].id, newList[newItemPosition].id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return TextUtils.equals(oldList[oldItemPosition].url, newList[newItemPosition].url)
    }
}
