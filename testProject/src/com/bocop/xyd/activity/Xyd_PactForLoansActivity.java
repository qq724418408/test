package com.bocop.xyd.activity;

import com.boc.jx.baseUtil.view.annotation.event.OnClick;
import com.bocop.jxplatform.R;
import com.bocop.xyd.base.XydBaseActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class Xyd_PactForLoansActivity  extends XydBaseActivity{

	@Override
	protected int getLoyoutId() {
		return R.layout.xyd_activity_loans_pact;
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}
	
	@OnClick(R.id.ll_pact_back)
	public void back(View view){
		switch (view.getId()) {
		case R.id.ll_pact_back:
			finish();
			break;
		default:
			break;
		}
	}
	public static void  StartThisActivity(Context context) {
		Intent i=new Intent(context,Xyd_PactForLoansActivity.class);
//		i.putExtra("STYLE", style);
		context.startActivity(i);
	}

}
