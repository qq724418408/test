package com.bocop.xfjr.view.brokelineview;

import com.boc.jx.tools.LogUtils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * 功能说明 说明：详细说明
 *
 * @author formssi
 */

public class SelectedView {

	private float x = 0;
	private int oy = 0;// 坐标轴原点的y值
	private float y = 0;
	private int fontSize = 36;
	private int textValue = 0;
	private int alterBgColor = Color.LTGRAY;
	private int fontColor = Color.WHITE;
	private int circleColor = Color.RED;
	

	private float textWidth, textHeight;
	private Paint mPaintBoxText;
	private float margin = 30;// -50

	public void draw(Canvas canvas) {
		if (x != 0 && y != 0) {
			Paint mPaintFill = new Paint();
			mPaintFill.setColor(fontColor);
			mPaintFill.setStyle(Paint.Style.FILL);

			Paint mPaintBorder = new Paint();
			mPaintBorder.setAntiAlias(true);
			mPaintBorder.setStyle(Paint.Style.STROKE);
			mPaintBorder.setStrokeWidth(2);
			mPaintBorder.setColor(circleColor);
			canvas.drawCircle(x, y, 10, mPaintFill);
			canvas.drawCircle(x, y, 10, mPaintBorder);

			Paint mPaintBox = new Paint();
			mPaintBox.setColor(alterBgColor);
			mPaintBox.setStyle(Paint.Style.FILL);

			float paddingLeftRight = 20;
			float paddingTopBottom = 12;
			float left = x - textWidth / 2 - paddingLeftRight;
			float right = x + textWidth / 2 + paddingLeftRight;
			float top = y - margin - textHeight / 2 - paddingTopBottom;
			float bottom = y - margin + textHeight / 2 - paddingTopBottom;
			
			// canvas.draw
			RectF rectF = new RectF(left, top, right, bottom);
			// 画圆角矩形
			canvas.drawRoundRect(rectF, textWidth / 2, (textHeight + paddingTopBottom) / 2, mPaintBox);
			canvas.drawText(String.valueOf(textValue), x - textWidth / 2, y - margin, getTextPaint());
		}
	}

	public SelectedView setAlterBgColor(int alterBgColor) {
		this.alterBgColor = alterBgColor;
		return this;
	}

	public SelectedView setFontColor(int fontColor) {
		this.fontColor = fontColor;
		return this;
	}

	public SelectedView setCircleColor(int circleColor) {
		this.circleColor = circleColor;
		return this;
	}

	public SelectedView setX(float x) {
		this.x = x;
		return this;
	}

	public SelectedView setPointf(PointF pointf) {
		this.x = pointf.x;
		this.y = pointf.y;
		return this;
	}

	public SelectedView setY(float y) {
		this.y = y;
		return this;
	}
	
	public SelectedView setOY(int oy) {
		this.oy = oy;
		return this;
	}
	

	public SelectedView setFontSize(int fontSize) {
		this.fontSize = fontSize;
		calculateText();
		return this;
	}

	public SelectedView setMargin(float margin) {
		this.margin = margin;
		return this;
	}

	public SelectedView setTextValue(float textValue) {
		this.textValue = (int) textValue;
		calculateText();
		return this;
	}

	private void calculateText() {
		String valueStr = String.valueOf(textValue);
		textWidth = getTextPaint().measureText(valueStr);
		Paint.FontMetrics fm = getTextPaint().getFontMetrics();
		textHeight = (float) Math.ceil(fm.descent - fm.ascent);
	}

	private Paint getTextPaint() {
		if (mPaintBoxText == null) {
			mPaintBoxText = new Paint();
			mPaintBoxText.setColor(Color.WHITE);
			mPaintBoxText.setAntiAlias(true);
			mPaintBoxText.setTextSize(fontSize);
		}
		return mPaintBoxText;
	}

	public float getViewHeight() {
		return textHeight + margin;
	}

	public boolean getCrossBorder() {
		return y - textHeight - 10 <= 0;
	}

}
