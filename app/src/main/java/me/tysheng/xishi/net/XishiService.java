package me.tysheng.xishi.net;

import io.reactivex.Flowable;
import me.tysheng.xishi.bean.DayAlbums;
import me.tysheng.xishi.bean.Mains;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sty
 * Date: 16/8/22 22:18.
 */
public interface XishiService {


    @GET("mains/p{page}.html")
    Flowable<Mains> getMains(@Path("page") int page);

    @GET("albums/a{id}.html")
    Flowable<DayAlbums> getDayAlbums(@Path("id") int id);

}
