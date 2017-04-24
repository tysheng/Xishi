package me.tysheng.xishi.dagger.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.tysheng.xishi.net.XishiRetrofit;
import me.tysheng.xishi.net.XishiService;
import me.tysheng.xishi.utils.ImageLoadHelper;

/**
 * Created by tysheng
 * Date: 2017/4/22 17:32.
 * Email: tyshengsx@gmail.com
 */
@Module
public class ApplicationModule {
    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context.getApplicationContext();
    }

    @Provides
    @Singleton
    public ImageLoadHelper provideImageLoadHelper(Context context) {
        return new ImageLoadHelper(context);
    }

    @Provides
    @Singleton
    public XishiService provideXishiService(Context context) {
        return new XishiRetrofit(context).get();
    }

}
