package me.tysheng.xishi.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Sty
 * Date: 16/8/22 22:11.
 */
data class Album(
        @SerializedName("id") val id: String?, // 1987
        @SerializedName("title") val title: String?, // 2018-06-02 姣忔棩绮鹃€�
        @SerializedName("url") val url: String?, // http://pic01.bdatu.com/Upload/appsimg/1527219151.jpg
        @SerializedName("addtime") val addtime: String?, // 2018-06-02 00:04:00
        @SerializedName("adshow") val adshow: String?, // NO
        @SerializedName("fabu") val fabu: String?, // YES
        @SerializedName("encoded") val encoded: String?, // 1
        @SerializedName("amd5") val amd5: String?, // 2af9f0a1ace95cf5218ba884dbb40071
        @SerializedName("sort") val sort: String?, // 1987
        @SerializedName("ds") val ds: String?, // 1
        @SerializedName("timing") val timing: String?, // 1
        @SerializedName("timingpublish") val timingpublish: String? // 2018-06-02 00:00:00
)
