package com.bocop.xfjr.view.brokelineview;

import java.util.ArrayList;
import java.util.List;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */


public class BrokenLineChartView extends View {
    private Paint paint;

    private int firstLineColor = Color.parseColor("#8e6cc9");//上面那根线颜色
    private int firstBelowColor = Color.parseColor("#bb9af5");//上面那根线下面的颜色
    private int secondLineColor = Color.parseColor("#3c96d2");//下面那根线的颜色
    private int secondBelowColor = Color.parseColor("#5abdff");//下面那根线的下面颜色
    private int endColor = Color.parseColor("#ffffff");//每根线下面的渐变色最后的颜色
    private int substrateColor = Color.parseColor("#A5A5A5");//坐标的颜色
    private int titleColor = Color.parseColor("#999999");//坐标上文字的颜色
    private int markTitleColor = Color.parseColor("#000000");//x轴的标记文字颜色
    private int imaginaryLineColor = Color.parseColor("#E0E0E0");//中间网格虚线颜色
    private int clickFirstLineBgColor;//点击上面那根线以后的椭圆背景色
    private int clickFirstLineFontColor;//点击上面那根线以后的椭圆中文字颜色
    private int clickFirstLineCircleColor;//点击上面那根线以后的圆圈颜色
    private int clickSecondLineBgColor;//点击下面那根线以后的椭圆背景色
    private int clickSecondLineFontColor;//点击下面那根线以后的椭圆中文字颜色
    private int clickSecondLineCircleColor;//点击下面那根线以后的圆圈颜色

    // 左上角x
    private int LEFTUPX = 50;//左边起点
    // 左上角Y
    private int LEFTUPY = 0;//上边起点
    // 左下角Y
    private int LEFTDOWNY;//x轴坐标
    // 右下角x
    private int RIGHTDOWNX;//x轴右边
    // 上下间隔
    private int UPDOWNSPACE;
    // 左右间隔
    private int LEFTRIGHTSPACE;

    private int intervalTime = 80;//如果要动画显示时的延迟时间

    private int[] lanspaceTitle = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private int[] verticalTitle = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    private ShowRule showRule;

    private int leftrightlines;//横坐标上的个数
    private int updownlines;//纵坐标上的个数
    
    private int dataSize=12;


    private ArrayList<Float> listX = new ArrayList<>();
    private ArrayList<Float> listY = new ArrayList<>();
    private ArrayList<Float> listY2 = new ArrayList<>();

    private int count = 1;
    private int number = 1; // 最大10

    private SelectedView selectedView;
    private List<PointBean> pointFs;//记录所有转折点
    private PointBean clickPoint;//点击后需要显示点击效果的位置以及相关信息
    private boolean isFinish;

