package me.tysheng.xishi.net;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.tysheng.xishi.App;
import me.tysheng.xishi.utils.LogUtil;
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

    private static final int TIME_MAX = 12;
    private static volatile Retrofit retrofit = null;
    private static volatile XishiService sService = null;
    private static String BASE_URL = "http://dili.bdatu.com/jiekou/";

    private static void init() {
        final File baseDir = App.get().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpCache");
            //设置缓存 10M
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }

        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(2, TimeUnit.SECONDS);//表示当访问此网页后的max-age秒内再次访问不会去服务器请求
        cacheBuilder.maxStale(1, TimeUnit.DAYS);//允许读取过期时间必须小于max-stale 值的缓存对象

        final CacheControl cacheControl = cacheBuilder.build();

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                LogUtil.d(request.toString());
                Response response = chain.proceed(request);
                LogUtil.d(response.cacheControl().toString());
                return response;
            }
        };


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //网络拦截
        builder.addInterceptor(interceptor);
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

