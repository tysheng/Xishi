package me.tysheng.xishi.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import me.tysheng.xishi.App;
import me.tysheng.xishi.R;
import me.tysheng.xishi.bean.Picture;
import me.tysheng.xishi.dagger.component.DaggerAdapterComponent;
import me.tysheng.xishi.ui.AlbumActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Sty
 * Date: 16/8/23 20:17.
 */
public class AlbumAdapter extends BaseGalleryAdapter<Picture> {
    @Inject
    public AlbumAdapter(Activity activity) {
        super(new ArrayList<Picture>(), activity);
        DaggerAdapterComponent.builder()
                .applicationComponent(((App) activity.getApplicationContext()).getApplicationComponent())
                .build().inject(this);
    }


    @Override
    protected void initOtherView(View view, int position) {


    }

    @Override
    protected void initAttacher(PhotoViewAttacher attacher, final int position) {
        attacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(mActivity, R.style.BlackDialog)
                        .setItems(new String[]{"保存", "分享", "分享给微信好友"}, new DialogInterface.OnClickListener() {
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
    protected CharSequence setPageTitle(int position) {
        return String.valueOf(position);
    }
}
