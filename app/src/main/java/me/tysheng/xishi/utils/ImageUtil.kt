package me.tysheng.xishi.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.functions.Function
import me.tysheng.xishi.R
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Sty
 * Date: 16/8/23 21:37.
 */
object ImageUtil {

    val saveDir: File
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    fun saveImageToGallery(context: Context, url: String): Observable<Uri> {
        return Observable.just(url)
                .map(Function { s ->
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = Picasso.get().load(s).get()
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // 首先保存图片
                    val appDir = saveDir
                    if (!appDir.exists()) {
                        appDir.mkdir()
                    }
                    val file: File
                    val start = s.lastIndexOf("/") + 1
                    val end = s.lastIndexOf(".")
                    val fileName = s.substring(start, end) + ".jpg"
                    file = File(appDir, fileName)
                    if (file.exists()) {
                        return@Function Uri.fromFile(file)
                    }

                    try {
                        val fos = FileOutputStream(file)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        fos.flush()
                        fos.close()
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                    }

                    // 最后通知图库更新
                    var uri = Uri.fromFile(file)
                    MediaScannerConnection.scanFile(
                            context,
                            arrayOf(file.absolutePath),
                            null
                    ) { path, uri -> Timber.d("file $path was scanned successfully: $uri") }
                    uri
                })

    }

    fun shareImage2Wechat(context: Context, path: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, path)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.component = ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI")
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_title)))
    }

}
