package me.tysheng.xishi.utils

import io.reactivex.observers.ResourceObserver
import timber.log.Timber

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
abstract class TySubscriber<T> : ResourceObserver<T>() {

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        Timber.e(e)
    }

    override fun onNext(t: T) {
        next(t)
    }

    abstract fun next(t: T)
}
