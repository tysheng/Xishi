package me.tysheng.xishi.adapter

import android.view.LayoutInflater
import android.view.View

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import me.tysheng.xishi.R

/**
 * Created by Sty
 * Date: 16/9/18 09:18.
 */
abstract class BaseLoadMoreRecyclerViewAdapter<T>(layoutResId: Int, data: List<T>) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data) {

    override fun createBaseViewHolder(view: View): BaseViewHolder {
        return BaseViewHolder(view)
    }

    fun onEmptyView() {
        val v = LayoutInflater.from(recyclerView.context).inflate(R.layout.item_loading_more, recyclerView, false)
        emptyView = v
    }

    fun onEnd() {
        loadMoreEnd()
    }

    fun onError() {
        loadMoreFail()
    }


}
