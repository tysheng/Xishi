package me.tysheng.xishi.bean

/**
 * Created by Sty
 * Date: 16/8/22 21:58.
 */
data class Mains(

        /**
         * total : 791
         * page : 1
         * pagecount : 15
         */

        var total: String? = null,
        var page: String? = null,
        var pagecount: String? = null,
        /**
         * id : 1321
         * title : 2016-08-22每日精选
         * url : http://pic01.bdatu.com/Upload/appsimg/1471255495.jpg
         * addtime : 2016-08-22 00:04:00
         * adshow : NO
         * fabu : YES
         * encoded : 1
         * amd5 : b4f33b606d2e91dbe70623468c08076f
         * sort : 1507
         * ds : 1
         * timing : 1
         * timingpublish : 2016-08-22 00:00:00
         */

        var album: List<Album> = arrayListOf()
)