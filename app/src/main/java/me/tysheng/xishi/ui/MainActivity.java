package me.tysheng.xishi.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.MainsAdapter;
import me.tysheng.xishi.base.BaseActivity;
import me.tysheng.xishi.bean.Mains;
import me.tysheng.xishi.net.XishiRetrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
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
        mAdapter = new MainsAdapter(null);
        mAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                startActivity(AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id),
                        ActivityOptionsCompat.makeScaleUpAnimation(view, 0, y / 5, x, y / 2).toBundle());
//                ActivityCompat.startActivity(MainActivity.this,AlbumActivity.newIntent(MainActivity.this, mAdapter.getData().get(i).id),
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,view,"scaledImage").toBundle());
            }
        });
        mAdapter.openLoadMore(10, true);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getMains(++page, 1);
            }
        });
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop();
            }
        });
        getMains(page, 0);
        Display display = MainActivity.this.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        y = point.y;
        x = point.x;
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
        add(XishiRetrofit.getQuanziApi().getMains(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Mains>() {
                    @Override
                    public void call(Mains mains) {
                        if (type == 0)
                            mAdapter.setNewData(mains.album);
                        else {
                            mAdapter.notifyDataChangedAfterLoadMore(mains.album, true);
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
