package me.tysheng.xishi.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
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
import android.view.ViewGroup;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.io.File;
import java.util.Locale;

import javax.inject.Inject;

import me.tysheng.xishi.R;
import me.tysheng.xishi.adapter.AlbumAdapter;
import me.tysheng.xishi.base.BaseSwipeActivity;
import me.tysheng.xishi.bean.DayAlbums;
import me.tysheng.xishi.databinding.ActivityAlbumBinding;
import me.tysheng.xishi.net.XishiService;
import me.tysheng.xishi.utils.ImageUtil;
import me.tysheng.xishi.utils.RxHelper;
import me.tysheng.xishi.utils.ScreenUtil;
import me.tysheng.xishi.utils.StySubscriber;
import me.tysheng.xishi.utils.SystemUtil;
import rx.functions.Action1;

public class AlbumActivity extends BaseSwipeActivity {
    @Inject
    public XishiService mXishiService;
    @Inject
    AlbumAdapter mAdapter;
    private ActivityAlbumBinding binding;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("albums", Integer.valueOf(id));
        return intent;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setScrollViewParams(newConfig.orientation);
    }

    private void setScrollViewParams(int orientation) {
        ViewGroup.LayoutParams params = binding.scrollView.getLayoutParams();
        params.height = ScreenUtil.dip2px(orientation == Configuration.ORIENTATION_LANDSCAPE ? 60 : 120);
        binding.scrollView.setLayoutParams(params);
    }

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

        injectAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        binding.setVisible(true);
        parseIntent();

        /**
         * 横屏
         */
        setScrollViewParams(this.getResources().getConfiguration().orientation);
        binding.viewPager.setAdapter(mAdapter);
        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == binding.getAmount() - 1 && positionOffsetPixels == 0) {
                    binding.setCountForFinish(binding.getCountForFinish() + 1);
                    if (binding.getCountForFinish() > 8) {
                        finish();
                    }
                } else {
                    binding.setCountForFinish(0);
                }
            }

            @Override
            public void onPageSelected(int position) {
                selected(position);
            }
        });

        mXishiService.getDayAlbums(binding.getId())
                .compose(this.<DayAlbums>bindUntilEvent(ActivityEvent.DESTROY))
                .compose(RxHelper.<DayAlbums>ioToMain())
                .subscribe(new StySubscriber<DayAlbums>() {
                    @Override
                    public void next(DayAlbums dayAlbums) {
                        binding.setAmount(dayAlbums.picture.size());
                        mAdapter.setData(dayAlbums.picture);
                        selected(0);
                    }
                });
    }

    private void selected(int position) {
        if (binding.getAmount() != 0) {
            SpannableString string = new SpannableString(String.format(Locale.getDefault(), "%d/%d", 1 + position, binding.getAmount()));
            RelativeSizeSpan sizeSpan0 = new RelativeSizeSpan(1.4f);
            RelativeSizeSpan sizeSpan2 = new RelativeSizeSpan(0.7f);
            if (position >= 9) {
                string.setSpan(sizeSpan0, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                string.setSpan(sizeSpan0, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            }
            binding.indicator.setText(string);
            SpannableString spannableString = new SpannableString(mAdapter.getData().get(position).title + "    " + mAdapter.getData().get(position).author);
            spannableString.setSpan(sizeSpan2, mAdapter.getData().get(position).title.length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            binding.title.setText(spannableString);
            binding.content.setText(mAdapter.getData().get(position).content);
        }
    }

    public void hideOrShow() {
        binding.setVisible(!binding.getVisible());
    }

    private void parseIntent() {
        binding.setId(getIntent().getIntExtra("albums", 1322));
    }

    public void saveImageToGallery(final int position, final int i) {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new StySubscriber<Boolean>() {
                    @Override
                    public void next(Boolean aBoolean) {
                        if (aBoolean) {
                            ImageUtil.saveImageToGallery(AlbumActivity.this, mAdapter.getData().get(position).url)
                                    .compose(RxHelper.<Uri>ioToMain())
                                    .subscribe(new Action1<Uri>() {
                                        @Override
                                        public void call(Uri uri) {
                                            File appDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                                            String msg = String.format(getString(R.string.picture_has_save_to),
                                                    appDir.getAbsolutePath());
                                            switch (i) {
                                                case 0:
                                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                                    break;
                                                case 1:
                                                    SystemUtil.share(AlbumActivity.this, "来自西施App的图片分享", "分享到", uri);
                                                    break;
                                                case 2:
                                                    ImageUtil.shareImage(AlbumActivity.this, uri);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Oops permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
