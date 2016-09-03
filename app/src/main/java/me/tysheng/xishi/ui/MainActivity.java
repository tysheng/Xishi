package me.tysheng.xishi.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.base.BaseMainActivity;
import me.tysheng.xishi.bean.Mains;
import me.tysheng.xishi.net.XishiRetrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseMainActivity {
    private RecyclerView mRecyclerView;
    private MainsAdapter mAdapter;
    private int page = 1;
    private Toolbar mToolBar;
    private StaggeredGridLayoutManager mLayoutManager;
    private int y, x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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


        getMains(page, 0);
//        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        y = point.y;
//        x = point.x;
    }

    @Override
    public void onBackPressed() {
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
        }
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.stopScroll();
    }

    private void getMains(int page, final int type) {
        add(XishiRetrofit.get().getMains(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Mains>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        final View failView = getLayoutInflater().inflate(R.layout.item_loading_error, (ViewGroup) mRecyclerView.getParent(), false);
                        mAdapter.setLoadMoreFailedView(failView);
                        mAdapter.showLoadMoreFailedView();
//                        mAdapter.addFooterView(failView);
//                        failView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mAdapter.removeFooterView(failView);
//                                mAdapter.openLoadMore(10);
//                            }
//                        });
                    }

                    @Override
                    public void onNext(Mains mains) {
                        if (type == 0)
                            mAdapter.setNewData(mains.album);
                        else {
                            mAdapter.addData(mains.album);
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
