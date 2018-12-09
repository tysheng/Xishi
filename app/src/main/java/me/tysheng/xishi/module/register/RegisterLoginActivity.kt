package me.tysheng.xishi.module.register

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import me.tysheng.xishi.module.BaseActivity

/**
 * Created by tysheng
 * Date: 26/9/18 5:17 PM.
 * Email: tyshengsx@gmail.com
 */
class RegisterLoginActivity : BaseActivity() {
    private val frameLayoutId by lazy { View.generateViewId() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val frameLayout = FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            id = frameLayoutId
        }
        setContentView(frameLayout)

        supportFragmentManager.beginTransaction()
                .add(frameLayoutId, RegisterFragment())
                .commitAllowingStateLoss()
    }

}