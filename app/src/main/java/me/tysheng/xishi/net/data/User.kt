package me.tysheng.xishi.net.data

/**
 * Created by tysheng
 * Date: 21/9/18 6:29 PM.
 * Email: tyshengsx@gmail.com
 */
data class User(
        val userId: Int = 0,
        val userName: String,
        var password: String? = null,
        val avatar: String? = null,
        val albums: List<ServerAlbum>? = null,
        val shots: List<Shot>? = null,
        var token: String? = null
)