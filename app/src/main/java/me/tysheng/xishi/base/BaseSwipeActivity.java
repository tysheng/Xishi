package me.tysheng.xishi.base;

import android.os.Bundle;
import android.view.View;

import me.tysheng.xishi.R;
import me.tysheng.xishi.utils.swipeback.SwipeBackActivityHelper;

/**
 * Created by Sty
 * Date: 16/9/3 19:45.
 */
public abstract class BaseSwipeActivity extends BaseActivity {
    protected SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    public void setSwipeBackEnable(boolean enable) {
        mHelper.getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_right_out);
    }
}
