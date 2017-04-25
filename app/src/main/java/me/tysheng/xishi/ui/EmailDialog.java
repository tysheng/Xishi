package me.tysheng.xishi.ui;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.BaseLoadMoreRecyclerViewAdapter;
import me.tysheng.xishi.databinding.DialogBottomSheetBinding;
import me.tysheng.xishi.utils.SystemUtil;

/**
 * Created by Sty
 * Date: 16/8/18 22:26.
 */
public class EmailDialog extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;

    @Override
    public void onStart() {
        super.onStart();
        //默认全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        DialogBottomSheetBinding binding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_bottom_sheet, null, false);
        ArrayList<Bean> list = new ArrayList<>();
        list.add(new Bean(R.drawable.ic_email_black_24dp, "邮件反馈"));
        list.add(new Bean(R.drawable.ic_update_black_24dp, "检查更新"));
        list.add(new Bean(R.drawable.ic_thumb_up_black_24dp, "捐赠支持"));
        list.add(new Bean(R.drawable.ic_content_copy_black_24dp, "复制邮箱"));
        list.add(new Bean(R.drawable.ic_compare_arrows_black_24dp, "主题切换"));
        binding.title.setText(String.format("版本 %s", SystemUtil.getVersionName()));
        binding.shareRecyclerView.setHasFixedSize(true);
        BaseLoadMoreRecyclerViewAdapter<Bean> adapter = new BaseLoadMoreRecyclerViewAdapter<Bean>(R.layout.item_dialog_bottom_sheet, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean item) {
                holder.setText(R.id.title, item.name)
                        .setImageResource(R.id.icon, item.res);
            }
        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MainActivity) getContext()).onItemClick(position);
                dismiss();
            }
        });
        adapter.bindToRecyclerView(binding.shareRecyclerView);
        dialog.setContentView(binding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        return dialog;
    }

    private static class Bean {
        int res;
        String name;

        Bean(int res, String name) {
            this.res = res;
            this.name = name;
        }
    }
}
