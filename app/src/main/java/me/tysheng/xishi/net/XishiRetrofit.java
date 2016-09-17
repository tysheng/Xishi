package me.tysheng.xishi.net;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.tysheng.xishi.App;
import me.tysheng.xishi.utils.SystemUtil;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class XishiRetrofit {


    private static volatile Retrofit retrofit = null;
    private static volatile XishiService sService = null;
    private static String BASE_URL = "http://dili.bdatu.com/jiekou/";
    private static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!SystemUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (SystemUtil.isNetworkAvailable()) {
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    private static void init() {
        final File baseDir = App.get().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpCache");
            //设置缓存 10M
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }

//        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
//        cacheBuilder.maxAge(2, TimeUnit.SECONDS);//表示当访问此网页后的max-age秒内再次访问不会去服务器请求
//        cacheBuilder.maxStale(1, TimeUnit.DAYS);//允许读取过期时间必须小于max-stale 值的缓存对象
//        final CacheControl cacheControl = cacheBuilder.build();
//        Interceptor interceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .cacheControl(cacheControl)
//                        .build();
//                LogUtil.d(request.toString());
//                Response response = chain.proceed(request);
//                LogUtil.d(response.cacheControl().toString());
//                return response;
//            }
//        };

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
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    public static XishiService get() {
        if (sService == null) {
            synchronized (XishiRetrofit.class) {
                if (sService == null)
                    init();
                sService = retrofit.create(XishiService.class);
            }
        }
        return sService;
    }

}

