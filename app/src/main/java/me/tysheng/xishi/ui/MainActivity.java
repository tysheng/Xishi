package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.base.BaseMainActivity;
import me.tysheng.xishi.bean.Mains;
import me.tysheng.xishi.net.XishiRetrofit;
import me.tysheng.xishi.utils.HttpUtil;
import me.tysheng.xishi.utils.SystemUtil;
import me.tysheng.xishi.utils.fastcache.FastCache;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseMainActivity {
    private RecyclerView mRecyclerView;
    private MainsAdapter mAdapter;
    private int page = 1;
    private Toolbar mToolBar;
    private StaggeredGridLayoutManager mLayoutManager;
    private int y, x;
    private AppBarLayout mAppBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mToolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
        mAdapter = new MainsAdapter();
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String id = mAdapter.getData().get(i).id;
                if (!TextUtils.isEmpty(id))
                    startActivity(AlbumActivity.newIntent(MainActivity.this, id));
//                startActivity(AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id),
//                        ActivityOptionsCompat.makeScaleUpAnimation(view, 0, y / 5, x, y / 2).toBundle());
//                ActivityCompat.startActivity(MainActivity.this,AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id),
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,view,"scaledImage").toBundle());
            }
        });
        mAdapter.openLoadMore(10);
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
        View view = getLayoutInflater().inflate(R.layout.item_loading, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.setLoadingView(view);

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                getMains(page, 0);
            }
        });

//        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        y = point.y;
//        x = point.x;
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {

                    }
                });
    }

    public void showAlipayFail() {
        Snackbar.make(mToolBar, "支付宝账号已复制到剪贴板", Snackbar.LENGTH_SHORT).show();
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
                    Snackbar.make(mToolBar, "当前版本已是最新版", Snackbar.LENGTH_SHORT).show();
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
        int[] pos = new int[2];
        mLayoutManager.findFirstCompletelyVisibleItemPositions(pos);
        if (pos[0] == 0) {
            super.onBackPressed();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        int[] pos = new int[2];
        mLayoutManager.findFirstCompletelyVisibleItemPositions(pos);
        if (pos[1] > 60) {
            mLayoutManager.scrollToPosition(12);
            mAppBarLayout.setExpanded(true, true);
        }
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.stopScroll();
    }

    private void getMains(final int page, final int type) {
        final boolean[] flags = new boolean[1];
        flags[0] = true;
        add(FastCache.getAsync("page" + page, Mains.class)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (flags[0])
                            HttpUtil.convert(XishiRetrofit.get().getMains(page))
                                    .subscribe(new Subscriber<Mains>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            View failView = getLayoutInflater().inflate(R.layout.item_loading_error, (ViewGroup) mRecyclerView.getParent(), false);
                                            if (TextUtils.equals("HTTP 404 Not Found", e.getMessage())) {
                                                TextView textView = (TextView) failView.findViewById(R.id.textView);
                                                textView.setText("已到末尾,无更多内容");
                                            }
                                            mAdapter.setLoadMoreFailedView(failView);
                                            mAdapter.showLoadMoreFailedView();
                                        }

                                        @Override
                                        public void onNext(Mains mains) {
                                            if (type == 0) {
                                                mAdapter.setNewData(mains.album);
                                            } else {
                                                mAdapter.addData(mains.album);
                                            }
                                            FastCache.putAsync("page" + page, mains)
                                                    .subscribeOn(Schedulers.io())
                                                    .subscribe(new Action1<Boolean>() {
                                                        @Override
                                                        public void call(Boolean aBoolean) {

                                                        }
                                                    });
                                        }
                                    });
                    }
                })
                .subscribe(new Subscriber<Mains>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Mains mains) {
                        if (mains != null) {
                            flags[0] = false;
                            if (type == 0) {
                                mAdapter.setNewData(mains.album);
                            } else {
                                mAdapter.addData(mains.album);
                            }
                        }
                    }
                }));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zoom_out);
    }
}
