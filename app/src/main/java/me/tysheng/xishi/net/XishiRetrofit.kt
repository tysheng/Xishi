package me.tysheng.xishi.net

import android.content.Context
import io.reactivex.schedulers.Schedulers
import me.tysheng.xishi.utils.SystemUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by shengtianyang on 16/3/19.
 */
class XishiRetrofit(private val mContext: Context) {
    @Volatile
    private lateinit var retrofit: Retrofit
    @Volatile
    private lateinit var sService: XishiService
    private val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
        var request = chain.request()
        val cacheControl = if (SystemUtil.isNetworkAvailable(mContext))
            CacheControl.FORCE_NETWORK
        else
            CacheControl.FORCE_CACHE
        request = request.newBuilder()
                .cacheControl(cacheControl)
                .build()
        val originalResponse = chain.proceed(request)
        originalResponse.newBuilder()
                .build()
    }

    private fun init() {
        val baseDir = mContext.cacheDir
        var cache: Cache? = null
        if (baseDir != null) {
            val cacheDir = File(baseDir, "HttpCache")
            //设置缓存 10M
            cache = Cache(cacheDir, (100 * 1024 * 1024).toLong())
        }

        val builder = OkHttpClient.Builder()
        //设置超时
        val TIME_MAX = 12
        builder.readTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        //错误重连
        builder.retryOnConnectionFailure(true)
        //网络拦截
        builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
        builder.addInterceptor(HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message -> Timber.tag("OkHttp").d(message) }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        //缓存设置
        builder.cache(cache)

        val client = builder.build()

        retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

    }

    fun get(): XishiService {
        init()
        sService = retrofit.create<XishiService>(XishiService::class.java)
        return sService
    }

    companion object {
        private const val BASE_URL = "http://dili.bdatu.com/jiekou/"
    }

}

