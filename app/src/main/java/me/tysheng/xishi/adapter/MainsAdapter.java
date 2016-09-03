package me.tysheng.xishi.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Album;

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
public class MainsAdapter extends BaseQuickAdapter<Album> {
    public MainsAdapter(List<Album> data) {
        super(R.layout.item_mains,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Album album) {
        holder.setText(R.id.title,album.title)
        .setOnClickListener(R.id.imageView,new OnItemChildClickListener());
        Picasso.with(mContext)
                .load(album.url)
                .into((ImageView) holder.getView(R.id.imageView));
    }
}
