package me.tysheng.xishi.net

import io.reactivex.Observable
import me.tysheng.xishi.bean.DayAlbums
import me.tysheng.xishi.bean.Mains
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Sty
 * Date: 16/8/22 22:18.
 */
interface XishiService {


    @GET("mains/p{page}.html")
    fun getMains(@Path("page") page: Int): Observable<Mains>

    @GET("albums/a{id}.html")
    fun getDayAlbums(@Path("id") id: Int): Observable<DayAlbums>

}
