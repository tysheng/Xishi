package me.tysheng.xishi.ui

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by tysheng
 * Date: 31/3/18 22:47.
 * Email: tyshengsx@gmail.com
 */
interface BasePresenter {

}

interface BaseView {
    fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T>
    fun <T> bindUntilDestroy() = bindUntilEvent<T>(ActivityEvent.DESTROY)
}