package me.tysheng.xishi.net;

import java.io.File;
import java.util.concurrent.TimeUnit;

import me.tysheng.xishi.App;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class XishiRetrofit {

    private static Retrofit retrofit = null;
    private static XishiService sService = null;
    private static final int TIME_MAX = 6;
    private static String BASE_URL = "http://dili.bdatu.com/jiekou/";

    private static void init() {
        final File baseDir = App.get().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            //设置缓存 10M
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }

//        Interceptor interceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                if (!SystemUtil.isNetworkAvailable()) {//如果网络不可用或者设置只用缓存
//                    request = request.newBuilder()
//                            .cacheControl(CacheControl.FORCE_CACHE)
//                            .build();
////                    Log.d("OkHttp", "网络不可用请求拦截");
//                }
////                else if(SystemUtil.isNetworkAvailable()&&!isUseCache){//网络可用
////                    request = request.newBuilder()
////                            .cacheControl(CacheControl.FORCE_NETWORK)
////                            .build();
//////                    Log.d("OkHttp", "网络可用请求拦截");
////                }
//                Response response = chain.proceed(request);
//                return response;
//            }
//        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        //网络拦截 没有网络用本地缓存
//        builder.addInterceptor(interceptor);

        OkHttpClient client = builder.cache(cache).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .baseUrl(BASE_URL)
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

