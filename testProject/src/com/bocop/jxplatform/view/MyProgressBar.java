package com.bocop.jxplatform.view;

import com.bocop.jxplatform.R;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyProgressBar {

	private ProgressBar progressBar;
//	private Dialog dialog;
//	private LinearLayout mainLy;
//	private LayoutParams mainLp;
	private RelativeLayout parent;
	private RelativeLayout.LayoutParams progressLp;
	private TextView promptTv;

	/**
	 * 创建一个新的实例 MyProgressBar(对话框方式)
	 *
	 * @param context
	 */
	public MyProgressBar(Context context, String prompt) {
		progressBar = new ProgressBar(context);
		progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.loading));
	}
	
	/**
	 * 
	 * 创建一个新的实例 MyProgressBar(子视图方式)
	 *
	 * @param context
	 * @param parent 父布局
	 */
	public MyProgressBar(Context context, RelativeLayout parent) {
		this.parent = parent;
		progressBar = new ProgressBar(context);
		progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.loading));
		progressLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		progressLp.addRule(RelativeLayout.CENTER_IN_PARENT);		 
	}

	public MyProgressBar(Context context, RelativeLayout parent, int centerType, int xOffset, int yOffset) {
		this.parent = parent;
		progressBar = new ProgressBar(context);
		progressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.loading));
		progressLp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		progressLp.addRule(centerType);
		progressLp.leftMargin = xOffset;
		progressLp.topMargin = yOffset;
	}
	
	/** 
	 * addView(在布局中显示进度条)
	 */
	public void addView() {
		if (parent != null) {
			parent.addView(progressBar, progressLp);
		}
	}
	
	/** 
	 * removeView(在布局中移除进度条)
	 */
	public void removeView() {
		if (parent != null) {
			parent.removeView(progressBar);
		}
	}
	
	/**
	 * setPromptText(设置对话框提示文本)
	 */
	public void setPromptText(String prompt){
		promptTv.setText(prompt);
	}
	
	/**
	 * setPromptText(设置对话框提示文本)
	 */
	public void setPromptText(int resid) {
		promptTv.setText(resid);
	}
}

