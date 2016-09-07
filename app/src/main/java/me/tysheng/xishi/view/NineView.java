package me.tysheng.xishi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.util.List;

/**
 * @author shoyu
 * @ClassName MultiImageView.java
 * @Description: 显示1~N张图片的View
 */

public abstract class NineView extends LinearLayout {
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<String> mImagesList;
    protected Context mContext;
    /**
     * 长度 单位为Pixel
     **/
    private int mSingleLength;  // 单张图最大允许宽高
    private int mMultiLength;// 多张图的宽高
    private int mImagePadding;// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams mSingleParams;
    private LayoutParams mMultiParams, mMultiParamsColumnFirst;
    private LayoutParams mRowParams;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public NineView(Context context) {
        this(context, null);
    }

    public NineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImagePadding = dip2px(3);
        mContext = getContext();
    }

    public void setList(List<String> lists) {
        if (lists == null || lists.size() == 0) {
            setVisibility(GONE);
            return;
        } else
            setVisibility(VISIBLE);
        mImagesList = lists;

        if (MAX_WIDTH > 0) {
            mMultiLength = (MAX_WIDTH - mImagePadding * 2) / 3;
            mSingleLength = MAX_WIDTH * 2 / 3;
            initLayoutParams();
        }

        initView();
        this.requestLayout();
    }

    public void setList(List<String> lists, int padding) {
        if (lists == null || lists.size() == 0) {
            setVisibility(GONE);
            return;
        } else
            setVisibility(VISIBLE);
        mImagesList = lists;
        mImagePadding = dip2px(padding);
        if (MAX_WIDTH > 0) {
            mMultiLength = (MAX_WIDTH - mImagePadding * 2) / 3;
            mSingleLength = MAX_WIDTH * 2 / 3;
            initLayoutParams();
        }

        initView();
        this.requestLayout();
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (mImagesList != null && mImagesList.size() > 0) {
                    setList(mImagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initLayoutParams() {
        mSingleParams = new LayoutParams(mSingleLength, mSingleLength);
        mMultiParamsColumnFirst = new LayoutParams(mMultiLength, mMultiLength);
        mMultiParams = new LayoutParams(mMultiLength, mMultiLength);
        mMultiParams.setMargins(mImagePadding, 0, 0, 0);
        mRowParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(mContext));
            return;
        }
        if (mImagesList.size() == 1) {
            addView(createImageView(0, false));
        } else {
            int allCount = mImagesList.size();
            MAX_PER_ROW_COUNT = (allCount == 4 ? 2 : 3);
            int rowCount = allCount / MAX_PER_ROW_COUNT
                    + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(mContext);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(mRowParams);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, mImagePadding, 0, 0);
                }
                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
                        : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);
                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    rowLayout.addView(createImageView(position, true));
                }
            }
        }
    }

    private ImageView createImageView(int position, final boolean isMultiImage) {
        String url = mImagesList.get(position);
        ImageView imageView = initImageView();
        imageView.setScaleType(ScaleType.CENTER_CROP);
        if (isMultiImage) {
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? mMultiParamsColumnFirst : mMultiParams);
        } else {
            imageView.setMaxHeight(mSingleLength);
            imageView.setLayoutParams(mSingleParams);
        }
        imageView.setId(url.hashCode());
        imageView.setOnClickListener(new ImageOnClickListener(position, mImagesList));
        loadIntoImage(url, imageView);
        return imageView;
    }

    protected abstract void loadIntoImage(String url, ImageView imageView);

    protected abstract ImageView initImageView();

    private class ImageOnClickListener implements OnClickListener {

        private int position;
        private List<String> mList;

        public ImageOnClickListener(int position, List<String> mList) {
            this.position = position;
            this.mList = mList;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position, mList);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, List<String> mList);
    }
}