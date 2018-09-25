package me.tysheng.xishi.ext

import me.tysheng.xishi.data.Album
import me.tysheng.xishi.data.Picture
import me.tysheng.xishi.net.data.ServerAlbum
import me.tysheng.xishi.net.data.Shot
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tysheng
 * Date: 23/9/18 5:42 PM.
 * Email: tyshengsx@gmail.com
 */

//2018-06-08 10:42:06
val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
    timeZone = TimeZone.getTimeZone("GMT+8")
}

fun String.timeToMillis(): Long {
    val date = timeFormat.parse(this)
    return date.time
}

fun Album.toServerAlbum(): ServerAlbum {
    return ServerAlbum(albumId = id!!.toInt(),
            title = title!!,
            url = url!!,
            addTime = addtime!!.timeToMillis()
    )
}

fun Picture.toShot(): Shot {
    return Shot(
            shotId = id!!.toInt(),
            albumId = albumid!!.toInt(),
            title = title,
            content = content ?: "",
            author = author ?: "",
            url = url,
            youShotLink = yourshotlink ?: "",
            imageSize = size!!.toLong(),
            addTime = addtime?.timeToMillis() ?: 0,
            thumb = thumb
    )
}