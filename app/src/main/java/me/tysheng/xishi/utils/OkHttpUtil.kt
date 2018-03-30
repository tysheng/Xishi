package me.tysheng.xishi.utils

import java.io.IOException

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

/**
 * Created by Sty
 * Date: 16/9/7 21:30.
 */
object OkHttpUtil {
    @Volatile
    private var sClient: OkHttpClient? = null

    @Synchronized
    operator fun get(url: String): String? {
        if (sClient == null)
            sClient = OkHttpClient()
        //创建一个Request

        val request = Request.Builder()
                .url(url)
                .build()

        val response: Response
        var s: String? = null
        try {
            response = sClient!!.newCall(request).execute()
            s = response.body()!!.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return s
    }

    @Synchronized
    fun post(url: String, json: String): String? {
        if (sClient == null)
            sClient = OkHttpClient()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val response: Response
        var s: String? = null
        try {
            response = sClient!!.newCall(request).execute()
            s = response.body()!!.string()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return s
    }
}
