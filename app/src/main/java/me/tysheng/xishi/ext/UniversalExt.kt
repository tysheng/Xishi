package me.tysheng.xishi.ext

import android.content.res.Resources
import android.widget.Toast
import me.tysheng.xishi.App

/**
 * Created by tysheng
 * Date: 31/3/18 11:50.
 * Email: tyshengsx@gmail.com
 */

internal fun dp2PxInternal(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun Float.dp2Px() = dp2PxInternal(this)

fun Int.dp2Px() = dp2PxInternal(this.toFloat())

fun String.toast() {
    Toast.makeText(App.instance, this, Toast.LENGTH_SHORT).show()
}