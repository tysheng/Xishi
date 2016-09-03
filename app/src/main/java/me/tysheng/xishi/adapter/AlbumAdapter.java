package me.tysheng.xishi.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Picture;
import me.tysheng.xishi.ui.AlbumActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Sty
 * Date: 16/8/23 20:17.
 */
public class AlbumAdapter extends BaseGalleryAdapter<Picture> {
    public AlbumAdapter(List<Picture> images, Activity activity) {
        super(images, activity);
    }

    public static int sVelocityY = 12000;

    @Override
    protected void initOtherView(View view, int position) {


    }

    public int dip2px(float dipValue) {
        return (int) (dipValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void initAttacher(PhotoViewAttacher attacher, final int position) {
        attacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mActivity)
                        .setItems(new String[]{"保存", "分享", "分享到朋友圈"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                ((AlbumActivity) mActivity).saveImageToGallery(position, i);
                            }
                        }).show();
                return true;
            }
        });
        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((AlbumActivity) mActivity).hideOrShow();
            }
        });
        attacher.setOnSingleFlingListener(new PhotoViewAttacher.OnSingleFlingListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (velocityY > sVelocityY) {
                    mActivity.finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected String setItemUrl(int position) {
        return mImages.get(position).url;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_album;
    }

    @Override
    protected int setImageViewId() {
        return R.id.imageView;
    }


    @Override
    protected CharSequence setPageTitle(int position) {
        return String.valueOf(position);
    }
}