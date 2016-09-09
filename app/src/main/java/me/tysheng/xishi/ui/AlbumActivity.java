package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;
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
import me.tysheng.xishi.utils.HttpUtil;
import me.tysheng.xishi.utils.ImageUtil;
import me.tysheng.xishi.view.HackyViewPager;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AlbumActivity extends BaseSwipeActivity {

    private HackyViewPager mViewPager;
    private TextView mIndicator;
    private AlbumAdapter mAdapter;
    private int mId;
    private List<Picture> mAlbums;
    private TextView title;
    private TextView content;
    private int mAmount;
    private int countForFinish;
    private AlphaAnimation mShowBottomAnim, mHideBottomAnim;
    private boolean mVisible = true;
    private ScrollView mScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT); //也可以设置成灰色透明的，比较符合Material Design的风格
        }
        setContentView(R.layout.activity_album);
        parseIntent();
        mViewPager = (HackyViewPager) findViewById(R.id.viewPager);
        mIndicator = (TextView) findViewById(R.id.indicator);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        mAlbums = new ArrayList<>();
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

        add(HttpUtil.convert(XishiRetrofit.get().getDayAlbums(mId))
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
                }));
    }

    private void selected(int position) {
        if (mAlbums.size() != 0) {
            SpannableString string = new SpannableString(String.format(Locale.getDefault(), "%d/%d", 1 + position, mAlbums.size()));
            RelativeSizeSpan sizeSpan0 = new RelativeSizeSpan(1.4f);
            RelativeSizeSpan sizeSpan1 = new RelativeSizeSpan(1.0f);
            RelativeSizeSpan sizeSpan2 = new RelativeSizeSpan(0.7f);
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                string.setSpan(sizeSpan1, 2, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                string.setSpan(sizeSpan1, 1, string.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            mIndicator.setText(string);
            SpannableString string1 = new SpannableString(mAlbums.get(position).title + "    " + mAlbums.get(position).author);
            string1.setSpan(sizeSpan1, 0, mAlbums.get(position).title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            string1.setSpan(sizeSpan2, mAlbums.get(position).title.length(), string1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            title.setText(string1);
            content.setText(mAlbums.get(position).content);
        }
    }

    public void hideOrShow() {
        if (mVisible) {
            if (mHideBottomAnim == null) {
                mHideBottomAnim = new AlphaAnimation((float) 1.0, (float) 0.0);
                mHideBottomAnim.setDuration(300);
//                mHideBottomAnim.setFillAfter(true);
            }
            mVisible = false;
            mIndicator.startAnimation(mHideBottomAnim);
            title.startAnimation(mHideBottomAnim);
            content.startAnimation(mHideBottomAnim);
            mIndicator.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);
        } else {
            if (mShowBottomAnim == null) {
                mShowBottomAnim = new AlphaAnimation((float) 0.0, (float) 1.0);
                mShowBottomAnim.setDuration(300);
//                mShowBottomAnim.setFillAfter(true);
            }
            mVisible = true;
            mIndicator.startAnimation(mShowBottomAnim);
            title.startAnimation(mShowBottomAnim);
            content.startAnimation(mShowBottomAnim);
            mIndicator.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.VISIBLE);
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

    public void saveImageToGallery(final int position, final int i) {
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
