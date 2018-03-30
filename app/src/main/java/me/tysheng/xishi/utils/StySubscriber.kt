package me.tysheng.xishi.utils

import io.reactivex.subscribers.ResourceSubscriber

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
abstract class StySubscriber<T> : ResourceSubscriber<T>() {

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {

    }

    override fun onNext(t: T) {
        next(t)
    }

    abstract fun next(t: T)
}
