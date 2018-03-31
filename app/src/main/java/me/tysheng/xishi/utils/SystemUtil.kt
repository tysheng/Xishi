package me.tysheng.xishi.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import me.tysheng.xishi.R
import timber.log.Timber


/**
 * Created by Sty
 * Date: 16/8/19 09:36.
 */
object SystemUtil {

    fun shareVia(context: Context, text: String, title: String, path: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_STREAM, path)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(Intent.createChooser(intent, title))
    }

    /**
     * 检测网络是否连接,不管是否可以上网
     *
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isAvailable
    }

    fun sendEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.getString(R.string.email_address), null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_about))
        context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_email)))
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param context     承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    fun shareAppShop(context: Context, packageName: String) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val uri = Uri.parse("http://www.coolapk.com/apk/$packageName")
            intent.data = uri
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_broswer)))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
