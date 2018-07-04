package com.boc.jx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.bocop.jxplatform.R;

public class LoadingView extends LinearLayout {
	
	@ViewInject(R.id.llTryAgain)
	private LinearLayout llTryAgain;
	@ViewInject(R.id.tvFailedTips)
	private TextView tvFailedTips;

	private OnRetryListener mOnRetryListener;

	public LoadingView(Context context) {
		super(context);
	}

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode())
			return;
		View view = LayoutInflater.from(context).inflate(R.layout.commom_layout_loadingview, this);
		ViewUtils.inject(this, view);
		llTryAgain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 重试启动后重置点击事件，防止重复点击 */
				if (mOnRetryListener != null) {
					mOnRetryListener.retry();
					setVisibility(View.GONE);
				}
//				llTryAgain.setOnClickListener(null);
			}
		});
	}

	/**
	 * 获取失败时的提示文字
	 *
	 */
	public TextView getTvFailedTips() {
		return tvFailedTips;
	}

	/**
	 * 设置失败时的提示文字
	 *
	 * @param failedTipsTxt
	 */
	public void setTvFailedTips(String failedTipsTxt) {
		this.tvFailedTips.setText(failedTipsTxt);
	}

    public void setmOnRetryListener(OnRetryListener mOnRetryListener) {
		this.mOnRetryListener = mOnRetryListener;
	}

	/**
     * 重试监听
     */
    public interface OnRetryListener {
        /**
         * 实现在请求重试时的操作
         */
        public void retry();
    }

}
