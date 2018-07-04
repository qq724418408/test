package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

public class Xyd_ApplyingActivity  extends XydBaseActivity{

	@Override
	protected int getLoyoutId() {
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		return R.layout.xyd_activity_applyfor_money;
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}
	@OnClick({R.id.ll_applying_back,R.id.tv_applying_submit})
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_applying_back:
			finish();
			break;
		case R.id.tv_applying_submit:
			Xyd_ApplyResultActivity.StartThisActivity(this);
			break;
		default:
			break;
		}
	}
	public static void  StartThisActivity(Context context) {
		Intent i=new Intent(context,Xyd_ApplyingActivity.class);
//		i.putExtra("STYLE", style);
		context.startActivity(i);
	}

}
