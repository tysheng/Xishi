package me.tysheng.xishi.utils;

import android.support.annotation.IntegerRes;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import me.tysheng.xishi.App;

/**
 * Created by Sty
 * Date: 16/9/6 09:32.
 */
public class ImageLoadHelper {

    private String mUrl;
    private int mPlaceholder;

    public static ImageLoadHelper get(){
        return new ImageLoadHelper();
    }

    public ImageLoadHelper placeholder(@IntegerRes int placeholder) {
        mPlaceholder = placeholder;
        return this;
    }

    public void into(ImageView target) {
        RequestCreator requestCreator = Picasso.with(App.get())
                .load(mUrl);
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder);
        requestCreator.into(target);
    }
    public void into(ImageView target, Callback callback) {
        RequestCreator requestCreator = Picasso.with(App.get())
                .load(mUrl);
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder);
        requestCreator.into(target,callback);
    }

    public ImageLoadHelper load(String url) {
        mUrl = url;
        return this;
    }
}
