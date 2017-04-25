package me.tysheng.xishi.adapter;

import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.tysheng.xishi.R;

/**
 * Created by Sty
 * Date: 16/9/18 09:18.
 */
public abstract class BaseLoadMoreRecyclerViewAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseLoadMoreRecyclerViewAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(View view) {
        return new BaseViewHolder(view);
    }

    public void onEmptyView() {
        View v = LayoutInflater.from(getRecyclerView().getContext()).inflate(R.layout.item_loading_more, getRecyclerView(), false);
        setEmptyView(v);
    }

    public void onEnd() {
        loadMoreEnd();
    }

    public void onError() {
        loadMoreFail();
    }


}
