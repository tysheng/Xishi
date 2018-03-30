package me.tysheng.xishi.utils

import org.reactivestreams.Publisher

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Sty
 * Date: 16/9/16 09:04.
 */
object RxHelper {
    /**
     * io to main
     *
     * @param <T>
     * @return
    </T> */
    fun <T> ioToMain(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
