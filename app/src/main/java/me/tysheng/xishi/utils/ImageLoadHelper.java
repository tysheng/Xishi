package me.tysheng.xishi.utils;

import android.content.Context;
import android.support.annotation.IntegerRes;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by Sty
 * Date: 16/9/6 09:32.
 */
public class ImageLoadHelper {

    private String mUrl;
    private int mPlaceholder;
    private Context mContext;

    public ImageLoadHelper(Context context) {
        mContext = context;
    }

    public ImageLoadHelper placeholder(@IntegerRes int placeholder) {
        mPlaceholder = placeholder;
        return this;
    }

    public void into(ImageView target) {
        RequestCreator requestCreator = Picasso.get()
                .load(mUrl);
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder);
        requestCreator.into(target);
    }

    public void into(ImageView target, Callback callback) {
        RequestCreator requestCreator = Picasso.get()
                .load(mUrl);
        if (mPlaceholder != 0)
            requestCreator.placeholder(mPlaceholder);
        requestCreator.into(target, callback);
    }

    public ImageLoadHelper load(String url) {
        mUrl = url;
        return this;
    }
}
