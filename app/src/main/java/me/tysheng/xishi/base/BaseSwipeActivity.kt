package me.tysheng.xishi.base

import android.os.Bundle
import android.view.View

import me.tysheng.xishi.utils.swipeback.SwipeBackActivityHelper

/**
 * Created by Sty
 * Date: 16/9/3 19:45.
 */
abstract class BaseSwipeActivity : BaseActivity() {
    protected var mHelper: SwipeBackActivityHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = SwipeBackActivityHelper(this)
        mHelper!!.onActivityCreate()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper!!.onPostCreate()
    }

    override fun <T : View?> findViewById(id: Int): T {
        val v = super.findViewById<View>(id)
        return (if (v == null && mHelper != null) mHelper!!.findViewById(id) else v) as T
    }

    fun setSwipeBackEnable(enable: Boolean) {
        mHelper!!.swipeBackLayout!!.setEnableGesture(enable)
    }

}
