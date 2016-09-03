package me.tysheng.xishi.net;

import java.io.File;
import java.util.concurrent.TimeUnit;

import me.tysheng.xishi.App;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shengtianyang on 16/3/19.
 */
public class XishiRetrofit {

    private static Retrofit retrofit = null;
    private static XishiService sService = null;
    private static final int TIME_MAX = 6;

    private static void init() {
        final File baseDir = App.getInstance().getCacheDir();
        Cache cache = null;
        if (baseDir != null) {
            final File cacheDir = new File(baseDir, "HttpResponseCache");
            //设置缓存 10M
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.connectTimeout(TIME_MAX, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_MAX, TimeUnit.SECONDS);
        OkHttpClient client = builder.cache(cache).build();

        retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(XishiService.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    public static XishiService getQuanziApi() {
        if (sService == null) {
            synchronized (XishiRetrofit.class){
                if (sService ==null)
                    init();
                    sService = retrofit.create(XishiService.class);
            }
        }
        return sService;
    }

}

