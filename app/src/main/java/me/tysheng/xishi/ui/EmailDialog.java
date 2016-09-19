package me.tysheng.xishi.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import me.tysheng.xishi.App;
import me.tysheng.xishi.R;
import me.tysheng.xishi.utils.AlipayZeroSdk;
import me.tysheng.xishi.utils.SystemUtil;

/**
 * Created by Sty
 * Date: 16/8/18 22:26.
 */
public class EmailDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_email, (ViewGroup) getView(),false);
        AlertDialog dialog = new AlertDialog.Builder(getActivity(),R.style.BlackDialog)
//                .setView(view)
                .setItems(new String[]{"邮件反馈", "检查更新", "捐赠", "复制邮箱", "主题切换"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                SystemUtil.sendEmail(getContext());
                                break;
                            case 1:
                                ((MainActivity) getContext()).checkVersionByBaidu();
                                break;
                            case 2:
                                if (AlipayZeroSdk.hasInstalledAlipayClient(App.get())) {
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
                    }
                })
                .setTitle(String.format("版本 %s", SystemUtil.getVersionName()))
                .create();
        dialog.show();
        return dialog;
    }
}
