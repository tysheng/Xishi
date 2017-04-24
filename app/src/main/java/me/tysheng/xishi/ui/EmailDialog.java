package me.tysheng.xishi.ui;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;

import me.tysheng.xishi.BuildConfig;
import me.tysheng.xishi.R;
import me.tysheng.xishi.databinding.DialogBottomSheetBinding;
import me.tysheng.xishi.utils.AlipayZeroSdk;
import me.tysheng.xishi.utils.SystemUtil;

/**
 * Created by Sty
 * Date: 16/8/18 22:26.
 */
public class EmailDialog extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private DialogBottomSheetBinding mBinding;

    @Override
    public void onStart() {
        super.onStart();
        //默认全屏展开
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.dialog_bottom_sheet, null, false);
        ArrayList<Bean> list = new ArrayList<>();
        list.add(new Bean(R.drawable.ic_email_black_24dp, "邮件反馈"));
        list.add(new Bean(R.drawable.ic_update_black_24dp, "检查更新"));
        list.add(new Bean(R.drawable.ic_thumb_up_black_24dp, "捐赠支持"));
        list.add(new Bean(R.drawable.ic_content_copy_black_24dp, "复制邮箱"));
        list.add(new Bean(R.drawable.ic_compare_arrows_black_24dp, "主题切换"));
        mBinding.title.setText(String.format("版本 %s", SystemUtil.getVersionName()));
        mBinding.shareRecyclerView.setHasFixedSize(true);
        BaseQuickAdapter<Bean> adapter = new BaseQuickAdapter<Bean>(R.layout.item_dialog_bottom_sheet, list) {
            @Override
            protected void convert(BaseViewHolder holder, Bean item) {
                holder.setText(R.id.title, item.name)
                        .setImageResource(R.id.icon, item.res);
            }
        };
        mBinding.shareRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (i) {
                    case 0:
                        SystemUtil.sendEmail(getContext());
                        break;
                    case 1:
                        SystemUtil.shareAppShop(getContext(), BuildConfig.APPLICATION_ID);
                        break;
                    case 2:
                        if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
                            if (!AlipayZeroSdk.startAlipayClient(getActivity(), "aex07650apwol9ijoslnm39")) {
                                ((MainActivity) getContext()).showAlipayFail();
                            }
                        } else
                            ((MainActivity) getContext()).showAlipayFail();
                        break;
                    case 3:
                        ((MainActivity) getContext()).copyEmailAddress();
                        break;
                    case 4:
                        dismiss();
                        ((MainActivity) getContext()).setDayNightMode();
                        break;
                    default:
                        break;
                }
                dismiss();
            }
        });
        mBinding.shareRecyclerView.setAdapter(adapter);
        dialog.setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        return dialog;
//                .setItems(new String[]{"邮件反馈", "检查更新", "捐赠", "复制邮箱", "主题切换"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                SystemUtil.sendEmail(getContext());
//                                break;
//                            case 1:
//                                SystemUtil.shareAppShop(getContext(), BuildConfig.APPLICATION_ID);
//                                break;
//                            case 2:
//                                if (AlipayZeroSdk.hasInstalledAlipayClient(getContext())) {
//                                    if (!AlipayZeroSdk.startAlipayClient(getActivity(), "aex07650apwol9ijoslnm39")) {
//                                        ((MainActivity) getContext()).showAlipayFail();
//                                    }
//                                } else
//                                    ((MainActivity) getContext()).showAlipayFail();
//                                break;
//                            case 3:
//                                ((MainActivity) getContext()).copyEmailAddress();
//                                break;
//                            case 4:
//                                dismiss();
//                                ((MainActivity) getContext()).setDayNightMode();
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                })
//                .setTitle(String.format("版本 %s", SystemUtil.getVersionName()))
//                .create();
//        dialog.show();
//        return dialog;
    }

    static class Bean {
        int res;
        String name;

        Bean(int res, String name) {
            this.res = res;
            this.name = name;
        }
    }
}
