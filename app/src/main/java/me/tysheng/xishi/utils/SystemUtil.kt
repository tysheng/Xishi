package me.tysheng.xishi.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.TextUtils

import java.io.File
import java.io.IOException

import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.BuildConfig


/**
 * Created by Sty
 * Date: 16/8/19 09:36.
 */
object SystemUtil {
    val MB: Long = 1048576 // 1024 * 1024

    /**
     * @return 获取App版本号
     * @throws Exception
     */
    val versionName: String
        get() = BuildConfig.VERSION_NAME

    val versionCode: Int
        get() = BuildConfig.VERSION_CODE

    /**
     * 是否可以上网
     *
     * @return
     */
    val isNetworkOnline: Boolean
        get() {
            val runtime = Runtime.getRuntime()
            try {
                val ipProcess = runtime.exec("ping -c 1 114.114.114.114")
                val exitValue = ipProcess.waitFor()
                return exitValue == 0
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            return false
        }

    fun share(context: Context, text: String, title: String, path: Uri) {
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

    fun deleteFile(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return true
        }

        val file = File(path)
        if (!file.exists()) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return file.delete()
    }

    fun sendEmail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "353491983@qq.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "关于西施的问题反馈")
        //        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        //        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"353491983@qq.com"});
        context.startActivity(Intent.createChooser(emailIntent, "发送邮件..."))
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param context     承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    fun shareAppShop(context: Context, packageName: String) {
        try {
            //            Uri uri = Uri.parse("market://details?id=" + packageName);
            //            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            //跳转酷市场
            //            intent.setClassName("com.coolapk.market", "com.coolapk.market.activity.AppViewActivity");
            //            context.startActivity(intent);
            //从其他浏览器打开
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val content_url = Uri.parse("http://www.coolapk.com/apk/me.tysheng.xishi")
            intent.data = content_url
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"))
        } catch (e: Exception) {

        }

    }

    /**
     * 文件大小获取
     *
     * @param file File对象
     * @return 文件大小字符串
     */
    fun getFileSize(file: File): String {
        val length = getDirSize(file)
        return if (length / MB < 1) "0M" else (length / MB).toString() + "M"
    }

    /**
     * 遍历文件大小
     *
     * @param dir file
     * @return file size
     */
    private fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files) {
            if (file.isFile) {
                dirSize += file.length()
            } else if (file.isDirectory) {
                dirSize += file.length()
                dirSize += getDirSize(file)
            }
        }
        return dirSize
    }

    fun clearCache(context: Context) {
        Flowable.just(context)
                .map { context -> SystemUtil.deleteFile(context.cacheDir.path) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}
