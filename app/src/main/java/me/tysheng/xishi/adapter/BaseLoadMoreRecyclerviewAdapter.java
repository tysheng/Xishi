package me.tysheng.xishi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import me.tysheng.xishi.R;

/**
 * Created by Sty
 * Date: 16/9/18 09:18.
 */
public abstract class BaseLoadMoreRecyclerViewAdapter<T> extends BaseQuickAdapter<T> {
    private Context context;

    public BaseLoadMoreRecyclerViewAdapter(int layoutResId, List<T> data, Context context, int loadCount) {
        super(layoutResId, data);
        init(context, loadCount);
    }

    private void init(Context context, int loadCount) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.item_page_empty, null, false);
//        setEmptyView(v);
        this.context = context;
        openLoadMore(loadCount);
        View view = LayoutInflater.from(context).inflate(R.layout.item_loading_more, null, false);
        setLoadingView(view);
    }

    public void onEnd() {
        loadComplete();
        View view = LayoutInflater.from(context).inflate(R.layout.item_loading_error, null, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("已到末尾,无更多内容");
        addFooterView(view);
    }

    public void onError() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_loading_error, null, false);
        setLoadMoreFailedView(view);
        showLoadMoreFailedView();
    }


}
