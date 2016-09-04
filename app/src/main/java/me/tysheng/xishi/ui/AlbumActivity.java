package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.AlbumAdapter;
import me.tysheng.xishi.base.BaseSwipeActivity;
import me.tysheng.xishi.bean.DayAlbums;
import me.tysheng.xishi.bean.Picture;
import me.tysheng.xishi.net.XishiRetrofit;
import me.tysheng.xishi.utils.ImageUtil;
import me.tysheng.xishi.view.ViewPagerFixed;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AlbumActivity extends BaseSwipeActivity {

    private ViewPagerFixed mViewPager;
    private TextView mIndicator;
    private AlbumAdapter mAdapter;
    private int mId;
    private List<Picture> mAlbums = new ArrayList<>();
    private TextView title;
    private TextView content;
    private int mAmount;
    private int countForFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_album);
        parseIntent();
        mViewPager = (ViewPagerFixed) findViewById(R.id.viewPager);
        mIndicator = (TextView) findViewById(R.id.indicator);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        mAdapter = new AlbumAdapter(mAlbums, AlbumActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mAmount - 1 && positionOffsetPixels == 0) {
                    if (countForFinish++ > 8) {
                        finish();
                    }
                } else countForFinish = 0;
            }

            @Override
            public void onPageSelected(int position) {
                selected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        XishiRetrofit.get().getDayAlbums(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DayAlbums>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DayAlbums dayAlbums) {
                        mAlbums = dayAlbums.picture;
                        mAmount = mAlbums.size();
                        mAdapter.setData(mAlbums);
                        selected(0);
                    }
                });
    }

    private void selected(int position) {
        if (mAlbums.size()!=0){
            mIndicator.setText(String.format(Locale.getDefault(), "%d/%d", 1 + position, mAlbums.size()));
            title.setText(mAlbums.get(position).title);
            content.setText(mAlbums.get(position).content + "\n摄影  " + mAlbums.get(position).author);
        }
    }

    private AlphaAnimation mShowBottomAnim, mHideBottomAnim;
    private boolean mVisible = true;

    public void hideOrShow() {
        if (mVisible) {
            if (mHideBottomAnim == null) {
                mHideBottomAnim = new AlphaAnimation((float) 1.0, (float) 0.0);
                mHideBottomAnim.setDuration(300);
                mHideBottomAnim.setFillAfter(true);
            }
            mVisible = false;
            mIndicator.startAnimation(mHideBottomAnim);
            title.startAnimation(mHideBottomAnim);
            content.startAnimation(mHideBottomAnim);
        } else {
            if (mShowBottomAnim == null) {
                mShowBottomAnim = new AlphaAnimation((float) 0.0, (float) 1.0);
                mShowBottomAnim.setDuration(300);
                mShowBottomAnim.setFillAfter(true);
            }
            mVisible =true;
            mIndicator.startAnimation(mShowBottomAnim);
            title.startAnimation(mShowBottomAnim);
            content.startAnimation(mShowBottomAnim);
        }

    }

    private void parseIntent() {
        mId = getIntent().getIntExtra("albums", 1322);
    }

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("albums", Integer.valueOf(id));
        return intent;
    }

    public void saveImageToGallery(int position, int i) {
        requestPermission(position, i);
    }

    private void requestPermission(final int position, final int i) {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            ImageUtil.saveImageToGallery(AlbumActivity.this, mAlbums.get(position).url)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Action1<Uri>() {
                                        @Override
                                        public void call(Uri uri) {
                                            File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                            String msg = String.format(getString(R.string.picture_has_save_to),
                                                    appDir.getAbsolutePath());
                                            if (0 != i) {
                                                ImageUtil.shareImage(AlbumActivity.this, uri, i);
                                            } else
                                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Oops permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_right_out);
    }
}
