package com.bocop.xfjr.view;

import com.bocop.jxplatform.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * description：Edit联动右侧image
 * <p/>
 * Created by TIAN FENG on 2017/9/4 QQ：27674569 Email: 27674569@qq.com
 * Version：1.0
 */

public class EditChangeView extends FrameLayout
		implements TextWatcher, View.OnFocusChangeListener, View.OnClickListener {

	private EditText mEditext;
	private ImageView mImageView;

	public EditChangeView(Context context) {
		this(context, null);
	}

	public EditChangeView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EditChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		inflate(context, R.layout.xfjr_edit_change_view, this);
		initEditChangeView(context, attrs);
	}

	@SuppressLint("RtlHardcoded")
	private void initEditChangeView(Context context, AttributeSet attrs) {
		mEditext = (EditText) findViewById(R.id.editext);
		mImageView = (ImageView) findViewById(R.id.image);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditChangeView);
		mEditext.setHintTextColor(a.getColor(R.styleable.EditChangeView_ecvHintColor, Color.parseColor("#FFCAC8C8")));
		mEditext.setHint(a.getString(R.styleable.EditChangeView_ecvHint));
		mEditext.setTextColor(a.getColor(R.styleable.EditChangeView_ecvTextColor, Color.BLACK));
		mEditext.setText(a.getString(R.styleable.EditChangeView_ecvText));
		mEditext.setTextSize(TypedValue.COMPLEX_UNIT_PX,a.getDimension(R.styleable.EditChangeView_ecvTextSize,14));
		mImageView.setImageResource(a.getResourceId(R.styleable.EditChangeView_ecvRightSrc, R.drawable.gm_close));
		int gravity = a.getInt(R.styleable.EditChangeView_ecvGravity, -1);
		switch (gravity) {
		case -1:
			mEditext.setGravity(Gravity.LEFT);
			break;
		case 0:
			mEditext.setGravity(Gravity.CENTER);
			break;
		case 1:
			mEditext.setGravity(Gravity.RIGHT);
			break;
		}
		a.recycle();
		initEvent();
	}

	/*************************************************************************************
	 * 函数区
	 *************************************************************************************/
	/**
	 * hint 颜色
	 */
	public EditChangeView setHintColor(int color) {
		mEditext.setHintTextColor(color);
		return this;
	}

	/**
	 * hint 颜色
	 */
	public EditChangeView setHint(String hint) {
		mEditext.setHint(hint);
		return this;
	}

	/**
	 * 文本 颜色
	 */
	public EditChangeView setTextColor(int color) {
		mEditext.setTextColor(color);
		return this;
	}

	/**
	 * 文本
	 */
	public EditChangeView setText(String text) {
		mEditext.setText(text);
		return this;
	}

	/**
	 * 文本字体大小
	 */
	public EditChangeView setTextSize(float textSize) {
		mEditext.setTextSize(dip2px(textSize));
		return this;
	}

	/**
	 * 图片
	 */
	public EditChangeView setRightSrc(int resId) {
		mImageView.setImageResource(resId);
		return this;
	}

	/**
	 * 图片
	 */
	public EditChangeView setRightBitmap(Bitmap bmp) {
		mImageView.setImageBitmap(bmp);
		return this;
	}

	/**
	 * 获取文本
	 */
	public String getText() {
		return mEditext.getText().toString();
	}

	/*************************************************************************************/
	private void initEvent() {
		mEditext.addTextChangedListener(this);
		mEditext.setOnFocusChangeListener(this);
		mImageView.setOnClickListener(this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			mImageView.setVisibility(VISIBLE);
		} else {
			mImageView.setVisibility(GONE);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			// 此处为得到焦点时的处理内容
			if (mEditext.getText().length() > 0) {
				mImageView.setVisibility(VISIBLE);
			} else {
				// 此处为失去焦点时的处理内容
				mImageView.setVisibility(GONE);
			}
		} else {
			// 此处为失去焦点时的处理内容
			mImageView.setVisibility(GONE);
		}
	}

	@Override
	public void onClick(View v) {
		mEditext.setText("");
	}

	private int dip2px(float dipValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f); // +0.5是为了向上取整
	}
}
