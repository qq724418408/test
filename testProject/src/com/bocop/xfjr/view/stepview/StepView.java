package com.bocop.xfjr.view.stepview;

import com.bocop.jxplatform.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * description：进度条view
 * <p/>
 * Created by TIAN FENG on 2017/8/23
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */

public class StepView extends View {
    // 文本过长时结尾文本
    private static final String TEXT_END = "...";
    // 文字大小-> 像素
    private int mTextSize = 32;
    // 每一个item的宽
    private int mItemWidth;
    // 适配器
    private StepAdapter mAdapter;
    // 文本画笔
    private Paint mTextPaint;
    // 图片画笔
    private Paint mBitmapPaint;
    // 线的画笔
    private Paint mLinePaint;
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
    private int mUnFocusLineColor = Color.GRAY;
    private int mFocusLineColor = Color.BLACK;
    // 执行进度
    private int mFocusPostion = -1;
    // 内部进度位置
    private int mInnerStepPosition = -1;
    private int mCircleRadius = 5;
    private int mInnerStepCount = 0;
    private int mInnerStatePosition = -1;
    private int mStepInnerCircleColor = Color.WHITE;

    private StepClickListener mListener;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStepView(context, attrs);
    }

    // 初始化
    private void initStepView(Context context, AttributeSet attrs) {
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
        mStepInnerCircleColor = array.getColor(R.styleable.StepView_stepInnerCircleColor, mStepInnerCircleColor);
        mCircleRadius = array.getDimensionPixelSize(R.styleable.StepView_stepInnerCircleRadius, mCircleRadius);
        array.recycle();

        // 画笔初始化
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 设置两头圆角
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mAdapter != null) {
            // 第二次請求重新佈局時設置佈局的高度
            int width = MeasureSpec.getSize(widthMeasureSpec);
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;
            setMeasuredDimension(width, (int) (mBitmapHeight + mPadding + textHeight));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 没有设置适配器不绘制
        if (mAdapter == null) {
            return;
        }
        // 每个item的宽
        mItemWidth = measureItemWidth();
        drawLine(canvas);
        drawBitmap(canvas);
        drawText(canvas);
    }

    // 画线
    private void drawLine(Canvas canvas) {
        // 线的起始高为图高的一半 - 线宽的一半
        int y = mBitmapHeight / 2;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            // 1.先画默认线段
            // 默认颜色
            mLinePaint.setColor(mUnFocusLineColor);
            mLinePaint.setStrokeWidth(mUnFocusLineWidth);
            // 最后一个不绘制线段 mAdapter.getCount() -1
            if (i < mAdapter.getCount() - 1) {
                canvas.drawLine(mItemWidth / 2 + mItemWidth * i, y, mItemWidth / 2 + mItemWidth * (i + 1), y, mLinePaint);
                drawInnerCircle(mInnerStepPosition, canvas, y);
            }
            // 2. 根据2次进度绘制点
            //  画聚焦线条
            if (i <= mFocusPostion && i < mAdapter.getCount()) {
                // 设置聚焦颜色的属性
                mLinePaint.setColor(mFocusLineColor);
                mLinePaint.setStrokeWidth(mFocusLineWidth);
                // 判断是否在二次进度位置
                if (i == mInnerStepPosition) {
                    drawInnerPositionStats(canvas, i, y);
                } else {
                    // 不在二次进度位置画普通的线条 0 不画 1 画 0 ->1 
                	int startX = mItemWidth / 2 + mItemWidth *( i-1);
                	if (startX<mItemWidth / 2 ) {
						startX = mItemWidth / 2 ;
					}
                	int endX = mItemWidth / 2 + mItemWidth *( i);
                	if (endX<mItemWidth / 2 ) {
                		endX = mItemWidth / 2 ;
					}
//                    canvas.drawLine(mItemWidth / 2 + mItemWidth *( i), y, mItemWidth / 2 + mItemWidth *( i+1) , y, mLinePaint);
                	canvas.drawLine(startX, y, endX, y, mLinePaint);
                }
            }
            // 白色擦拭多余部分
            mLinePaint.setColor(Color.WHITE);
            canvas.drawCircle(mItemWidth / 2 + mItemWidth * i, y, y, mLinePaint);
        }

    }

    // 二次进度内部圆
    private void drawInnerCircle(int position, Canvas canvas, int y) {
        if (mInnerStepCount == 0) return;
        // 二次进度的宽
        int innerItemWidth = (mItemWidth - mBitmapWidth) / mInnerStepCount;
        // 圆起点位置
        int cricleStartX = (int) ((position + 0.5) * mItemWidth + 0.5 * mBitmapWidth);
        if (mInnerStepPosition >= 0) {
            mLinePaint.setColor(mStepInnerCircleColor);
            // 循环画圆
            for (int i = 0; i < mInnerStepCount; i++) {
                int cx = cricleStartX + innerItemWidth / 2 + i * innerItemWidth;
                canvas.drawCircle(cx, y, mCircleRadius, mLinePaint);
            }
        }
    }

    /**
     * 画内部进度
     */
    private void drawInnerPositionStats(Canvas canvas, int position, int y) {
        if (mInnerStepCount == 0) return;
        // 二次进度的宽
        int innerItemWidth = (mItemWidth - mBitmapWidth) / mInnerStepCount;
        // 线的起点位置
        int lineStartX = (int) ((position + 0.5) * mItemWidth);
        // mInnerStatePosition
        for (int i = 0; i < mInnerStatePosition; i++) {
            // 画线条位置
            mLinePaint.setColor(mFocusLineColor);
            mLinePaint.setStrokeWidth(mFocusLineWidth);
            canvas.drawLine(lineStartX, y, mBitmapWidth / 2 + lineStartX + i * innerItemWidth + 0.5f * innerItemWidth, y, mLinePaint);
            if (i == mAdapter.getInnerStepCount()-1) {
            	canvas.drawLine(lineStartX, y, mBitmapWidth / 2 + lineStartX +  mAdapter.getInnerStepCount() * innerItemWidth + 0.5f * innerItemWidth, y, mLinePaint);
    		}
        }
        
        drawInnerCircle(position, canvas, y);
    }

    // 画图
    private void drawBitmap(Canvas canvas) {
        // 位置 = item的一半 - 图片的一半 + position * itemWidth
        for (int i = 0; i < mAdapter.getCount(); i++) {
            int x = mItemWidth / 2 - mBitmapWidth / 2 + i * mItemWidth;
            // 先画非聚焦图片
            canvas.drawBitmap(scaleBitmap(mAdapter.getDefaultBitmap(i)), x, 0, mBitmapPaint);
            if (i <= mFocusPostion) {
                // 聚焦图片
                canvas.drawBitmap(scaleBitmap(mAdapter.getHighLightBitmap(i)), x, 0, mBitmapPaint);
            }
        }
    }

    // 画文字
    private void drawText(Canvas canvas) {
        // 计算文本的高
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        // baseline = 文本基线 + 图片所占高 + 间距
        int baseLine = (int) (fontMetrics.descent + textHeight / 2 + mBitmapHeight + mPadding);
        // 计算文本的起点x位置
        for (int i = 0; i < mAdapter.getCount(); i++) {
            String description = mAdapter.getDescription(i);
            int textWidth = (int) mTextPaint.measureText(description);
            // 文本过长
            if (textWidth > mItemWidth) {
                // 单个文字长
                int singeWidth = textWidth / description.length();
                // 能写下的文本数
                int count = mItemWidth / singeWidth;
                // 最终以...结尾
                description = description.substring(0, count - 1) + TEXT_END;
                // 重新计算文本宽
                textWidth = (int) mTextPaint.measureText(description);
            }
            // 绘制起始位置
            int x = mItemWidth / 2 - textWidth / 2 + i * mItemWidth;
            // 根据选择位置设置画笔颜色
            if (i > mFocusPostion) {
                mTextPaint.setColor(mUnFocusTextColor);
            	// 字体重置
            	mTextPaint.setTypeface(Typeface.DEFAULT);
            } else {
            	// 字体加粗
                mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
                mTextPaint.setColor(mFocusTextColor);
            }
            canvas.drawText(description, x, baseLine, mTextPaint);
        }
    }

    /**
     * 获取item的宽
     */
    private int measureItemWidth() {
        mItemWidth = getWidth() / mAdapter.getCount();
        // 如果没有给宽度 —> wrap_content
        if (mItemWidth == 0) {
            // 获取最大图片的宽
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mItemWidth = Math.max(mItemWidth, mAdapter.getDefaultBitmap(i).getWidth());
            }
            // 获取最大文本的宽
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mItemWidth = (int) Math.max(mItemWidth, mTextPaint.measureText(mAdapter.getDescription(i)));
            }
        }
        return mItemWidth;
    }

    private Bitmap scaleBitmap(Bitmap bm) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 设置想要的大小
        int newWidth = mBitmapWidth;
        int newHeight = mBitmapHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////  暴露函数接口区 /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置适配器
     */
    public void setAdapter(StepAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("adapter is null");
        }
        this.mAdapter = adapter;
        this.mInnerStepCount = mAdapter.getInnerStepCount();
        mInnerStepPosition = mAdapter.getInnerStepPosition();
        requestLayout();
        invalidate();
    }

    /**
     * 设置聚焦位置
     */
    public void setFocusPosition(int position) {
        if (mAdapter == null) {
            throw new NullPointerException("adapter is null");
        }
        if (position < 0 || position > mAdapter.getCount() - 1) {
            throw new IndexOutOfBoundsException("请输入正确的位置，0到" + (mAdapter.getCount() - 1));
        }
        mFocusPostion = position;
        invalidate();
    }

    /**
     * 设置二次进度的当前位置
     */
    public void setInnerStatePosition(int position) {
        mInnerStatePosition = position;
        invalidate();
    }

    /**
     * 图文间距
     */
    public StepView setPadding(int padding) {
        mPadding = padding;
        invalidate();
        return this;
    }

    /**
     * 图片宽
     */
    public StepView setBitmapWidth(int bitmapWidth) {
        mBitmapWidth = bitmapWidth;
        invalidate();
        return this;
    }

    /**
     * 图片高
     */
    public StepView setBitmapHeight(int bitmapHeight) {
        mBitmapHeight = bitmapHeight;
        invalidate();
        return this;
    }

    /**
     * 未选中文本颜色
     */
    public StepView setUnFocusTextColor(int color) {
        mUnFocusTextColor = color;
        invalidate();
        return this;
    }

    /**
     * 选中文本颜色
     */
    public StepView setFocusTextColor(int color) {
        mFocusTextColor = color;
        invalidate();
        return this;
    }

    /**
     * 未选中线条颜色
     */
    public StepView setUnFocusLineColor(int color) {
        mUnFocusLineColor = color;
        invalidate();
        return this;
    }

    /**
     * 选中线条颜色
     */
    public StepView setFocusLineColor(int color) {
        mFocusLineColor = color;
        invalidate();
        return this;
    }

    /**
     * 线条宽
     */
    public StepView setFocusLineWidth(int width) {
        mFocusLineWidth = width;
        invalidate();
        return this;
    }

    /**
     * 文字大小
     */
    public StepView setTextSize(int size) {
        mTextSize = size;
        invalidate();
        return this;
    }

    /**
     * 设置2次进度的位置
     *
     * @param position 2次进度的位置
     */
    public StepView setInnerStepPosition(int position) {
        mInnerStepPosition = position;
        return this;
    }

    /**
     * 点击事件
     */
    public void setStepClickListener(StepClickListener stepClickListener) {
        this.mListener = stepClickListener;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 禁止父布局吃事件
//        getParent().requestDisallowInterceptTouchEvent(true);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            positiOnClick((int) ev.getX());
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 按下事件
     */
    private void positiOnClick(int downX) {
        if (mAdapter == null || mListener == null) {
            return;
        }
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (downX >= i * mItemWidth && downX < (i + 1) * mItemWidth) {
                mListener.onPosition(i);
                mFocusPostion = i;
                invalidate();
                break;
            }
        }
    }

    /**
     * 获取单个控件的宽
     */
    public int getItemWidth(){
        return measureItemWidth();
    }

    /**
     * 指示器是使用
     */
     void setOption(int uFocusTextColor, int padding,
                    int mUnFocusLineColor, int focusLineColor,
                    int textSize, int focusTextColor,
                    int bitmapWidth, int bitmapHeight,
                    int unFocusLineWidth, int focusLineWidth) {
         this.mUnFocusTextColor = uFocusTextColor;
         this.mPadding = padding;
         this.mUnFocusLineColor = mUnFocusLineColor;
         this.mFocusLineColor = focusLineColor;
         this.mTextSize = textSize;
         this.mFocusTextColor = focusTextColor;
         this.mBitmapWidth = bitmapWidth;
         this.mBitmapHeight = bitmapHeight;
         this.mUnFocusLineWidth = unFocusLineWidth;
         this.mFocusLineWidth = focusLineWidth;
         
    }
}
