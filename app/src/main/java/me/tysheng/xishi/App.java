package me.tysheng.xishi;

import android.app.Application;

import me.tysheng.xishi.dagger.component.ApplicationComponent;
import me.tysheng.xishi.dagger.component.DaggerApplicationComponent;
import me.tysheng.xishi.dagger.module.ApplicationModule;


/**
 * Created by Sty
 * Date: 16/8/22 22:17.
 */
public class App extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
