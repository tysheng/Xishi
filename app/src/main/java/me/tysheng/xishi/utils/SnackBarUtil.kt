package me.tysheng.xishi.utils

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

/**
 * Created by Sty
 * Date: 16/9/11 13:47.
 */
object SnackBarUtil {

    @JvmOverloads
    fun show(view: View, msg: String, time: Int = Snackbar.LENGTH_SHORT) {
        val snackbar = Snackbar.make(view, msg, time)
        val viewGroup = snackbar.view as ViewGroup
        val child = viewGroup.getChildAt(0)
        if (child is SnackbarContentLayout) {
            val messageView = child.getChildAt(0)
            if (messageView is TextView) {
                messageView.setTextColor(Color.WHITE)
            }
        }
        snackbar.show()

    }
}
