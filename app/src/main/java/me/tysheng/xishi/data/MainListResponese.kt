package me.tysheng.xishi.data

import com.google.gson.annotations.SerializedName

data class MainListResponese(
        @SerializedName("total") val total: String?, // 1465
        @SerializedName("page") val page: String?, // 1
        @SerializedName("pagecount") val pagecount: String?, // 15
        @SerializedName("album") val album: List<Album>?
)