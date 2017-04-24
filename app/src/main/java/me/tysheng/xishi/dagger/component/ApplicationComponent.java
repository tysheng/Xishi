package me.tysheng.xishi.dagger.component;

import android.content.Context;
import android.support.v4.app.Fragment;

import javax.inject.Singleton;

import dagger.Component;
import me.tysheng.xishi.App;
import me.tysheng.xishi.base.BaseActivity;
import me.tysheng.xishi.dagger.module.ApplicationModule;
import me.tysheng.xishi.net.XishiService;
import me.tysheng.xishi.utils.ImageLoadHelper;

/**
 * Created by tysheng
 * Date: 2017/4/22 17:38.
 * Email: tyshengsx@gmail.com
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Context getApp();

    ImageLoadHelper getImageLoadHelper();

    XishiService getService();

    void inject(BaseActivity activity);

    void inject(App app);

    void inject(Fragment fragment);
}
