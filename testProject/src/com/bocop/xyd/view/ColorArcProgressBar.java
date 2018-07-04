package com.bocop.xyd.view;

import com.bocop.jxplatform.R;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


/**
 * colorful arc progress bar
 */
public class ColorArcProgressBar extends View {//

    private int mWidth;
    private int mHeight;
    private int diameter = 500;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(15);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    private float longdegree = 0;
    private float shortdegree = 0;
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(2);

    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private int bgArcColor = Color.parseColor("#111111");
    private String titleString;
    private String hintString;
    private String definitionContent;

    private int currentVauleColor;

    private boolean isShowCurrentSpeed = true;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private boolean isNeedDefinitionContent;

    private float definitionContentWidth;


    private boolean showMoveCircle = true;// 是否显示移动的小园。
    private Paint mPaintCircle = null;
    private Paint mInnerPaintCircle = null;
    private Paint definitionContentPaint = null;
    private int barCircleColor = Color.parseColor("#ffffff");
    private int innerCircleColor = Color.parseColor("#ca2222");


    // sweepAngle / maxValues 的值
    private float k;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     * @param context drawable
     * @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};
        bgArcColor=a.getColor(R.styleable.ColorArcProgressBar_back_color, bgArcColor);

        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width, dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content, false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        currentVauleColor=a.getColor(R.styleable.ColorArcProgressBar_current_value_color,Color.BLACK);
        showMoveCircle= a.getBoolean(R.styleable.ColorArcProgressBar_showMoveCircle, true);
        isNeedDefinitionContent=a.getBoolean(R.styleable.ColorArcProgressBar_is_need_definition_content, false);
        definitionContent = a.getString(R.styleable.ColorArcProgressBar_definition_content);


        setMaxValues(maxValues);
        setCurrentValues(curValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height= (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        if(sweepAngle<=180){
            height=height/2;
        } else {
            float degree=(360-sweepAngle)/2;
            double length=diameter/2-diameter/2*Math.cos(degree*Math.PI/180);
            height= (int) (height-length);
        }

        setMeasuredDimension(width, height);
    }
//    private
    private void initView() {

        diameter =  getScreenWidth()-2*148;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth/2 + DEGREE_PROGRESS_DISTANCE);

        startAngle=sweepAngle>=180?180-(sweepAngle-180)/2:180+(90-sweepAngle/2);
        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(bgArcColor);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);


        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(currentVauleColor);
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        //进度末端的圆换
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(barCircleColor);

        mInnerPaintCircle = new Paint();
        mInnerPaintCircle.setAntiAlias(true);
        mInnerPaintCircle.setColor(innerCircleColor);

        definitionContentPaint=new Paint();
        definitionContentPaint.setAntiAlias(true);
        definitionContentPaint.setColor(Color.parseColor("#fefefe"));
        definitionContentPaint.setTextSize(44);
        definitionContentPaint.setTypeface(Typeface.DEFAULT_BOLD);

        if(!TextUtils.isEmpty(definitionContent))
        for (int i=0;i<definitionContent.length();i++){
            float[] widths=new float[1];
            String srt=String.valueOf(definitionContent.charAt(i));
            definitionContentPaint.getTextWidths(srt,widths);
            definitionContentWidth+=widths[0];
        }


        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2,
                            centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        rotateMatrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        if(currentAngle!=0)
            canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);

        if(isNeedDefinitionContent){
            canvas.drawText(definitionContent, centerX-definitionContentWidth/2, centerY + textSize / 3, definitionContentPaint);
        }else{
            if (isNeedContent) {
                canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
            }
        }

        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3, curSpeedPaint);
        }
        // 随ProgressBar移动的圆环。
        if (showMoveCircle) {
            canvas.drawCircle(
                    (float) (centerX + diameter/2
                            * Math.cos((startAngle+currentAngle) * Math.PI / 180)),
                    (float) (centerY + diameter/2
                            * Math.sin((startAngle+currentAngle) * Math.PI / 180)),
                    progressWidth / 2-1, mPaintCircle);// 外圆
            canvas.drawCircle(
                    (float) (centerX + diameter/2
                            * Math.cos((startAngle+currentAngle) * Math.PI / 180)),
                    (float) (centerY + diameter/2
                            * Math.sin((startAngle+currentAngle) * Math.PI / 180)),
                    progressWidth / 2-4, mInnerPaintCircle);// 内圆
        }
        invalidate();

    }

    /**
     * 设置最大值
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**
     * 设置当前值
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 设置标题
     * @param title
     */
    private void setTitle(String title){
        this.titleString = title;
    }

    /**
     * 设置是否显示标题
     * @param isNeedTitle
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * 设置是否显示单位文字
     * @param isNeedUnit
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * 设置是否显示外部刻度盘
     * @param isNeedDial
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (float) animation.getAnimatedValue();
                curValues = currentAngle/k;
                Log.e("TGA","curValues"+curValues+"|currentAngle"+currentAngle+"|startAngle"+startAngle);

            }
        });
        progressAnimator.start();
    }

    public void setShowMoveCircle(boolean showMoveCircle) {
        this.showMoveCircle = showMoveCircle;
    }

    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}