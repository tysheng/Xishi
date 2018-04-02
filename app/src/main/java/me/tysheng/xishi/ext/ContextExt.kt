package me.tysheng.xishi.ext

import android.content.Context
import android.support.v4.content.ContextCompat

/**
 * Created by tysheng
 * Date: 31/3/18 11:37.
 * Email: tyshengsx@gmail.com
 */
fun Context.drawable(resId: Int) =
        ContextCompat.getDrawable(this, resId)!!


fun Context.color(resId: Int) =
        ContextCompat.getColor(this, resId)
