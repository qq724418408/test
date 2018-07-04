package com.bocop.xfjr.view;

import com.boc.jx.tools.LogUtils;
import com.bocop.jxplatform.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * description：固定宽高的 圆角图片 height = 0.6*width
 * <p/>
 * Created by TIAN FENG on 2017/8/29 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */

@SuppressLint({ "AppCompatCustomView", "DrawAllocation" })
public class RoundImageView extends ImageView {
	/**
	 * /** 圆角的大小 16 9
	 */
	private int mBorderRadius;

	/**
	 * 绘图的Paint
	 */
	private Paint mBitmapPaint;

	public RoundImageView(Context context, AttributeSet attrs) {

		super(context, attrs);
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);

		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);

		mBorderRadius = a.getDimensionPixelSize(R.styleable.RoundImageView_borderRadius,
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));// 默认为10dp

		a.recycle();
	}

	public RoundImageView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			setMeasuredDimension(0, 0);
			return;
		}

		int width = MeasureSpec.getSize(widthMeasureSpec);
		LogUtils.e("width:" + width + "height:" + MeasureSpec.getSize(heightMeasureSpec));
		setMeasuredDimension(width, (int) (width * 0.6));
		LogUtils.e("width:" + getWidth() + "height:" + getHeight());
		invalidate();
	}

	/**
	 *
	 * @param rx
	 *            x方向弧度
	 * @param ry
	 *            y方向弧度
	 */
	public void setRadius(int r) {
		this.mBorderRadius = r;
	}

	private boolean isFirst = true;
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (null != drawable&&isFirst) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			Bitmap b = getRoundBitmap(bitmap, mBorderRadius);
			final Rect rectSrc = new Rect(0, 0, getWidth(), getHeight());
			final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
			mBitmapPaint.reset();
			isFirst = false;
			canvas.drawBitmap(b, rectSrc, rectDest, mBitmapPaint);

		} else {
			super.onDraw(canvas);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
	}

	/**
	 * 等比缩放图片
	 */
	public Bitmap zoomImg(Bitmap bm, float newWidth, float newHeight, float scan) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scan, scan);
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inSampleSize = 2;
		op.inJustDecodeBounds = true;
		op.inPreferredConfig = Bitmap.Config.ARGB_4444;
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		int startX = (newbm.getWidth() - getWidth()) / 2;
		int startY = (newbm.getHeight() - getHeight()) / 2;
		newbm = Bitmap.createBitmap(newbm, startX, startY, getWidth(), getHeight());
		return newbm;
	}

	/**
	 * 获取圆角矩形图片方法
	 * 
	 * @param bitmap
	 * @param roundPx,一般设置成14
	 * @return Bitmap
	 * @author caizhiming
	 */
	@SuppressLint("NewApi")
	private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Rect rect = new Rect(0, 0, getWidth(), getHeight());
		final RectF rectF = new RectF(rect);
		mBitmapPaint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, roundPx, roundPx, mBitmapPaint);
		mBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, mBitmapPaint);

		return output;

	}

}