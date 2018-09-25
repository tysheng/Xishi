package me.tysheng.xishi.net

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
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
    private lateinit var retrofit: Retrofit
    private lateinit var ngService: NgService
    private lateinit var xishiService: XishiService
    private lateinit var okHttpClient: OkHttpClient
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
    private val gson= GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setExclusionStrategies()
            .create()

    private val authInterceptor = Interceptor {
        val oldRequest = it.request()
        val builder = oldRequest.newBuilder()
        builder.header("Authorization", "Bearer " + XishiService.token)
        it.proceed(builder.build())
    }

    init {
        okHttpClient()
    }

    fun createNgService(): NgService {
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        ngService = retrofit.create<NgService>(NgService::class.java)
        return ngService

    }

    fun createXishiService(): XishiService {
        retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(XISHI_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        xishiService = retrofit.create<XishiService>(XishiService::class.java)
        return xishiService
    }

    private fun okHttpClient(): OkHttpClient? {
        val baseDir = mContext.cacheDir
        var cache: Cache? = null
        if (baseDir != null) {
            val cacheDir = File(baseDir, "HttpCache")
            cache = Cache(cacheDir, (100 * 1024 * 1024).toLong())
        }

        val builder = OkHttpClient.Builder()
        builder.readTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(TIME_MAX.toLong(), TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)
        //builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
        builder.addInterceptor(authInterceptor)
        builder.addInterceptor(HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message -> Timber.tag("OkHttp").d(message) }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        builder.cache(cache)

        okHttpClient = builder.build()
        return okHttpClient
    }

    companion object {
        private const val TIME_MAX = 12

        private const val BASE_URL = "http://dili.bdatu.com/jiekou/"
        private const val XISHI_BASE_URL = "http://132.232.188.197:8080/"
    }

}

