package me.tysheng.xishi.net

import io.reactivex.Observable
import me.tysheng.xishi.net.data.*
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by tysheng
 * Date: 21/9/18 6:21 PM.
 * Email: tyshengsx@gmail.com
 */
interface XishiService {

    @POST("albums/add")
    fun addAlbum(@Body album: ServerAlbum): Observable<CommonResponse<Any>>

    @POST("shots/add")
    fun addShot(@Body shot: Shot): Observable<CommonResponse<Any>>

    @POST("users/register")
    fun register(@Body registerParam: RegisterParam): Observable<CommonResponse<User>>

    companion object {
        const val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjF9.KVG-MUZhzYWX0ycNNlu4t_jj77uWyB5PbpmB9StRZ3Q"
    }
}