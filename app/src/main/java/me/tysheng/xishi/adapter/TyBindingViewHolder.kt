package me.tysheng.xishi.adapter

import android.databinding.ViewDataBinding
import android.view.View

import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by tysheng
 * Date: 2017/5/11 14:42.
 * Email: tyshengsx@gmail.com
 */

class TyBindingViewHolder<Binding : ViewDataBinding>(view: View) : BaseViewHolder(view) {
    var binding: Binding? = null
}
