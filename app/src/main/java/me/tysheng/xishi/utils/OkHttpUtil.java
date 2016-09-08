package me.tysheng.xishi.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Sty
 * Date: 16/9/7 21:30.
 */
public class OkHttpUtil {
    private static volatile OkHttpClient sClient;

    public static synchronized String get(String url) {
        if (sClient == null)
            sClient = new OkHttpClient();
        //创建一个Request

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        String s = null;
        try {
            response = sClient.newCall(request).execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
    public static synchronized String post(String url, String json){
        if (sClient == null)
            sClient = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response;
        String s = null;
        try {
            response = sClient.newCall(request).execute();
            s = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
