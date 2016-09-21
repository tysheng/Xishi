package me.tysheng.xishi.inter;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import java.util.ArrayList;

import me.tysheng.xishi.bean.Album;

/**
 * Created by Sty
 * Date: 16/9/21 13:18.
 */

public class DataCallback extends DiffUtil.Callback {
    private ArrayList<Album> oldList, newList;

    public DataCallback(ArrayList<Album> oldList, ArrayList<Album> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return TextUtils.equals(oldList.get(oldItemPosition).id, newList.get(newItemPosition).id);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return TextUtils.equals(oldList.get(oldItemPosition).url, newList.get(newItemPosition).url);
    }
}
