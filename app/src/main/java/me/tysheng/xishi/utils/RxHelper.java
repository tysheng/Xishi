package me.tysheng.xishi.utils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Sty
 * Date: 16/9/16 09:04.
 */
public class RxHelper {
    /**
     * io to main
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> ioToMain() {
        return new FlowableTransformer<T, T>() {

            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
