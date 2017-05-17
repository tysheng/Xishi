package me.tysheng.xishi.adapter;

import javax.inject.Inject;

import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Album;
import me.tysheng.xishi.databinding.ItemMainsBinding;

/**
 * Created by Sty
 * Date: 16/8/22 22:27.
 */
public class MainsAdapter extends BaseDataBindingAdapter<Album, ItemMainsBinding> {

    @Inject
    public MainsAdapter() {
        super(R.layout.item_mains, null);
    }

    @Override
    protected void convert(ItemMainsBinding binding, Album item) {
        binding.setItem(item);
    }
}
