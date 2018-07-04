package com.bocop.xfjr.view.horizontalstepsview;

import static android.content.ContentValues.TAG;

import java.util.ArrayList;
import java.util.List;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.view.horizontalstepsview.bean.StepBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 日期：16/6/24 11:48
 * <p/>
 * 描述：
 */
public class VerticalStepView extends LinearLayout implements VerticalStepViewIndicator.OnDrawIndicatorListener {
    private RelativeLayout mTextContainer;
    private VerticalStepViewIndicator mStepsViewIndicator;
    private List<StepBean> mTexts;
    private int mComplectingPosition;
    private int mUnComplectedTextColor = getContext().getResources().getColor(R.color.uncompleted_text_color);//定义默认未完成文字的颜色;
    private int mComplectedTextColor = getContext().getResources().getColor(android.R.color.white);//定义默认完成文字的颜色;

    private int mTextSize = 14;//default textSize
    private LinearLayout contentView;
    private TextView mTextView;
    private TextView mTimeView;


    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.xfjr_widget_vertical_stepsview, this);
        mStepsViewIndicator = (VerticalStepViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (RelativeLayout) rootView.findViewById(R.id.rl_text_container);


    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStepsViewIndicator.post(new Runnable() {
            @Override
            public void run() {
                // 自己的宽度 - 进度的宽度 - 进度的左右margin - 自己的左右pandding - 自己的左右margin - 自己的左右margin = 自己子view的宽
                int width = MeasureSpec.getSize(widthMeasureSpec) - mStepsViewIndicator.getWidth() -
                        ((LayoutParams) mStepsViewIndicator.getLayoutParams()).rightMargin - ((LayoutParams) mStepsViewIndicator.getLayoutParams()).leftMargin -
                        ((LayoutParams) mTextContainer.getLayoutParams()).rightMargin - ((LayoutParams) mTextContainer.getLayoutParams()).leftMargin -
                        getPaddingRight()-getPaddingLeft()-
                        mTextContainer.getPaddingRight()-mTextContainer.getPaddingLeft() ;
                Log.e(TAG, "width = "+width);
                mTextContainer.getLayoutParams().width = width;
            }
        });
    }

    private int getTextHeight() {
        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (int) (metrics.descent - metrics.ascent);
    }


    /**
     * 设置显示的文字
     *
     * @param texts
     * @return
     */
    public VerticalStepView setStepViewTexts(List<StepBean> texts) {
        mTexts = texts;
        if (texts != null) {
            mStepsViewIndicator.setStepNum(texts);
        } else {
            mStepsViewIndicator.setStepNum(new ArrayList<StepBean>());
        }
        return this;
    }

    /**
     * 设置正在进行的position
     *
     * @param complectingPosition
     * @return
     */
    public VerticalStepView setStepsViewIndicatorComplectingPosition(int complectingPosition) {
        mComplectingPosition = complectingPosition;
        mStepsViewIndicator.setComplectingPosition(complectingPosition);
        return this;
    }

    /**
     * 设置未完成文字的颜色
     *
     * @param unComplectedTextColor
     * @return
     */
    public VerticalStepView setStepViewUnComplectedTextColor(int unComplectedTextColor) {
        mUnComplectedTextColor = unComplectedTextColor;
        return this;
    }

    /**
     * 设置完成文字的颜色
     *
     * @param complectedTextColor
     * @return
     */
    public VerticalStepView setStepViewComplectedTextColor(int complectedTextColor) {
        this.mComplectedTextColor = complectedTextColor;
        return this;
    }

    /**
     * 设置StepsViewIndicator未完成线的颜色
     *
     * @param unCompletedLineColor
     * @return
     */
    public VerticalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator完成线的颜色
     *
     * @param completedLineColor
     * @return
     */
    public VerticalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor) {
        mStepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    /**
     * 设置StepsViewIndicator默认图片
     *
     * @param defaultIcon
     */
    public VerticalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator已完成图片
     *
     * @param completeIcon
     */
    public VerticalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon) {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    /**
     * 设置StepsViewIndicator正在进行中的图片
     *
     * @param attentionIcon
     */
    public VerticalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon) {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    /**
     * is reverse draw 是否倒序画
     *
     * @param isReverSe default is true
     * @return
     */
    public VerticalStepView reverseDraw(boolean isReverSe) {
        this.mStepsViewIndicator.reverseDraw(isReverSe);
        return this;
    }

    /**
     * set linePadding  proportion 设置线间距的比例系数
     *
     * @param linePaddingProportion
     * @return
     */
    public VerticalStepView setLinePaddingProportion(float linePaddingProportion) {
        this.mStepsViewIndicator.setIndicatorLinePaddingProportion(linePaddingProportion);
        return this;
    }


    /**
     * set textSize
     *
     * @param textSize
     * @return
     */
    public VerticalStepView setTextSize(int textSize) {
        if (textSize > 0) {
            mTextSize = textSize;
            requestLayout();
        }
        return this;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void ondrawIndicator() {
        Log.e("VerticalStepViewIndicator", "width =  " +mTextContainer.getWidth() + ", height = " + mTextContainer.getHeight() );
      if (mTextContainer.getWidth()  == 0){
          requestLayout();
      }
        if (mTextContainer != null) {
            mTextContainer.removeAllViews();//clear ViewGroup
            List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
            if (mTexts != null && complectedXPosition != null && complectedXPosition.size() > 0) {
                for (int i = 0; i < mTexts.size(); i++) {
                    contentView = new LinearLayout(getContext());
                    contentView.setY(complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2);
                    contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    contentView.setOrientation(LinearLayout.VERTICAL);
                    contentView.setShowDividers(SHOW_DIVIDER_END);
                    contentView.setDividerDrawable(getContext().getResources().getDrawable(R.drawable.xfjr_linearlayout_divider));

                    mTextView = new TextView(getContext());
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
                    mTextView.setText(mTexts.get(i).getName());
                    mTextView.setMaxLines(2);
                    mTextView.setEllipsize(TextUtils.TruncateAt.END);
//                    mTextView.setY(complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2);
                    mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (i <= mComplectingPosition) {
                        mTextView.setTypeface(null, Typeface.BOLD);
                        mTextView.setTextColor(mComplectedTextColor);
                    } else {
                        mTextView.setTextColor(mUnComplectedTextColor);
                    }
                    contentView.addView(mTextView);

                    mTimeView = new TextView(getContext());
                    mTimeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
                    mTimeView.setText(mTexts.get(i).getTime());
//                    mTimeView.setY(complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2);
                    mTimeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (i <= mComplectingPosition) {
                        mTimeView.setTypeface(null, Typeface.BOLD);
                        mTimeView.setTextColor(mComplectedTextColor);
                    } else {
                        mTimeView.setTextColor(mUnComplectedTextColor);
                    }
                    contentView.addView(mTimeView);

                    mTextContainer.addView(contentView);
                }
            }
        }
    }

}
