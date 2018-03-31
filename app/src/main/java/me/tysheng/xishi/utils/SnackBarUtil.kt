package me.tysheng.xishi.utils

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Sty
 * Date: 16/9/11 13:47.
 */
object SnackBarUtil {

    @JvmOverloads
    fun show(view: View, msg: String, time: Int = Snackbar.LENGTH_SHORT) {
        val snackbar = Snackbar.make(view, msg, time)
        val viewGroup = snackbar.view as ViewGroup
        for (i in 0 until viewGroup.childCount) {
            val v = viewGroup.getChildAt(i)
            (v as? TextView)?.setTextColor(Color.WHITE)
        }
        snackbar.show()

    }
}
