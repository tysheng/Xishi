package me.tysheng.xishi.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.dialog_bottom_sheet.view.*
import me.tysheng.xishi.BuildConfig
import me.tysheng.xishi.R
import me.tysheng.xishi.adapter.BaseLoadMoreRecyclerViewAdapter

/**
 * Created by Sty
 * Date: 16/8/18 22:26.
 */
class EmailDialog : BottomSheetDialogFragment() {
    private var behavior: BottomSheetBehavior<*>? = null
    var dialogCallback: DialogCallback? = null
    private lateinit var customView: View
    override fun onStart() {
        super.onStart()
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        customView = requireActivity().layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        dialog.setContentView(customView)
        behavior = BottomSheetBehavior.from(customView.parent as View)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list = arrayListOf(R.drawable.ic_email_black_24dp to getString(R.string.feedback_by_email),
                R.drawable.ic_update_black_24dp to getString(R.string.check_update),
                R.drawable.ic_thumb_up_black_24dp to getString(R.string.donate),
                R.drawable.ic_content_copy_black_24dp to getString(R.string.copy_email),
                R.drawable.ic_compare_arrows_black_24dp to getString(R.string.change_theme))
        customView.title.text = ("${getString(R.string.version)} ${BuildConfig.VERSION_NAME}")
        object : BaseLoadMoreRecyclerViewAdapter<Pair<Int, String>>(R.layout.item_dialog_bottom_sheet, list) {
            override fun convert(holder: BaseViewHolder, item: Pair<Int, String>) {
                holder.setText(R.id.title, item.second)
                        .setImageResource(R.id.icon, item.first)
            }
        }.apply {
            bindToRecyclerView(customView.shareRecyclerView)
            setOnItemClickListener { _, _, position ->
                dismissAllowingStateLoss()

                dialogCallback?.itemClick(position)
            }
        }
    }

    companion object {
        const val TAG = "EmailDialog"
    }
}

interface DialogCallback {
    fun itemClick(position: Int)
}

sealed class MainDialogActionListener
class SendEmail(val activity: Activity) : MainDialogActionListener()
class ShareToStore(val activity: Activity) : MainDialogActionListener()
class JumpToAlipay(val activity: Activity) : MainDialogActionListener()
class CopyEmail(val activity: Activity) : MainDialogActionListener()
class SwitchDayNightMode(val activity: Activity) : MainDialogActionListener()