package com.bocop.xfjr.view.stepview;

import com.bocop.jxplatform.R;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * description：
 * <p/>
 * Created by TIAN FENG on 2017/8/31
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class StepIndicatorView extends FrameLayout implements ViewPager.OnPageChangeListener, StepClickListener {

    private StepView mStepView;
    private StepIndicatorAdapter mAdapter;
    private int mItemWidth;
    private View mBottomTrackView;
    private LayoutParams mTrackParams;
    private int mInitLeftMargin;
    private ViewPager mViewPager;
    private FrameLayout mGroup;

    // 线宽
    private int mUnFocusLineWidth = 30;
    private int mFocusLineWidth = 20;
    // 图片和文本的间距
    private int mPadding = 10;
    // 图片宽高属性
    private int mBitmapWidth = 100;
    private int mBitmapHeight = 100;
    // 文本颜色
    private int mUnFocusTextColor = Color.GRAY;
    private int mFocusTextColor = Color.BLACK;
    // 线条颜色
    private int mTextSize = 32;
    // 线条颜色
    private int mUnFocusLineColor = Color.GRAY;
    private int mFocusLineColor = Color.BLACK;

    public StepIndicatorView(@NonNull Context context) {
        this(context, null);
    }

    public StepIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepIndicatorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.xfjr_step_indicator, this);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mUnFocusTextColor = array.getColor(R.styleable.StepView_unFocusTextColor, mUnFocusTextColor);
        mFocusTextColor = array.getColor(R.styleable.StepView_focusTextColor, mFocusTextColor);
        mBitmapWidth = array.getDimensionPixelSize(R.styleable.StepView_bitmapWidth, mBitmapWidth);
        mBitmapHeight = array.getDimensionPixelSize(R.styleable.StepView_bitmapHeight, mBitmapHeight);
        mUnFocusLineWidth = array.getDimensionPixelSize(R.styleable.StepView_unFocusLineWidth, mUnFocusLineWidth);
        mFocusLineWidth = array.getDimensionPixelSize(R.styleable.StepView_focusLineWidth, mFocusLineWidth);
        mPadding = array.getDimensionPixelSize(R.styleable.StepView_bitmapPadding, mPadding);
        mUnFocusLineColor = array.getColor(R.styleable.StepView_unFocusLineColor, mUnFocusTextColor);
        mFocusLineColor = array.getColor(R.styleable.StepView_focusLineColor, mFocusTextColor);
        mTextSize = array.getDimensionPixelSize(R.styleable.StepView_stepTextSize, mTextSize);
        array.recycle();
        mGroup = (FrameLayout) findViewById(R.id.group);
        mStepView = (StepView) findViewById(R.id.step);
        mStepView.setStepClickListener(this);
        mStepView.setOption(mUnFocusTextColor, mPadding,
                mUnFocusLineColor, mFocusLineColor, mTextSize,
                mFocusTextColor, mBitmapWidth, mBitmapHeight,
                mUnFocusLineWidth, mFocusLineWidth);
    }

    public void setAdapter(ViewPager viewPager, StepIndicatorAdapter adapter) {
        this.mAdapter = adapter;
        mViewPager = viewPager;
        mStepView.setAdapter(adapter);
        mStepView.setFocusPosition(0);
        mViewPager.setOnPageChangeListener(this);
        mStepView.post(new Runnable() {
            @Override
            public void run() {
                addBottomTrackView(mAdapter.getBottomTrackView(mStepView.getItemWidth()), mStepView.getItemWidth());

                Log.e("TAG", "itemWidth = " + mStepView.getItemWidth());
            }
        });

    }


    /**
     * 添加底部跟踪的指示器
     */
    public void addBottomTrackView(View bottomTrackView, int itemWidth) {
        if (bottomTrackView == null) {
            return;
        }
        this.mItemWidth = itemWidth;

        this.mBottomTrackView = bottomTrackView;
        // 添加底部跟踪的View
        mGroup.addView(mBottomTrackView);
        // 要让他在底部   宽度 -> 一个条目的宽度

        // 让其在底部
        mTrackParams = (LayoutParams) mBottomTrackView.getLayoutParams();
        mTrackParams.gravity = Gravity.BOTTOM;
        // 宽度 -> 一个条目的宽度  如果用户设置死了 那么就用用户的值如 88 如果没有那么就用mItemWidth
        int trackWidth = mTrackParams.width;
        // 没有设置宽度
        if (mTrackParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            trackWidth = mItemWidth;
        }
        // 设置的宽度过大
        if (trackWidth > mItemWidth) {
            trackWidth = mItemWidth;
        }
        // 最后确定宽度
        mTrackParams.width = trackWidth;
        // 确保在最中间
        mInitLeftMargin = (mItemWidth - trackWidth) / 2;
        mTrackParams.leftMargin = mInitLeftMargin;
    }

    /**
     * 滚动底部的指示器  -> leftMargin
     */
    public void scrollBottomTrack(int position, float positionOffset) {
        if (mBottomTrackView == null) {
            return;
        }
        int leftMargin = (int) ((position + positionOffset) * mItemWidth);
        // 控制leftMargin去移动
        mTrackParams.leftMargin = leftMargin + mInitLeftMargin;
        mBottomTrackView.setLayoutParams(mTrackParams);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        scrollBottomTrack(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        mStepView.setFocusPosition(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPosition(int position) {
        scrollBottomTrack(position);
        mViewPager.setCurrentItem(position);
    }


    /**
     * 滚动底部的指示器 点击移动带动画 -> leftMargin
     */
    private void scrollBottomTrack(int position) {
        if (mBottomTrackView == null) {
            return;
        }
        // 最终要移动的位置
        int finalLeftMargin = (int) ((position) * mItemWidth) + mInitLeftMargin;
        // 当前的位置
        int currentLeftMargin = mTrackParams.leftMargin;
        // 移动的距离
        int distance = finalLeftMargin - currentLeftMargin;

        // 带动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin).setDuration((long) (Math.abs(distance) * 0.4f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 会不断的回掉这个方法 不断的设置leftMrgin
                float currentLeftMargin = (float) animation.getAnimatedValue();
                mTrackParams.leftMargin = (int) currentLeftMargin;
                mBottomTrackView.setLayoutParams(mTrackParams);
            }
        });
        //  差值器  让速度越来越慢
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