    BrokenAdapter mAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (number < 50) {
                number++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, intervalTime);
            } else if (count < dataSize - 1) {
                number = 1;
                count++;
                invalidate();
                handler.sendEmptyMessageDelayed(1, intervalTime);
            } else {
                isFinish = true;
                invalidate();
            }
        }

        ;
    };

    public BrokenLineChartView(Context context) {
        this(context, null);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BrokenLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        if (attrs == null)
            return;
        //初始化各种颜色
        TypedArray typeArr = getContext().obtainStyledAttributes(attrs, R.styleable.BrokenLineChartView);
        firstLineColor = typeArr.getColor(R.styleable.BrokenLineChartView_firstLineColor, Color.parseColor("#8e6cc9"));
        firstBelowColor = typeArr.getColor(R.styleable.BrokenLineChartView_firstBelowColor, Color.parseColor("#bb9af5"));
        secondLineColor = typeArr.getColor(R.styleable.BrokenLineChartView_secondLineColor, Color.parseColor("#3c96d2"));
        secondBelowColor = typeArr.getColor(R.styleable.BrokenLineChartView_secondBelowColor, Color.parseColor("#5abdff"));
        endColor = typeArr.getColor(R.styleable.BrokenLineChartView_endColor, Color.parseColor("#ffffff"));
        substrateColor = typeArr.getColor(R.styleable.BrokenLineChartView_substrateColor, Color.parseColor("#A5A5A5"));
        titleColor = typeArr.getColor(R.styleable.BrokenLineChartView_brokenTitleColor, Color.parseColor("#999999"));
        markTitleColor = typeArr.getColor(R.styleable.BrokenLineChartView_markTitleColor, Color.parseColor("#000000"));
        imaginaryLineColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#E0E0E0"));
        clickSecondLineBgColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#bb9af5"));
        clickSecondLineFontColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#ffffff"));
        clickSecondLineCircleColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#8e6cc9"));
        clickFirstLineBgColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#5abdff"));
        clickFirstLineFontColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#ffffff"));
        clickFirstLineCircleColor = typeArr.getColor(R.styleable.BrokenLineChartView_imaginaryLineColor, Color.parseColor("#3c96d2"));


        typeArr.recycle();
        selectedView = new SelectedView();
    }

    public void setAdapter(final BrokenAdapter mAdapter) {
        this.mAdapter = mAdapter;
        if (mAdapter == null) {
			return ;
		}
        clickPoint=null;
        post(new Runnable() {
            @Override
            public void run() {
                listX.clear();
                listY.clear();
                listY2.clear();
                postInvalidate();
                if (pointFs == null) {
                    pointFs = new ArrayList<>();
                }
                pointFs.clear();
                dataSize=mAdapter.getSize();
                for (int i = 0; i < dataSize; i++) {
                    float x1 = LEFTUPX + (mAdapter.getLands(i)) * LEFTRIGHTSPACE;//最左边的坐标+间隔*倍数
                    float y1 = LEFTDOWNY - (mAdapter.getTotal(i)) * UPDOWNSPACE;//最下面的坐标-间隔*倍数
                    float y2 = LEFTDOWNY - (mAdapter.getActual(i)) * UPDOWNSPACE;
                    listX.add(x1);
                    listY.add(y2<0?0:y2);
                    listY2.add(y1<0?0:y1);
                    LogUtils.e("*******************************************");
                    LogUtils.e("x = "+x1 + " , y2 = "+y2 + " , y1 = " +y1 );
                    LogUtils.e("*******************************************");
                    //记录转折点
                    pointFs.add(new PointBean(new PointF(x1, y1), new PointF(x1, y2), PointBean.THEFIRSTLINE, mAdapter.getTotal(i), mAdapter.getActual(i)));

                }
                number = 1;
                count = 1;
                isFinish = false;
                postInvalidate();
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean illegea = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST;
        if (illegea) {//如果有wrap_content则大小为0
            setMeasuredDimension(0, 0);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("TGA", "width=>" + width + "|height=>" + height);
        LEFTUPX = getPaddingLeft();
        LEFTUPY = getPaddingTop() + 30;
        LEFTDOWNY = height - getPaddingBottom() - LEFTUPY + 30;
        RIGHTDOWNX = width - getPaddingRight() - LEFTUPX - 30;
        if (leftrightlines == 0 || updownlines == 0) {
            return;
        }
        //算出横坐标以及纵坐标的间隔
        LEFTRIGHTSPACE = (RIGHTDOWNX - LEFTUPX) / leftrightlines;
        UPDOWNSPACE = (LEFTDOWNY - LEFTUPY) / updownlines;
    }


    public void setShowRule(ShowRule rule) {
        showRule = rule;
        if (rule.landsTitle() != null && rule.landsTitle().length != 0) {
            lanspaceTitle = rule.landsTitle();
        }
        if (rule.verticalTitle() != null && rule.verticalTitle().length != 0) {
            verticalTitle = rule.verticalTitle();
        }
        leftrightlines = lanspaceTitle.length;
        updownlines = verticalTitle.length;
        intervalTime = rule.intervalTime();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if(true){
//        	paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        	paint.setColor(Color.BLACK);
//        	
//        	Path p = new Path();
//        	p.moveTo(0, 200);
//        	p.lineTo(100, -100);
//        	p.lineTo(200, 200);
//        	canvas.drawLine(0, 30, 60, -10, paint);
//        	canvas.drawLine(60, -10, 120, 100, paint);
//        	paint.setColor(Color.RED);
//        	canvas.drawPath(p , paint);
//        	return ;
//        }
        
        
        getPaint().reset();
        /**
         * 外框线
         */
        // 设置颜色
        getPaint().setColor(substrateColor);
        // 设置宽度
        getPaint().setStrokeWidth(2);
        // 线的坐标点 （四个为一条线）
        float[] pts = {LEFTUPX, LEFTUPY - 20, LEFTUPX, LEFTDOWNY, LEFTUPX, LEFTDOWNY, RIGHTDOWNX + 20, LEFTDOWNY};
        // 画线
        canvas.drawLines(pts, getPaint());

        /**
         * 箭头
         */
        // 通过路径画三角形
        Path path = new Path();
        getPaint().setStyle(Paint.Style.FILL);// 设置为空心
        path.moveTo(LEFTUPX - 5, LEFTUPY - 20);// 此点为多边形的起点
        path.lineTo(LEFTUPX + 5, LEFTUPY - 20);
        path.lineTo(LEFTUPX, LEFTUPY - 35);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, getPaint());
        // 第二个箭头
        path.moveTo(RIGHTDOWNX + 20, LEFTDOWNY - 5);// 此点为多边形的起点
        path.lineTo(RIGHTDOWNX + 20, LEFTDOWNY + 5);
        path.lineTo(RIGHTDOWNX + 35, LEFTDOWNY);
        canvas.drawPath(path, getPaint());

        /**
         *  中间虚线
         */
        float[] pts2 = new float[(updownlines + leftrightlines) * 4];
        // 计算位置y轴标题
        if (updownlines != 0) {
            for (int i = 0; i < updownlines; i++) {

                float x1 = LEFTUPX;
                float y1 = LEFTDOWNY - (i + 1) * UPDOWNSPACE;
                float x2 = RIGHTDOWNX;
                float y2 = LEFTDOWNY - (i + 1) * UPDOWNSPACE;
                pts2[i * 4 + 0] = x1;
                pts2[i * 4 + 1] = y1;
                pts2[i * 4 + 2] = x2;
                pts2[i * 4 + 3] = y2;
                getPaint().setColor(Color.BLACK);
                getPaint().setTextSize(25);
                if (showRule != null) {
                    if (showRule.showVerticalTitle(i)){
                        canvas.drawText(String.valueOf(verticalTitle[i]), x1 + 5, y1 + 10, getPaint(titleColor));
                        canvas.drawLine(x1 + 5, y1 + 10,  RIGHTDOWNX + 20, y1 + 10, getPaint(Color.parseColor("#30000000")));
                    }
                } else {
                    canvas.drawText(String.valueOf(i), x1 + 5, y1 + 10, getPaint(titleColor));
                }
            }
        }

        // 计算位置x轴上标题
        if (leftrightlines != 0) {
            for (int i = 0; i < leftrightlines; i++) {
                float x1 = LEFTUPX + (i + 1) * LEFTRIGHTSPACE;
                float y1 = LEFTUPY;
                float x2 = LEFTUPX + (i + 1) * LEFTRIGHTSPACE;
                float y2 = LEFTDOWNY;
                pts2[(i + updownlines) * 4 + 0] = x1;
                pts2[(i + updownlines) * 4 + 1] = y1;
                pts2[(i + updownlines) * 4 + 2] = x2;
                pts2[(i + updownlines) * 4 + 3] = y2;
                if (showRule != null) {
                    if (showRule.showLandsTitle(i)) {
                        Paint pen = getPaint();
                        if (showRule.markLandsTitle(String.valueOf(lanspaceTitle[i]))) {
                            pen.setColor(markTitleColor);
                            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
                            pen.setTypeface(font);

                        } else {
                            pen.setColor(titleColor);
                            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
                            pen.setTypeface(font);
                        }
                        canvas.drawText(String.valueOf(lanspaceTitle[i]), x2 - 10, y2 + 30, pen);
                    }
                } else {
                    canvas.drawText(String.valueOf(i), x2 - 10, y2 + 30, getPaint(titleColor));
                }
            }
            if (showRule != null && !TextUtils.isEmpty(showRule.landsUnit())) {
                float[] widths = new float[1];
                float width = 0;
                for (int j = 0; j < (String.valueOf(lanspaceTitle[lanspaceTitle.length - 1])).length(); j++) {
                    getPaint().getTextWidths(String.valueOf((String.valueOf(lanspaceTitle[lanspaceTitle.length - 1]).charAt(j))), widths);
                    width += widths[0];
                }
                canvas.drawText(showRule.landsUnit(), LEFTUPX + (leftrightlines) * LEFTRIGHTSPACE + width, LEFTDOWNY + 30, getPaint(titleColor));
            }

        }

        getPaint().setColor(imaginaryLineColor);
        getPaint().setStrokeWidth(1);
        if (showRule != null && showRule.showImaginaryLine())
            canvas.drawLines(pts2, getPaint());
        getPaint().setStrokeWidth(2);
        if (showRule != null) {
            if (intervalTime == 0) {
                number = 50;
                count = dataSize - 1;
                isFinish = true;
                //一定要先画矮的那根线，否则会被遮盖
                if (listY2.size() > 0)
                    canvasDataLines(canvas, listY2, firstLineColor, firstBelowColor);
                if (listY.size() > 0)
                    canvasDataLines(canvas, listY, secondLineColor, secondBelowColor);
            } else {
                if (listY2.size() > 0)
                    canvasDataLines(canvas, listY2, firstLineColor, firstBelowColor);
                if (listY.size() > 0)
                    canvasDataLines(canvas, listY, secondLineColor, secondBelowColor);
                if(!isFinish)
                handler.sendEmptyMessage(1);
            }
            if (clickPoint != null) {
                Log.e("Click", "postInvalidate--->>>onDraw");
                //点击后的浮现效果出现
                selectedView.setPointf(clickPoint.getLine() == PointBean.THEFIRSTLINE ? clickPoint.getUppointF() : clickPoint.getDownPoint())
                        .setAlterBgColor(clickPoint.getLine() == PointBean.THEFIRSTLINE ? clickSecondLineBgColor : clickFirstLineBgColor)
                        .setFontColor(clickPoint.getLine() == PointBean.THEFIRSTLINE ? clickFirstLineFontColor : clickSecondLineFontColor)
                        .setCircleColor(clickPoint.getLine() == PointBean.THEFIRSTLINE ? clickSecondLineCircleColor : clickFirstLineCircleColor)
                        .setTextValue(clickPoint.getLine() == PointBean.THEFIRSTLINE ? clickPoint.getText() : clickPoint.getText2())
                        .setMargin(selectedView.getCrossBorder()?-50:30)
                        .setOY(getWidth()-UPDOWNSPACE)
                        .draw(canvas);
                //如果需要这个浮现效果自动消失就用一个handler将clickPoint制空并且重新绘制就好了
                
            }

        }
    }

    /**
     * 画折线
     *
     * @param canvas
     * @param listY
     * @param color
     * @param endColor
     */
    private void canvasDataLines(Canvas canvas, ArrayList<Float> listY, int color, int endColor) {
        // 线的路径
        Path path2 = new Path();
        // 共几个转折点
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                path2.moveTo(listX.get(i), listY.get(i));
            } else {
                path2.lineTo(listX.get(i), listY.get(i));
            }
        }
        // 上一个点  减去 下一个点的位置 计算中间点位置
        path2.lineTo(listX.get(count - 1) + (listX.get(count) - listX.get(count - 1)) / 50f * number,
                listY.get(count - 1) + (listY.get(count) - listY.get(count - 1)) / 50f * number);
        getPaint().setColor(color);
        getPaint().setStrokeWidth(2);
        getPaint().setStyle(Paint.Style.STROKE);// 设置为空心
        canvas.drawPath(path2, getPaint());

        path2.lineTo(listX.get(count - 1) + (listX.get(count) - listX.get(count - 1)) / 50f * number, LEFTDOWNY);
        path2.lineTo(listX.get(0), LEFTDOWNY);
        path2.lineTo(listX.get(0), listY.get(0));
        getPaint().setStyle(Paint.Style.FILL);// 设置为空心
        canvas.drawPath(path2, getShadeColorPaint(endColor));
        getPaint().reset();
        // 画出转折点圆圈
//        for (int i = 0; i < count; i++) {
//            // 画外圆
//            getPaint().setColor(color);
//            getPaint().setStyle(Paint.Style.FILL);// 设置为空心
//            canvas.drawCircle(listX.get(i), listY.get(i), 7, getPaint());
//            // 画中心点为白色
//            getPaint().setColor(Color.WHITE);
//            getPaint().setStyle(Paint.Style.FILL);
//            canvas.drawCircle(listX.get(i), listY.get(i), 4, getPaint());
//        }
//        if (isFinish) {
//            getPaint().setColor(color);
//            getPaint().setStyle(Paint.Style.FILL);// 设置为空心
//            canvas.drawCircle(listX.get(count), listY.get(count), 7, getPaint());
//            getPaint().setColor(Color.WHITE);
//            getPaint().setStyle(Paint.Style.FILL);
//            canvas.drawCircle(listX.get(count), listY.get(count), 4, getPaint());
//        }

    }

    public void drawBrokenLine() {
        number = 1;
        count = 1;
        isFinish = false;
        invalidate();
    }

    // 获取笔
    private Paint getPaint(int... color) {
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
        }
        if (color != null && color.length > 0) {
            paint.setColor(color[0]);
        }
        return paint;
    }

    // 修改笔的颜色
    private Paint getShadeColorPaint(int belowColor) {
        Shader mShader = new LinearGradient(0, LEFTUPY, 0, LEFTDOWNY + LEFTUPY,
                new int[]{belowColor, endColor}, null, Shader.TileMode.CLAMP);
        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        getPaint().setShader(mShader);
        return getPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("Click", "x===" + event.getX() + "y===" + event.getY());
        if (pointFs != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            PointF downPointF = new PointF(event.getX(), event.getY());
            Log.e("Click", "pointFs长度" + pointFs.size());
            clickPoint = null;
            //循环遍历所有转折点判断点中哪个店
            for (PointBean point : pointFs) {
//                if (JudgePoint(point.getPointF(), downPointF)) {
//                    clickPoint = point;
//                    Log.e("Click", "postInvalidate");
//                    break;
//                }
                //首先是判断是否点击靠近哪次的数据（根据x的值进行判断），然后判断这个点距离在这个x的两个坐标哪个近  就判断为哪个显示效果
                if (Math.abs(point.getDownPoint().x - downPointF.x) < LEFTRIGHTSPACE / 2) {
                    clickPoint = point;
                    if (distance4PointF(point.getUppointF(), downPointF) < distance4PointF(point.getDownPoint(), downPointF)) {
                        clickPoint.setLine(PointBean.THEFIRSTLINE);
                    } else {
                        clickPoint.setLine(PointBean.THESECONDLINE);
                    }
                    break;
                }
            }
            postInvalidate();
        }
        return true;
    }

    private boolean JudgePoint(PointF pointF, PointF downPointf) {
        //点击的点到控制旋转，缩放点的距离
        float distanceToControl = distance4PointF(pointF, downPointf);

        //如果两者之间的距离小于 控制图标的宽度，高度的最小值，则认为点中了控制图标
        if (distanceToControl < 30) {
            return true;
        }
        return false;

    }

    /**
     * 两个点之间的距离
     *
     * @param pf1
     * @param pf2
     * @return
     */
    private float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return ((float) Math.sqrt(disX * disX + disY * disY));
    }
}