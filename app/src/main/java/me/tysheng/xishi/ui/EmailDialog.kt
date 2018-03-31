package me.tysheng.xishi.ui

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import me.tysheng.xishi.BuildConfig
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.BaseLoadMoreRecyclerViewAdapter
import me.tysheng.xishi.databinding.DialogBottomSheetBinding
import java.util.*

/**
 * Created by Sty
 * Date: 16/8/18 22:26.
 */
class EmailDialog : BottomSheetDialogFragment() {
    private var mBehavior: BottomSheetBehavior<*>? = null
    var dialogCallback: DialogCallback? = null
    override fun onStart() {
        super.onStart()
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val binding = DataBindingUtil.inflate<DialogBottomSheetBinding>(activity!!.layoutInflater, R.layout.dialog_bottom_sheet, null, false)
        val list = ArrayList<Bean>()
        list.add(Bean(R.drawable.ic_email_black_24dp, getString(R.string.feedback_by_email)))
        list.add(Bean(R.drawable.ic_update_black_24dp, getString(R.string.check_update)))
        list.add(Bean(R.drawable.ic_thumb_up_black_24dp, getString(R.string.donate)))
        list.add(Bean(R.drawable.ic_content_copy_black_24dp, getString(R.string.copy_email)))
        list.add(Bean(R.drawable.ic_compare_arrows_black_24dp, getString(R.string.change_theme)))

        binding.title.text = ("${getString(R.string.version)} ${BuildConfig.VERSION_NAME}")
        binding.shareRecyclerView.setHasFixedSize(true)
        val adapter = object : BaseLoadMoreRecyclerViewAdapter<Bean>(R.layout.item_dialog_bottom_sheet, list) {
            override fun convert(holder: BaseViewHolder, item: Bean) {
                holder.setText(R.id.title, item.name)
                        .setImageResource(R.id.icon, item.res)
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            dialogCallback?.itemClick(position)
            dismiss()
        }
        adapter.bindToRecyclerView(binding.shareRecyclerView)
        dialog.setContentView(binding.root)
        mBehavior = BottomSheetBehavior.from(binding.root.parent as View)
        return dialog
    }

    private class Bean internal constructor(internal var res: Int, internal var name: String)
}

interface DialogCallback {
    fun itemClick(position: Int)
}