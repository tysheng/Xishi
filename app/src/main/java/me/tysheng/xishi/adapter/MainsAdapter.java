package me.tysheng.xishi.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;

import javax.inject.Inject;

import me.tysheng.xishi.App;
import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Album;
import me.tysheng.xishi.dagger.component.DaggerAdapterComponent;
import me.tysheng.xishi.utils.ImageLoadHelper;

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
public class MainsAdapter extends BaseLoadMoreRecyclerViewAdapter<Album> {
    @Inject
    ImageLoadHelper mHelper;
    private RelativeSizeSpan span = new RelativeSizeSpan(1.4f);

    @Inject
    public MainsAdapter(Context context) {
        super(R.layout.item_mains, null);
        DaggerAdapterComponent.builder()
                .applicationComponent(((App) context.getApplicationContext()).getApplicationComponent())
                .build().inject(this);
    }

    @Override
    protected void convert(BaseViewHolder holder, Album album) {
        holder.setText(R.id.title, titleConvert(album.title))
                .setText(R.id.time, timeConvert(album.addtime.substring(0, 10)))
                .addOnClickListener(R.id.imageView);
        mHelper
                .load(album.url)
                .into((ImageView) holder.getView(R.id.imageView));
    }

    private SpannableString timeConvert(String s) {
        int month = Integer.valueOf(s.substring(5, 7));
        int day = Integer.valueOf(s.substring(8));
        SpannableString string = new SpannableString(day + "/" + month);
        string.setSpan(span, 0, String.valueOf(day).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return string;
    }

    private String titleConvert(String s) {
        return s.startsWith("20") ? s.substring(10).trim() : s;
    }
}
