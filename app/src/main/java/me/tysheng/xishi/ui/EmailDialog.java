package me.tysheng.xishi.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_email, (ViewGroup) getView(),false);
        AlertDialog dialog = new AlertDialog.Builder(getActivity(),R.style.BlackDialog)
                .setView(view)
                .setTitle(String.format("关于-Ver.%s", SystemUtil.getVersionName()))
                .setPositiveButton("捐赠", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        https://qr.alipay.com/aex07650apwol9ijoslnm39
                        if (AlipayZeroSdk.hasInstalledAlipayClient(App.get())) {
                            if (!AlipayZeroSdk.startAlipayClient(getActivity(), "aex07650apwol9ijoslnm39")){
                                ((MainActivity) getContext()).showAlipayFail();
                            }
                        } else
                            ((MainActivity) getContext()).showAlipayFail();
                    }
                })
                .setNegativeButton("检查更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getContext()).checkVersionByBaidu();
                    }
                })
                .create();
        dialog.show();
        return dialog;
    }
}
