package me.tysheng.xishi.utils;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Sty
 * Date: 16/9/16 09:09.
 */
public abstract class StySubscriber<T> extends ResourceSubscriber<T> {

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        next(t);
    }

    public abstract void next(T t);
}
