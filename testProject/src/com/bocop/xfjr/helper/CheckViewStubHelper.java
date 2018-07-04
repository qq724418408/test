package com.bocop.xfjr.helper;

import com.boc.jx.baseUtil.view.ViewUtils;
import com.boc.jx.baseUtil.view.annotation.ViewInject;
import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * description： 检测
 * <p/>
 * Created by TIAN FENG on 2017年9月11日
 * QQ：27674569
 * Email: 27674569@qq.com
 * Version：1.0
 */
public class CheckViewStubHelper {
	private Activity activity;
	private View.OnClickListener listener;
	private ViewStub viewStub;
	@ViewInject(R.id.ivError)
	private ImageView ivError;
	@ViewInject(R.id.tvError)
	private TextView tvError;
	
	public CheckViewStubHelper(Activity activity){
		this.activity = activity;
		this.viewStub = (ViewStub) activity.findViewById(R.id.viewStub);
	}
	
	public void showError1() {
		inflate(R.drawable.xfjr_error_1);
	}
	
	public void showError2() {
		inflate(R.drawable.xfjr_error_2);
	}
	
	public void hide() {
		viewStub.setVisibility(View.GONE);
	}
	
	private void inflate(int res){	
		viewStub.inflate();
		ViewUtils.inject(this, activity);
		viewStub.setVisibility(View.VISIBLE);
		if (ivError!=null) {
			ivError.setImageResource(res);
		}
	}
	
	@OnClick(R.id.errorView)
	private void onClick(View view){
		if (listener!=null) {
			listener.onClick(view);
		}
	}
	
	public void setOnErrorClickListener(View.OnClickListener listener){
		this.listener = listener;
	}
}
