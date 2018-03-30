package me.tysheng.xishi.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.View
import android.view.ViewGroup

import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * Created by tysheng
 * Date: 2017/5/11 14:39.
 * Email: tyshengsx@gmail.com
 */

abstract class BaseDataBindingAdapter<T, Binding : ViewDataBinding> : BaseQuickAdapter<T, TyBindingViewHolder<Binding>> {


    constructor(@LayoutRes layoutResId: Int, data: List<T>?) : super(layoutResId, data) {}

    constructor(data: List<T>?) : super(data) {}

    constructor(@LayoutRes layoutResId: Int) : super(layoutResId) {}

    override fun createBaseViewHolder(view: View): TyBindingViewHolder<Binding> {
        return TyBindingViewHolder(view)
    }

    override fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): TyBindingViewHolder<Binding> {
        val binding = DataBindingUtil.inflate<Binding>(mLayoutInflater, layoutResId, parent, false)
        val view: View
        if (binding == null) {
            view = getItemView(layoutResId, parent)
        } else {
            view = binding.root
        }
        val holder = TyBindingViewHolder<Binding>(view)
        holder.binding = binding
        return holder
    }

    override fun convert(helper: TyBindingViewHolder<Binding>, item: T) {
        convert(helper.binding, item)
        helper.binding!!.executePendingBindings()
    }

    fun onEnd() {
        loadMoreEnd()
    }

    fun onError() {
        loadMoreFail()
    }

    protected abstract fun convert(binding: Binding?, item: T)
}
