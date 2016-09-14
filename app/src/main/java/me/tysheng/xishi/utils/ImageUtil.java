package me.tysheng.xishi.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.tysheng.xishi.R;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Sty
 * Date: 16/8/23 21:37.
 */
public class ImageUtil {
    public static Observable<Uri> saveImageToGallery(final Context context, String url) {
        return Observable.just(url)
                .map(new Func1<String, Uri>() {
                    @Override
                    public Uri call(String s) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = Picasso.with(context).load(s).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 首先保存图片
                        File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!appDir.exists()) {
                            appDir.mkdir();
                        }
                        File file;
                        int start = s.lastIndexOf("/") + 1;
                        int end = s.lastIndexOf(".");
                        String fileName = s.substring(start, end) + ".jpg";
                        file = new File(appDir, fileName);
                        if (file.exists()) {
                            return Uri.fromFile(file);
                        }

                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            if (bitmap != null) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            }
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        Uri uri = Uri.fromFile(file);

                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                        return uri;
                    }
                });

    }

    public static void shareImage(Context context, Uri path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
//            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_title)));
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.fade_in, 0);
    }

}
