package me.tysheng.xishi.utils;

import android.databinding.BindingAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;


public class BindingAdapters {

    @BindingAdapter("show")
    public static void setShow(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("hide")
    public static void setHide(View view, boolean visible) {
        setShow(view, !visible);
    }


    @BindingAdapter("invisible")
    public static void setInvisible(View view, boolean invisible) {
        view.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
    }

    @BindingAdapter("titleConvert")
    public static void titleConvert(TextView view, String title) {
        String s = title.startsWith("20") ? title.substring(10).trim() : title;
        view.setText(s);
    }

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView view, String url) {
        RequestCreator requestCreator = Picasso.with(view.getContext()).load(url);
        requestCreator.into(view);
    }

    @BindingAdapter("timeConvert")
    public static void timeConvert(TextView view, String s) {
        s = s.substring(0, 10);
        int month = Integer.valueOf(s.substring(5, 7));
        int day = Integer.valueOf(s.substring(8));
        SpannableString string = new SpannableString(day + "/" + month);
        string.setSpan(new RelativeSizeSpan(1.4f), 0, String.valueOf(day).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setText(string);
    }

}
