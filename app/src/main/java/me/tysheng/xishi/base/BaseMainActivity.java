package me.tysheng.xishi.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.tysheng.xishi.R;

/**
 * Created by Sty
 * Date: 16/9/3 19:45.
 */
public abstract class BaseMainActivity extends BaseActivity {
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.pop_exit_anim);
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_right_in,R.anim.swipeback_bottom_alpha_out);
    }
}
