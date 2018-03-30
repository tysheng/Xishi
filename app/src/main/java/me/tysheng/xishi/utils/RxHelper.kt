package me.tysheng.xishi.utils

import io.reactivex.ObservableTransformer
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
    fun <T> ioToMain(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
