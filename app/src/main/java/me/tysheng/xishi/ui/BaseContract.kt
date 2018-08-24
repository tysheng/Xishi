package me.tysheng.xishi.ui

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by tysheng
 * Date: 31/3/18 22:47.
 * Email: tyshengsx@gmail.com
 */
interface BasePresenter<T> {
    var view: T
}

interface BaseView<out T : BasePresenter<*>> {

    val presenter: T

}

abstract class AbstractPresenter<V : BaseView<P>, out P : BasePresenter<V>> : BasePresenter<V> {

    private val subscription: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    protected fun addToSubscription(disposable: Disposable) {
        subscription.add(disposable)
    }

    fun onDestroy() {
        subscription.clear()
    }
}

