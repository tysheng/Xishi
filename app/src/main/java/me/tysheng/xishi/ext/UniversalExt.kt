@file:Suppress("NOTHING_TO_INLINE")

package me.tysheng.xishi.ext

import android.content.res.Resources
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import me.tysheng.xishi.App
import java.security.MessageDigest

/**
 * Created by tysheng
 * Date: 31/3/18 11:50.
 * Email: tyshengsx@gmail.com
 */
inline fun dp2PxInternal(dpValue: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

inline fun Float.dp2Px() = dp2PxInternal(this)

inline fun Int.dp2Px() = dp2PxInternal(this.toFloat())

inline fun String.toast() {
    Toast.makeText(App.instance, this, Toast.LENGTH_SHORT).show()
}

inline fun mainThreadPost(crossinline action: () -> Unit) {
    AndroidSchedulers.mainThread().scheduleDirect {
        action()
    }
}

inline fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") {
        String.format("%02x", it)
    }
}
