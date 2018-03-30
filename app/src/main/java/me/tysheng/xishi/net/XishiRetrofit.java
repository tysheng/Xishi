package me.tysheng.xishi.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.tysheng.xishi.utils.SystemUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class XishiRetrofit {

    private static String BASE_URL = "http://dili.bdatu.com/jiekou/";
    private Context mContext;
    private volatile Retrofit retrofit = null;
    private volatile XishiService sService = null;
    private Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            CacheControl cacheControl = SystemUtil.isNetworkAvailable(mContext) ?
                    CacheControl.FORCE_NETWORK : CacheControl.FORCE_CACHE;
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
            Response originalResponse = chain.proceed(request);
            return originalResponse.newBuilder()
                    .build();
        }
    };

    public XishiRetrofit(Context context) {
        mContext = context;
    }

    private void init() {
        final File baseDir = mContext.getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpCache");
            //设置缓存 10M
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        final int TIME_MAX = 12;
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //网络拦截
        builder.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        //缓存设置
        builder.cache(cache);

        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    public XishiService get() {
        init();
        sService = retrofit.create(XishiService.class);
        return sService;
    }

}

