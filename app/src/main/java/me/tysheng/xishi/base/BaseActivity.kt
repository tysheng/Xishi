package me.tysheng.xishi.base

import android.os.Bundle
import android.support.v4.app.Fragment
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.tysheng.xishi.App
import me.tysheng.xishi.dagger.component.ActivityComponent
import me.tysheng.xishi.dagger.component.ApplicationComponent
import me.tysheng.xishi.dagger.component.DaggerActivityComponent
import me.tysheng.xishi.dagger.module.ActivityModule

/**
 * Created by shengtianyang on 16/7/11.
 */
abstract class BaseActivity : RxAppCompatActivity() {

    protected val applicationComponent: ApplicationComponent?
        get() = (application as App).applicationComponent

    protected val activityModule: ActivityModule
        get() = ActivityModule(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onPause() {
        super.onPause()
//        StatService.onPause(this)
    }

    override fun onResume() {
        super.onResume()
//        StatService.onResume(this)
    }

    protected fun injectAppComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(activityModule)
                .build()
    }

    /**
     * Fragment之间的切换
     *
     * @param from 当前
     * @param to   目标
     * @param id
     * @param tag
     */
    protected fun jumpFragment(from: Fragment?, to: Fragment?, id: Int, tag: String) {
        val manager = supportFragmentManager
        if (to == null) {
            return
        }
        val transaction = manager.beginTransaction()
        if (from == null) {
            transaction.add(id, to, tag)
        } else {
            transaction.hide(from)
            if (to.isAdded) {
                transaction.show(to)
            } else {
                transaction.add(id, to, tag)
            }
        }
        transaction
                .setCustomAnimations(0, 0, android.R.anim.fade_in, android.R.anim.fade_out)
                .commitAllowingStateLoss()

    }

}
