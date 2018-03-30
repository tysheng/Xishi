package me.tysheng.xishi.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.tysheng.xishi.BuildConfig;


/**
 * Created by Sty
 * Date: 16/8/19 09:36.
 */
public class SystemUtil {
    public static final long MB = 1048576; // 1024 * 1024

    public static void share(Context context, String text, String title, Uri path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * @return 获取App版本号
     * @throws Exception
     */
    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getVersionCode() {

        return BuildConfig.VERSION_CODE;
    }

    /**
     * 检测网络是否连接,不管是否可以上网
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 是否可以上网
     *
     * @return
     */
    public static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 114.114.114.114");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static void sendEmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "353491983@qq.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "关于西施的问题反馈");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"353491983@qq.com"});
        context.startActivity(Intent.createChooser(emailIntent, "发送邮件..."));
    }

    /**
     * 根据应用包名，跳转到应用市场
     *
     * @param context     承载跳转的Activity
     * @param packageName 所需下载（评论）的应用包名
     */
    public static void shareAppShop(Context context, String packageName) {
        try {
//            Uri uri = Uri.parse("market://details?id=" + packageName);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //跳转酷市场
//            intent.setClassName("com.coolapk.market", "com.coolapk.market.activity.AppViewActivity");
//            context.startActivity(intent);
            //从其他浏览器打开
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse("http://www.coolapk.com/apk/me.tysheng.xishi");
            intent.setData(content_url);
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } catch (Exception e) {

        }
    }

    /**
     * 文件大小获取
     *
     * @param file File对象
     * @return 文件大小字符串
     */
    public static String getFileSize(File file) {
        long length = getDirSize(file);
        if (length / MB < 1)
            return "0M";
        return length / MB + "M";
    }

    /**
     * 遍历文件大小
     *
     * @param dir file
     * @return file size
     */
    private static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }

    public static void clearCache(Context context) {
        Flowable.just(context)
                .map(new Function<Context, Boolean>() {
                    @Override
                    public Boolean apply(Context context) throws Exception {
                        return SystemUtil.deleteFile(context.getCacheDir().getPath());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
