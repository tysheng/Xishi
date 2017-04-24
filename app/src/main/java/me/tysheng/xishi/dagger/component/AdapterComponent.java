package me.tysheng.xishi.dagger.component;

import dagger.Component;
import me.tysheng.xishi.adapter.AlbumAdapter;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.dagger.module.PerActivity;

/**
 * Created by sll on 2016/3/8.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface AdapterComponent {

    void inject(MainsAdapter adapter);


    void inject(AlbumAdapter albumAdapter);

}

