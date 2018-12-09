package me.tysheng.xishi.utils

import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import me.tysheng.xishi.module.album.AlbumActivity
import me.tysheng.xishi.module.register.RegisterLoginActivity

/**
 * Created by tysheng
 * Date: 27/9/18 10:15 AM.
 * Email: tyshengsx@gmail.com
 */
object Navigator {
    fun openAlbum(context: Context, id: Int){
        val intent = Intent(context, AlbumActivity::class.java)
        intent.putExtra(AlbumActivity.KEY_ALBUMS, id)
        ActivityCompat.startActivity(context, intent, null)
    }

    fun openRegisterLogin(context: Context){
        context.startActivity(Intent(context,RegisterLoginActivity::class.java))
    }
}