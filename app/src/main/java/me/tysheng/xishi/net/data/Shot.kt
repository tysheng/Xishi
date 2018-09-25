package me.tysheng.xishi.net.data

/**
 * Created by tysheng
 * Date: 21/9/18 6:26 PM.
 * Email: tyshengsx@gmail.com
 */
data class Shot(
        val shotId: Int,
        val albumId: Int,
        val title: String,
        val content: String,
        val author: String,
        val url: String,
        val youShotLink: String? = null,
        val imageSize: Long,
        val addTime: Long,
        val thumb: String? = null
)