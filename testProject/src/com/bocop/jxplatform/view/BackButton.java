package com.bocop.jxplatform.view;

import com.boc.jx.base.BaseActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义返回按钮
 * @author llc
 *
 */
public class BackButton extends ImageView{

	public BackButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	@Override
	public boolean performClick() {
		((BaseActivity)getContext()).finish();
		return super.performClick();
		
	}
	
}
