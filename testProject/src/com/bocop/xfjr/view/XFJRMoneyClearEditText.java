package com.bocop.xfjr.view;

import com.bocop.jxplatform.R;
import com.bocop.xfjr.util.MoneyFormatTextWatcher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * 带清空图标的金额EditText
 * 
 * @author wujunliu
 *
 */
public class XFJRMoneyClearEditText extends EditText implements OnFocusChangeListener/*, TextWatcher */{

	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	private int textMaxLength; // 整数位数
	private int decimalDigits = 2; // 小数位数
//	private String origin = "";
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFoucs;

	public XFJRMoneyClearEditText(Context context) {
		this(context, null);
	}

	public XFJRMoneyClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public XFJRMoneyClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * 获取正真的数值
	 * 
	 * @return
	 */
	public String getTextString() {
		String s = getText().toString().trim();
		if (s.endsWith(".")) {
			return s.replaceAll(",", "").replace(".", "").replace("￥", "");
		} else {
			return s.replaceAll(",", "").replace("￥", "");
		}
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XFJRMoneyClearEditText);
		textMaxLength = a.getInt(R.styleable.XFJRMoneyClearEditText_textMaxLength, 10);
		a.recycle();
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			// throw new NullPointerException("You can add drawableRight
			// attribute in XML");
			mClearDrawable = getResources().getDrawable(R.drawable.xfjr_ic_delete2);
		}

		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(new MoneyFormatTextWatcher(this, textMaxLength, decimalDigits){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (hasFoucs) {
					setClearIconVisible(s.length() > 0);
				}
			}
		});
		//setFilters(new InputFilter[]{inputFilter});
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right,
				getCompoundDrawables()[3]);
	}

//	private InputFilter inputFilter = new InputFilter() {
//	    @Override
//	    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//	        // 删除等特殊字符，直接返回
//	        if (TextUtils.isEmpty(source)) {
//	            return null;
//	        }
//	        String dValue = dest.toString();
//	        String[] splitArray = dValue.split("\\.");
//	        if (splitArray.length > 1) {
//	            String dotValue = splitArray[1];
//	            // decimalDigits 表示输入框的小数位数
//	            int diff = dotValue.length() + 1 - decimalDigits;
//	            if (diff > 0) {
//	                return source.subSequence(start, end - diff);
//	            }
//	        }
//	        return null;
//	    }
//	};

//	/**
//	 * 当输入框里面内容发生变化的时候回调的方法
//	 */
//	@Override
//	public void onTextChanged(CharSequence s, int start, int count, int after) {
//		if (hasFoucs) {
//			setClearIconVisible(s.length() > 0);
//		}
//		 //如果输入框为空则不处理
//	    if (TextUtils.isEmpty(s)) {
//	        return;
//	    }
//	    LogUtils.e("onTextChanged：" + s.toString());
////		if (s.length() == 1 && s.toString().equals("0")) {
////	    	setText("");
////	    	return;
////	    }
////		if (s.length() == 1 && s.toString().startsWith("￥")) {
////			this.setText("");
////			return;
////		}
////	    int counter = counter(s.toString(), '.');
////	    if (counter > 1) {
////	        //小数点第一次出现的位置
////	        int index = s.toString().indexOf('.');
////	        setText(s.subSequence(0, index + 1));
////	    }
//	    setSelection(s.toString().length()); 
//	}

//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//   
//	}

//	@Override
//	public void afterTextChanged(Editable s) {
//		if (s.length() > 0) {
//			int comma = s.length() - getTextString().length();
//			LogUtils.e("afterTextChanged：" + s.toString());
//			setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(textMaxLength + comma)});
//			// 判断在输入过程中是否不同，也就是数值改变了，如果改变了，那么就会调用addComma()方法返回新的字符串数值赋值给EditText
//			if (!XFJRUtil.money.equals(this.getTextString())) {
//				// 不是0.开头的字符串不能继续输入0开头的数字，不能输入一个0
////			    if (s.length() > 1 && !s.toString().contains(".") && s.toString().startsWith("0")) {
////			    	setText("");
////			    	return;
////			    }
//			    //第一个字符为小数点的时候
//			    if (s.length() == 1 && s.toString().equals(".")) {
//			        setText("");
//			        return;
//			    }
//			    if (s.length() == 1 && s.toString().equals("0")) {
//			    	setText("");
//			    	return;
//			    }
//				if (s.length() == 1 && s.toString().startsWith("￥")) {
//					this.setText("");
//					return;
//				}
//				this.setText("￥" + XFJRUtil.addComma(this.getTextString(), this)); // 如果需要金额加,号显示打开这个注释即可
//				this.setSelection(XFJRUtil.addComma(this.getTextString(), this).length() + 1); // 如果需要金额加,号显示打开这个注释即可
//			}
//		}
//	}

//	@Override
//	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//		origin = s.toString();
//	}
//
//	@Override
//	public void onTextChanged(CharSequence s, int start, int before, int count) {
//		
//	}
//
//	@Override
//	public void afterTextChanged(Editable s) {
//		String finalStr = s.toString();
//		if (finalStr.equals("¥"))
//			setText("");
//		if (finalStr.equals(origin) || TextUtils.isEmpty(finalStr))
//			return;
//		finalStr = finalStr.replaceAll(",", "").replace("¥", "");
//		String regex = "^[1-9]\\d{0,%d}(\\.\\d{0,%d})?$";
//		regex = String.format(regex, textMaxLength - 1, decimalDigits);
//		if (finalStr.matches(regex)) {
//			if (finalStr.endsWith("."))
//				return;
//			StringBuilder stringBuilder = new StringBuilder("¥ ,###.");
//			for (int i = 0; i < decimalDigits; i++) {
//				stringBuilder.append("#");
//			}
//			String format = new DecimalFormat(stringBuilder.toString())
//					.format(Double.parseDouble(finalStr));
//			setText(format);
//		} else {
//			setText(origin);
//		}
//		setSelection(getText().length());
//	}

	/**
	 * 统计一个字符在字符串中出现的次数
	 *
	 * @param s
	 *            字符串
	 * @param c
	 *            字符
	 * @return 數量
	 */
//	public int counter(String s, char c) {
//		int count = 0;
//		for (int i = 0; i < s.length(); i++) {
//			if (s.charAt(i) == c) {
//				count++;
//			}
//		}
//		return count;
//	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation(int counts) {
		this.setAnimation(shakeAnimation(counts));
	}

	/**
	 * 晃动动画
	 * 
	 * @param counts
	 *            1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

}
