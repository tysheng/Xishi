package me.tysheng.xishi.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.tysheng.xishi.R;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by shengtianyang on 16/4/30.
 */
public abstract class BaseGalleryAdapter<T> extends PagerAdapter {
    protected List<T> mImages;
    protected Activity mActivity;

    public BaseGalleryAdapter(List<T> images, Activity activity) {
        mImages = images;
        mActivity = activity;
    }

    public void setData(List<T> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup view = (ViewGroup) LayoutInflater.from(mActivity).inflate(setLayoutId(), container, false);
        final PhotoViewAttacher mAttacher;
        final ImageView imageView = (ImageView) view.findViewById(setImageViewId());
        final ProgressBar progressBar = (ProgressBar) view.findViewById(setProgressBarId());
        initOtherView(view, position);
        mAttacher = new PhotoViewAttacher(imageView);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mAttacher.setMinimumScale(1);
        initAttacher(mAttacher, position);
        Picasso.with(mActivity)
                .load(setItemUrl(position))
                .error(R.layout.item_loading)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        mAttacher.update();
                    }

                    @Override
                    public void onError() {

                    }
                });
        container.addView(view);
        return view;
    }

    protected abstract void initOtherView(View view, int position);

    protected abstract void initAttacher(PhotoViewAttacher attacher, int position);

    protected abstract String setItemUrl(int position);

    protected abstract int setLayoutId();

    protected abstract int setImageViewId();

    protected int setProgressBarId() {
        return R.id.progressBar;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return setPageTitle(position);
    }

    protected abstract CharSequence setPageTitle(int position);

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
