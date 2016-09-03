package me.tysheng.xishi.net;

import me.tysheng.xishi.bean.DayAlbums;
import me.tysheng.xishi.bean.Mains;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sty
 * Date: 16/8/22 22:18.
 */
public interface XishiService {
    String BASE_URL = "http://dili.bdatu.com/jiekou/";

    @GET("mains/p{page}.html")
    Observable<Mains> getMains(@Path("page") int page);

    @GET("albums/a{id}.html")
    Observable<DayAlbums> getDayAlbums(@Path("id") int id);

}
