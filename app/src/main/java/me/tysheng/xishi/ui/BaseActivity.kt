package me.tysheng.xishi.ui

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.tysheng.xishi.App
import me.tysheng.xishi.di.component.ActivityComponent
import me.tysheng.xishi.di.component.ApplicationComponent
import me.tysheng.xishi.di.component.DaggerActivityComponent
import me.tysheng.xishi.di.module.ActivityModule

/**
 * Created by shengtianyang on 16/7/11.
 */
abstract class BaseActivity : RxAppCompatActivity() {

    private val applicationComponent: ApplicationComponent?
        get() = (application as App).applicationComponent

    private val activityModule: ActivityModule
        get() = ActivityModule(this)

    protected fun injectAppComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(activityModule)
                .build()
    }

}