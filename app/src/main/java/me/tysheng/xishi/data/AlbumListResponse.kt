package me.tysheng.xishi.data

import com.google.gson.annotations.SerializedName

data class AlbumListResponse(
        @SerializedName("counttotal") val countTotal: String?, // 12
        @SerializedName("picture") val picture: List<Picture>?
)