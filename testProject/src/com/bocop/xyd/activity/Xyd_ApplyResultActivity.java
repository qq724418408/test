package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Xyd_ApplyResultActivity extends XydBaseActivity{

	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_apply_sucess;
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}
	
	@OnClick({R.id.ll_applyresult_back,R.id.tv_applyresult_buttombtn})
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_applyresult_back:
			finish();
			break;
		case R.id.tv_applyresult_buttombtn:
			Xyd_UseMoneyDetailActivity.StartThisActivity(this);
			break;
		default:
			break;
		}
	}
	public static void  StartThisActivity(Context context) {
		Intent i=new Intent(context,Xyd_ApplyResultActivity.class);
//		i.putExtra("STYLE", style);
		context.startActivity(i);
	}

}
