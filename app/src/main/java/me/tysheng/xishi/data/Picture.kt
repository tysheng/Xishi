package me.tysheng.xishi.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Sty
 * Date: 16/8/22 22:12.
 */
data class Picture(
        @SerializedName("id") val id: String?, // 18650
        @SerializedName("albumid") val albumid: String?, // 2001
        @SerializedName("title") val title: String, // 鐧藉瓟闆€
        @SerializedName("content") val content: String?, // 鐧藉瓟闆€鈥斺€旂湡缇庯紒
        @SerializedName("url") val url: String, // http://pic01.bdatu.com/Upload/picimg/1528425723.jpg
        @SerializedName("size") val size: String?, // 247172
        @SerializedName("addtime") val addtime: String?, // 2018-06-08 10:42:06
        @SerializedName("author") val author: String?, // zdenka jan谩skov谩
        @SerializedName("thumb") val thumb: String?, // http://pic01.bdatu.com/Upload/picimg/1528425723.jpg
        @SerializedName("encoded") val encoded: String?, // 1
        @SerializedName("weburl") val weburl: String?, // http://
        @SerializedName("type") val type: String?, // pic
        @SerializedName("yourshotlink") val yourshotlink: String?, // http://yourshot.nationalgeographic.com/photos/11619942/
        @SerializedName("copyright") val copyright: String?,
        @SerializedName("pmd5") val pmd5: String?, // 32257dcab680a4b37d500fd8cad26f90
        @SerializedName("sort") val sort: String? // 18650
)
