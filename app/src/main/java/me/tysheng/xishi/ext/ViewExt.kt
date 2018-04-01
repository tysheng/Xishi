package me.tysheng.xishi.ext

import android.view.View

/**
 * Created by tysheng
 * Date: 1/4/18 12:41.
 * Email: tyshengsx@gmail.com
 */
fun View.toggleInVisible() {
    if (this.visibility == View.VISIBLE) {
        this.visibility = View.INVISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}