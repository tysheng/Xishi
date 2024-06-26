package me.tysheng.xishi.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.net.URISyntaxException

object AlipayZeroSdk {

    // 支付宝包名
    private const val ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone"

    // 旧版支付宝二维码通用 Intent Scheme Url 格式
    private const val INTENT_URL_FORMAT = "intent://platformapi/startapp?saId=10000007&" +
            "clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s" +
            "%3Dweb-other&_t=1472443966571#Intent;" +
            "scheme=alipayqr;package=com.eg.android.AlipayGphone;end"

    /**
     * 打开转账窗口
     * 旧版支付宝二维码方法，需要使用 https://fama.alipay.com/qrcode/index.htm 网站生成的二维码
     * 这个方法最好，但在 2016 年 8 月发现新用户可能无法使用
     *
     * @param context Parent Activity
     * @param urlCode 手动解析二维码获得地址中的参数，例如 https://qr.alipay.com/aehvyvf4taua18zo6e 最后那段
     * @return 是否成功调用
     */
    fun startAlipayClient(context: Context, urlCode: String): Boolean {
        return startIntentUrl(context, INTENT_URL_FORMAT.replace("{urlCode}", urlCode))
    }

    /**
     * 打开 Intent Scheme Url
     *
     * @param activity Parent Activity
     * @param intentFullUrl Intent 跳转地址
     * @return 是否成功调用
     */
    private fun startIntentUrl(context: Context, intentFullUrl: String): Boolean {
        return try {
            val intent = Intent.parseUri(
                    intentFullUrl,
                    Intent.URI_INTENT_SCHEME
            )
            context.startActivity(intent)
            true
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            false
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断支付宝客户端是否已安装，建议调用转账前检查
     * @param context Context
     * @return 支付宝客户端是否已安装
     */
    fun hasInstalledAlipayClient(context: Context): Boolean {
        val pm = context.packageManager
        return try {
            val info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0)
            info != null
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }

}
