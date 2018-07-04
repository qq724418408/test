package com.bocop.xfjr.view;



import com.bocop.jxplatform.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 功能说明
 * 说明：详细说明
 *
 * @author formssi
 */

public class MyTaskView extends TextView {

    private String taskNum="0";
    private int myTaskColor;
    private float myTaskSize;
    private String oldString;
    
    private boolean showNumText=false;
    
	public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;

	private int mHeight, mWidth;

	private Drawable mDrawable;

	private int mLocation;

    public MyTaskView(Context context) {
    	this(context, null);
    }

    public MyTaskView(Context context, @Nullable AttributeSet attrs) {
    	this(context, attrs,-1);
    }

    public MyTaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        if(showNumText){
        	setTaskNum(TextUtils.isEmpty(taskNum)?"0":taskNum);
        }
    }
    
    public String getTaskNum(){
    	return taskNum;
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray=getContext().obtainStyledAttributes(attrs,R.styleable.taskView);
        myTaskColor=typedArray.getColor(R.styleable.taskView_myTextColor,0);
        myTaskSize=typedArray.getDimension(R.styleable.taskView_myTextSize,0);
        taskNum=typedArray.getString(R.styleable.taskView_myNumText);
        showNumText=typedArray.getBoolean(R.styleable.taskView_showNumText, false);
        mWidth = typedArray.getDimensionPixelSize(R.styleable.taskView_drawable_width, 0);
		mHeight = typedArray.getDimensionPixelSize(R.styleable.taskView_drawable_height,0);
		mDrawable = typedArray.getDrawable(R.styleable.taskView_drawable_src);
		mLocation = typedArray.getInt(R.styleable.taskView_drawable_location, LEFT);
        typedArray.recycle();
        Log.e("tag", "kuan="+mWidth+";gao="+mHeight+";location="+mLocation);
      //绘制Drawable宽高,位置
      	drawDrawable();
    }
    public MyTaskView setMyTaskColor(int color){
        myTaskColor=color;
        return this;
    }
    public String getNum(){
    	return taskNum;
    }
    //dip
    public MyTaskView setMyTaskSize(float myTaskSize) {
        this.myTaskSize = myTaskSize;
        return this;
    }

    public MyTaskView setOldString(String oldString) {
        this.oldString = oldString;
        return this;
    }

    public void  setTaskNum(String num){
        taskNum=num;
        if(TextUtils.isEmpty(oldString))
            oldString=getText().toString();
        setText(dealTextViewColor(taskNum));
    }
    private SpannableStringBuilder dealTextViewColor(String num){
        String text=num+"\n"+oldString;
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        if(myTaskColor!=0){
            builder.setSpan(new ForegroundColorSpan(myTaskColor), 0, String.valueOf(num).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if(myTaskSize!=0){
            builder.setSpan(new AbsoluteSizeSpan((int) myTaskSize,true),0, String.valueOf(num).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
    
    
    /**
	 * 绘制Drawable宽高,位置
	 */
	public void drawDrawable() {

		if (mDrawable != null) {
			Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
			Drawable drawable;
			if (mWidth != 0 && mHeight != 0) {

				drawable = new BitmapDrawable(getResources(), getBitmap(bitmap,
						mWidth, mHeight));

			} else {
				drawable = new BitmapDrawable(getResources(),
						Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(),
								bitmap.getHeight(), true));
			}

			switch (mLocation) {
			case LEFT:
				this.setCompoundDrawablesWithIntrinsicBounds(drawable, null,
						null, null);
				break;
			case TOP:
				this.setCompoundDrawablesWithIntrinsicBounds(null, drawable,
						null, null);
				break;
			case RIGHT:
				this.setCompoundDrawablesWithIntrinsicBounds(null, null,
						drawable, null);
				break;
			case BOTTOM:
				this.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
						drawable);
				break;
			}
		}

	}

	/**
	 * 缩放图片
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public Bitmap getBitmap(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = (float) newWidth / width;
		float scaleHeight = (float) newHeight / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	}
}
