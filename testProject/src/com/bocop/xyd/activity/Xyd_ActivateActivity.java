package com.bocop.xyd.activity;

import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.content.Context;
import android.content.Intent;

public class Xyd_ActivateActivity extends XydBaseActivity{

	@Override
	protected int getLoyoutId() {
		// TODO Auto-generated method stub
		return R.layout.xyd_activity_activating;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
	public static void  StartThisActivity(Context context) {
		Intent i=new Intent(context,Xyd_ActivateActivity.class);
//		i.putExtra("STYLE", style);
		context.startActivity(i);
	}
}
