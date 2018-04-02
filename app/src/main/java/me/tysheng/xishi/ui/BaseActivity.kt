package me.tysheng.xishi.ui

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.tysheng.xishi.App
import me.tysheng.xishi.di.component.ApplicationComponent

/**
 * Created by shengtianyang on 16/7/11.
 */
abstract class BaseActivity : RxAppCompatActivity() {

    protected val applicationComponent: ApplicationComponent?
        get() = (application as App).applicationComponent

//    protected val activityModule: ActivityModule
//        get() = ActivityModule(this)
//
//    protected fun injectAppComponent(): ActivityComponent {
//        return DaggerActivityComponent.builder()
//                .applicationComponent(applicationComponent)
//                .activityModule(activityModule)
//                .build()
//    }

}
