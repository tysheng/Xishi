package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.android.ActivityEvent;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.base.BaseMainActivity;
import me.tysheng.xishi.bean.Mains;
import me.tysheng.xishi.net.XishiRetrofit;
import me.tysheng.xishi.utils.LogUtil;
import me.tysheng.xishi.utils.RxHelper;
import me.tysheng.xishi.utils.SnackBarUtil;
import me.tysheng.xishi.utils.StySubscriber;
import me.tysheng.xishi.utils.SystemUtil;
import me.tysheng.xishi.view.RecycleViewDivider;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends BaseMainActivity {
    private RecyclerView mRecyclerView;
    private MainsAdapter mAdapter;
    private int page;
    private Toolbar mToolBar;
    private LinearLayoutManager mLayoutManager;
    private int y, x;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public void setDayNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mToolBar.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToTop();
                    }
                });
            }
        });
        mToolBar.inflateMenu(R.menu.menu_toolbar);
        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_send) {
                    EmailDialog dialog = new EmailDialog();
                    dialog.show(getSupportFragmentManager(), "");
                }
                return false;
            }
        });
        mAdapter = new MainsAdapter(this, 10);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String id = mAdapter.getData().get(i).id;
                if (!TextUtils.isEmpty(id)) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.black);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id),
                                ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, x / 2, y / 2).toBundle());
                    } else {
                        startActivity(AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id));
                    }
                    bitmap.recycle();
                }
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        getMains(++page, 1);
                    }
                });
            }
        });
        mRecyclerView.addItemDecoration(new RecycleViewDivider(this));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMains(page = 1, 0);
            }
        });
        /**
         * Height and Width
         */
        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        y = point.y;
        x = point.x;


        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            SnackBarUtil.show(mCoordinatorLayout, "没有这些权限可能会出现问题:(");
                        } else {
                            mSwipeRefreshLayout.post(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(true);
                                    getMains(page = 1, 0);
                                }
                            });
                        }

                    }
                });
    }

    public void showAlipayFail() {
        SnackBarUtil.show(mCoordinatorLayout, "支付宝账号已复制到剪贴板");
        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("alipay", "353491983@qq.com"));//设置Clipboard 的内容
    }

    public void copyEmailAddress() {
        SnackBarUtil.show(mCoordinatorLayout, "邮箱地址已复制到剪贴板");
        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText("email", "353491983@qq.com"));//设置Clipboard 的内容
    }

    public void checkVersionByBaidu() {
        BDAutoUpdateSDK.cpUpdateCheck(this, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {
                if (appUpdateInfo != null && appUpdateInfo.getAppVersionCode() > SystemUtil.getVersionCode()) {
                    BDAutoUpdateSDK.uiUpdateAction(MainActivity.this,
                            new com.baidu.autoupdatesdk.UICheckUpdateCallback() {
                                @Override
                                public void onCheckComplete() {
                                }
                            });
                } else {
                    SnackBarUtil.show(mCoordinatorLayout, "当前版本已是最新版");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            super.onBackPressed();
            return;
        }
        mRecyclerView.stopScroll();
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
        mAppBarLayout.setExpanded(true, true);
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.stopScroll();
    }

    private void getMains(final int page, final int type) {
        XishiRetrofit.get().getMains(page)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mSwipeRefreshLayout.isRefreshing())
                            mSwipeRefreshLayout.setRefreshing(false);
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
                        LogUtil.d(mains.album.size() + "");
                        if (type == 0) {
                            mAdapter.setNewData(mains.album);
                        } else {
                            mAdapter.addData(mains.album);
                        }
                    }
                });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zoom_out);
    }
}
