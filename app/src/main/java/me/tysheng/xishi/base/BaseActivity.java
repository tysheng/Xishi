package me.tysheng.xishi.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shengtianyang on 16/7/11.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private CompositeSubscription mSubscription;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    protected void add(Subscription s) {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }

        this.mSubscription.add(s);
    }

    protected void clear() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
//        overridePendingTransition(R.anim.slide_right_in,R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(0, R.anim.slide_right_out);
    }

    /**
     * Fragment之间的切换
     *
     * @param from 当前
     * @param to   目标
     * @param id
     * @param tag
     */
    protected void jumpFragment(Fragment from, Fragment to, int id, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        if (to == null) {
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (from == null) {
            transaction.add(id, to, tag);
        } else {
            transaction.hide(from);
            if (to.isAdded()) {
                transaction.show(to);
            } else {
                transaction.add(id, to, tag);
            }
        }
        transaction
                .setCustomAnimations(0, 0, android.R.anim.fade_in, android.R.anim.fade_out)
                .commitAllowingStateLoss();

    }

}
