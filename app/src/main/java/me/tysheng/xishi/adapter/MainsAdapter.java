package me.tysheng.xishi.adapter;

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
        super(R.layout.item_mains,null);
    }

    @Override
    protected void convert(BaseViewHolder holder, Album album) {
        holder.setText(R.id.title,album.title)
        .addOnClickListener(R.id.imageView);
        ImageLoadHelper.get()
                .load(album.url)
                .into((ImageView) holder.getView(R.id.imageView));
    }
}
