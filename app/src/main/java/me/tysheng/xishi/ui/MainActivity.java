package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.android.ActivityEvent;

import javax.inject.Inject;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.base.BaseMainActivity;
import me.tysheng.xishi.bean.Mains;
import me.tysheng.xishi.databinding.ActivityMainBinding;
import me.tysheng.xishi.net.XishiService;
import me.tysheng.xishi.utils.LogUtil;
import me.tysheng.xishi.utils.RxHelper;
import me.tysheng.xishi.utils.SnackBarUtil;
import me.tysheng.xishi.utils.StySubscriber;
import me.tysheng.xishi.view.RecycleViewDivider;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends BaseMainActivity {
    @Inject
    MainsAdapter mAdapter;
    @Inject
    XishiService mXishiService;
    private int page;
    private LinearLayoutManager mLayoutManager;
    private ActivityMainBinding binding;

    public void setDayNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.toolBar.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToTop();
                    }
                });
            }
        });
        binding.toolBar.inflateMenu(R.menu.menu_toolbar);
        binding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_send) {
                    EmailDialog dialog = new EmailDialog();
                    dialog.show(getSupportFragmentManager(), "");
                }
                return false;
            }
        });
        mLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(mLayoutManager);

        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String id = mAdapter.getData().get(i).id;
                if (!TextUtils.isEmpty(id)) {
                    Intent intent = AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id);
//                    ActivityOptionsCompat options =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, view, getString(R.string.app_name));
                    ActivityCompat.startActivity(MainActivity.this, intent, null);
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                binding.recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        getMains(++page, 1);
                    }
                });
            }
        });
        binding.recyclerView.addItemDecoration(new RecycleViewDivider(this));
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMains(page = 1, 0);
            }
        });

        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            SnackBarUtil.show(binding.coordinatorLayout, "没有这些权限可能会出现问题:(");
                        } else {
                            binding.swipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.swipeRefreshLayout.setRefreshing(true);
                                    getMains(page = 1, 0);
                                }
                            });
                        }

                    }
                });
    }

    public void showAlipayFail() {
        SnackBarUtil.show(binding.coordinatorLayout, "支付宝账号已复制到剪贴板");
        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("alipay", "353491983@qq.com"));//设置Clipboard 的内容
    }

    public void copyEmailAddress() {
        SnackBarUtil.show(binding.coordinatorLayout, "邮箱地址已复制到剪贴板");
        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", "353491983@qq.com"));//设置Clipboard 的内容
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            super.onBackPressed();
            return;
        }
        binding.recyclerView.stopScroll();
        int pos = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pos == 0) {
            super.onBackPressed();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        int pos = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (pos > 15) {
            mLayoutManager.scrollToPosition(5);
        }
        binding.appBarLayout.setExpanded(true, true);
        binding.recyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.recyclerView.stopScroll();
    }

    private void getMains(final int page, final int type) {
        mXishiService.getMains(page)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        binding.swipeRefreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                if (binding.swipeRefreshLayout.isRefreshing()) {
                                    binding.swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    }
                })
                .compose(this.<Mains>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.<Mains>ioToMain())
                .subscribe(new StySubscriber<Mains>() {
                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(e.getMessage());
                        if (TextUtils.equals("HTTP 404 Not Found", e.getMessage())) {
                            mAdapter.onEnd();
                        } else {
                            mAdapter.onError();
                        }
                    }

                    @Override
                    public void next(Mains mains) {
                        if (type == 0) {
                            mAdapter.setNewData(mains.album);
                        } else {
                            mAdapter.addData(mains.album);
                        }
                    }
                });
    }
}
