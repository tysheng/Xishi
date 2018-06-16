package me.tysheng.xishi.ui

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by tysheng
 * Date: 31/3/18 22:47.
 * Email: tyshengsx@gmail.com
 */
abstract class BasePresenter {
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

interface BaseView {
}