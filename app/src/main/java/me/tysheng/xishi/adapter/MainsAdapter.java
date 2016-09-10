package me.tysheng.xishi.adapter;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Album;
import me.tysheng.xishi.utils.ImageLoadHelper;

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
public class MainsAdapter extends BaseQuickAdapter<Album> {
    public MainsAdapter() {
        super(R.layout.item_mains, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, Album album) {
        holder.setText(R.id.title, titleConvert(album.title))
                .setText(R.id.time, timeConvert(album.addtime.substring(0,10)))
                .addOnClickListener(R.id.imageView);
        ImageLoadHelper.get()
                .load(album.url)
                .into((ImageView) holder.getView(R.id.imageView));
    }
    RelativeSizeSpan span1 = new RelativeSizeSpan(1.4f);
    SpannableString timeConvert(String s) {
        int month = Integer.valueOf(s.substring(5, 7));
        int day = Integer.valueOf(s.substring(8));
        SpannableString string = new SpannableString(day + "/" + month);
        string.setSpan(span1,0,String.valueOf(day).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return string;
    }
    String titleConvert(String s){
        if (s.startsWith("20"))
            return s.substring(10).trim();
        else
            return s;
    }
}
